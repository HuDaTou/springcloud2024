package com.overthinker.cloud.media.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.overthinker.cloud.common.core.exception.BusinessException;
import com.overthinker.cloud.common.core.exception.FileUploadException;
import com.overthinker.cloud.common.core.resp.ReturnCodeEnum;
import com.overthinker.cloud.api.apis.media.ENUM.MediaUploadRuleEnum;
import com.overthinker.cloud.media.config.MinioProperties;
import com.overthinker.cloud.media.entity.DTO.InitiateMultipartUploadDTO;
import com.overthinker.cloud.media.entity.PO.FileUploadRules;
import com.overthinker.cloud.media.entity.PO.MediaAsset;
import com.overthinker.cloud.media.entity.VO.MediaAssetVO;
import com.overthinker.cloud.media.enums.MediaStatusEnum;
import com.overthinker.cloud.media.mapper.MediaAssetMapper;
import com.overthinker.cloud.media.service.FileUploadRulesService;
import com.overthinker.cloud.media.service.MediaAssetService;
import com.overthinker.cloud.media.service.UploadService;
import com.overthinker.cloud.media.utils.MediaUtils;
import com.overthinker.cloud.system.starter.redis.utils.MyRedisCache;
import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.Part;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 文件上传服务实现类
 *
 * @author overthinker
 * @since 2025-08-02
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UploadServiceImpl implements UploadService {

    private final MinioClient minioClient;
    private final MinioAsyncClient minioAsyncClient;
    private final MinioProperties minioProperties;
    private final MediaAssetMapper mediaAssetMapper;
    private final MyRedisCache redisCache;
    private final MediaAssetService mediaAssetService;
    private final FileUploadRulesService fileUploadRulesService;

    private static final String UPLOAD_ID_CACHE_PREFIX = "media:uploadId:";

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> handleFirstPartAndGenerateUrls(Long userId, InitiateMultipartUploadDTO dto) throws Exception {
        log.info("开始初始化分片上传，用户ID: {}, 文件名: {}", userId, dto.filename());

        FileUploadRules fileUploadRules = fileUploadRulesService.getById(dto.fileType());
        if (fileUploadRules == null) {
            throw new BusinessException(ReturnCodeEnum.PARAM_ERROR, "文件类型规则不存在，文件类型ID: " + dto.fileType());
        }

        long maxSizeBytes = fileUploadRules.getMaxSizeKb() * 1024L;
        if (dto.fileSize() > maxSizeBytes) {
            throw new FileUploadException(
                    "文件大小超过限制，最大允许: " + MediaUtils.formatFileSize(maxSizeBytes) + 
                    ", 当前文件: " + MediaUtils.formatFileSize(dto.fileSize()),
                    ReturnCodeEnum.FILE_SIZE_ERROR
            );
        }

        if (fileUploadRules.getAllowedExtensions() != null && 
            !fileUploadRules.getAllowedExtensions().isEmpty() && 
            !fileUploadRules.getAllowedExtensions().contains(dto.contentType())) {
            throw new FileUploadException(
                    "不支持的文件类型: " + dto.contentType() + 
                    "，允许的类型: " + String.join(", ", fileUploadRules.getAllowedExtensions()),
                    ReturnCodeEnum.FILE_TYPE_ERROR
            );
        }

        MediaAsset existingAsset = mediaAssetMapper.selectOne(
                new LambdaQueryWrapper<MediaAsset>()
                        .eq(MediaAsset::getFileMd5, dto.fileMd5())
                        .eq(MediaAsset::getStatus, MediaStatusEnum.UPLOADED.getCode())
                        .last("LIMIT 1")
        );

        if (existingAsset != null) {
            log.info("文件秒传命中，MD5: {}。直接复用现有文件: {}", dto.fileMd5(), existingAsset.getObjectName());
            MediaAsset newAsset = new MediaAsset()
                    .setFileName(dto.filename())
                    .setObjectName(existingAsset.getObjectName())
                    .setBucketName(existingAsset.getBucketName())
                    .setFileSize(existingAsset.getFileSize())
                    .setFileType(existingAsset.getFileType())
                    .setFileMd5(dto.fileMd5())
                    .setUploaderId(userId)
                    .setStatus(MediaStatusEnum.UPLOADED.getCode());
            mediaAssetMapper.insert(newAsset);

            Map<String, Object> result = new HashMap<>();
            result.put("instantUpload", true);
            result.put("objectName", newAsset.getObjectName());
            result.put("assetId", newAsset.getId());
            return result;
        }

        String fileExtension = MediaUtils.getFileExtension(dto.filename());
        if (fileExtension.isEmpty()) {
            fileExtension = "." + dto.contentType().split("/")[1];
        }
        String prefix = fileUploadRules.getStoragePrefix() != null ? fileUploadRules.getStoragePrefix() : "";
        String objectName = prefix + UUID.randomUUID().toString().replace("-", "") + fileExtension;

        MediaAsset asset = new MediaAsset()
                .setFileName(dto.filename())
                .setObjectName(objectName)
                .setBucketName(minioProperties.getBucketName())
                .setFileSize(dto.fileSize())
                .setFileType(dto.contentType())
                .setFileMd5(dto.fileMd5())
                .setUploaderId(userId)
                .setStatus(MediaStatusEnum.UPLOADING.getCode());
        mediaAssetMapper.insert(asset);

        CreateMultipartUploadResponse response = minioAsyncClient.createMultipartUploadAsync(
                minioProperties.getBucketName(),
                null,
                objectName,
                null,
                null
        ).join();

        String uploadId = response.result().uploadId();
        log.info("为文件 {} 生成分片上传任务ID: {} for object: {}", dto.filename(), uploadId, objectName);

        Map<String, String> cacheValue = new HashMap<>();
        cacheValue.put("objectName", objectName);
        cacheValue.put("fileMd5", dto.fileMd5());
        redisCache.setCacheObject(UPLOAD_ID_CACHE_PREFIX + uploadId, cacheValue, 2, TimeUnit.HOURS);

        Map<Integer, String> presignedUrls = new LinkedHashMap<>();
        for (int partNumber = 1; partNumber <= dto.totalParts(); partNumber++) {
            Map<String, String> queryParams = new HashMap<>();
            queryParams.put("uploadId", uploadId);
            queryParams.put("partNumber", String.valueOf(partNumber));
            try {
                String url = minioAsyncClient.getPresignedObjectUrl(
                        GetPresignedObjectUrlArgs.builder()
                                .method(Method.PUT)
                                .bucket(minioProperties.getBucketName())
                                .object(objectName)
                                .expiry(2, TimeUnit.HOURS)
                                .extraQueryParams(queryParams)
                                .build());
                presignedUrls.put(partNumber, url);
            } catch (Exception e) {
                log.error("生成预签名URL失败，partNumber: {}", partNumber, e);
                throw new FileUploadException("生成预签名URL失败: " + e.getMessage(), ReturnCodeEnum.FILE_UPLOAD_ERROR);
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("instantUpload", false);
        result.put("uploadId", uploadId);
        result.put("objectName", objectName);
        result.put("assetId", asset.getId());
        result.put("presignedUrls", presignedUrls);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long completeMultipartUpload(String uploadId) {
        log.info("开始完成分片上传，uploadId: {}", uploadId);

        Map<String, String> cacheValue = redisCache.getCacheObject(UPLOAD_ID_CACHE_PREFIX + uploadId);
        if (cacheValue == null || cacheValue.get("objectName") == null) {
            throw new BusinessException(ReturnCodeEnum.PARAM_ERROR, "上传会话已过期或uploadId无效: " + uploadId);
        }
        String objectName = cacheValue.get("objectName");
        String originalMd5 = cacheValue.get("fileMd5");

        try {
            CompletableFuture<ListPartsResponse> future = minioAsyncClient.listPartsAsync(
                    minioProperties.getBucketName(),
                    null,
                    objectName,
                    1000,
                    0,
                    uploadId,
                    null,
                    null
            );

            ListPartsResponse listPartsResponse = future.join();

            List<Part> completeParts = listPartsResponse.result().partList().stream()
                    .map(part -> new Part(part.partNumber(), part.etag()))
                    .collect(Collectors.toList());

            CompletableFuture<ObjectWriteResponse> completeFuture = minioAsyncClient.completeMultipartUploadAsync(
                    minioProperties.getBucketName(),
                    null,
                    objectName,
                    uploadId,
                    completeParts.toArray(new Part[0]),
                    null,
                    null
            );
            completeFuture.join();

            log.info("分片合并成功，文件已生成：{}", objectName);

            String serverSideMd5 = calculateMinioObjectMd5(objectName);

            if (!serverSideMd5.equalsIgnoreCase(originalMd5)) {
                log.error("文件完整性校验失败！客户端MD5: {}, 服务端MD5: {}. 文件: {}", originalMd5, serverSideMd5, objectName);
                minioClient.removeObject(RemoveObjectArgs.builder()
                        .bucket(minioProperties.getBucketName())
                        .object(objectName)
                        .build());
                MediaAsset assetUpdate = new MediaAsset().setStatus(MediaStatusEnum.FAILED.getCode());
                mediaAssetMapper.update(assetUpdate, new LambdaQueryWrapper<MediaAsset>()
                        .eq(MediaAsset::getObjectName, objectName));
                throw new FileUploadException("文件完整性校验失败", ReturnCodeEnum.FILE_UPLOAD_ERROR);
            }

            MediaAsset assetUpdate = new MediaAsset()
                    .setStatus(MediaStatusEnum.UPLOADED.getCode());
            mediaAssetMapper.update(assetUpdate, new LambdaQueryWrapper<MediaAsset>()
                    .eq(MediaAsset::getObjectName, objectName));

            MediaAsset completedAsset = mediaAssetMapper.selectOne(
                    new LambdaQueryWrapper<MediaAsset>().eq(MediaAsset::getObjectName, objectName));
            mediaAssetService.processMediaAsset(completedAsset);

            log.info("成功完成文件分片上传并校验通过: objectName: {}, uploadId: {}, assetId: {}", objectName, uploadId, completedAsset.getId());
            return completedAsset.getId();

        } catch (BusinessException | FileUploadException e) {
            throw e;
        } catch (Exception e) {
            log.error("完成文件分片上传失败: objectName: {}, uploadId: {}", objectName, uploadId, e);
            MediaAsset assetUpdate = new MediaAsset().setStatus(MediaStatusEnum.FAILED.getCode());
            mediaAssetMapper.update(assetUpdate, new LambdaQueryWrapper<MediaAsset>()
                    .eq(MediaAsset::getObjectName, objectName));
            throw new FileUploadException("完成分片上传失败: " + e.getMessage(), ReturnCodeEnum.FILE_UPLOAD_ERROR);
        } finally {
            redisCache.deleteObject(UPLOAD_ID_CACHE_PREFIX + uploadId);
        }
    }

    private String calculateMinioObjectMd5(String objectName) throws Exception {
        try (InputStream stream = minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(minioProperties.getBucketName())
                        .object(objectName)
                        .build())) {
            return DigestUtils.md5DigestAsHex(stream);
        }
    }

    @Override
    public Page<MediaAssetVO> listFiles(int pageNum, int pageSize) {
        Page<MediaAsset> page = new Page<>(pageNum, pageSize);
        Page<MediaAsset> assetPage = mediaAssetMapper.selectPage(page, 
                new LambdaQueryWrapper<MediaAsset>()
                        .orderByDesc(MediaAsset::getCreateTime));

        Page<MediaAssetVO> voPage = new Page<>(pageNum, pageSize, assetPage.getTotal());
        List<MediaAssetVO> voList = assetPage.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
        voPage.setRecords(voList);
        return voPage;
    }

    @Override
    public MediaAssetVO getAssetById(Long id) {
        MediaAsset asset = mediaAssetMapper.selectById(id);
        if (asset == null) {
            throw new BusinessException(ReturnCodeEnum.NOT_FOUND, "媒体资产不存在，ID: " + id);
        }
        MediaAssetVO vo = convertToVO(asset);
        vo.setDownloadUrl(getPresignedFileUrl(asset.getObjectName()));
        if (asset.getThumbnailName() != null) {
            vo.setThumbnailUrl(getPresignedThumbnailUrl(asset.getThumbnailName()));
        }
        return vo;
    }

    private MediaAssetVO convertToVO(MediaAsset asset) {
        MediaAssetVO vo = asset.copyProperties(MediaAssetVO.class);
        if (asset.getFileSize() != null) {
            vo.setFileSizeFormatted(MediaUtils.formatFileSize(asset.getFileSize()));
        }
        if (asset.getStatus() != null) {
            try {
                MediaStatusEnum statusEnum = MediaStatusEnum.fromCode(asset.getStatus());
                vo.setStatusDesc(statusEnum.getDesc());
            } catch (IllegalArgumentException e) {
                vo.setStatusDesc(asset.getStatus());
            }
        }
        return vo;
    }

    @Override
    public String getPresignedFileUrl(String objectName) {
        MediaAsset asset = mediaAssetMapper.selectOne(
                new LambdaQueryWrapper<MediaAsset>().eq(MediaAsset::getObjectName, objectName));
        if (asset == null) {
            throw new BusinessException(ReturnCodeEnum.NOT_FOUND, "文件不存在: " + objectName);
        }

        try {
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(asset.getBucketName())
                            .object(asset.getObjectName())
                            .expiry(1, TimeUnit.HOURS)
                            .build());
        } catch (Exception e) {
            log.error("获取文件预签名URL失败: {}", objectName, e);
            throw new BusinessException(ReturnCodeEnum.INTERNAL_SERVER_ERROR, "获取文件下载链接失败");
        }
    }

    @Override
    public String getPresignedThumbnailUrl(String thumbnailName) {
        if (thumbnailName == null || thumbnailName.isEmpty()) {
            return null;
        }
        try {
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(minioProperties.getBucketName())
                            .object(thumbnailName)
                            .expiry(1, TimeUnit.HOURS)
                            .build());
        } catch (Exception e) {
            log.error("获取缩略图预签名URL失败: {}", thumbnailName, e);
            return null;
        }
    }

    @Override
    public String getPresignedDownloadUrl(String objectName) {
        MediaAsset asset = mediaAssetMapper.selectOne(
                new LambdaQueryWrapper<MediaAsset>().eq(MediaAsset::getObjectName, objectName));
        if (asset == null) {
            throw new BusinessException(ReturnCodeEnum.NOT_FOUND, "文件不存在: " + objectName);
        }

        try {
            Map<String, String> extraQueryParams = new HashMap<>();
            String encodedFileName = java.net.URLEncoder.encode(asset.getFileName(), "UTF-8")
                    .replace("+", "%20");
            extraQueryParams.put("response-content-disposition",
                    "attachment; filename=\"" + encodedFileName + "\"; filename*=UTF-8''" + encodedFileName);

            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(asset.getBucketName())
                            .object(asset.getObjectName())
                            .expiry(1, TimeUnit.HOURS)
                            .extraQueryParams(extraQueryParams)
                            .build());
        } catch (Exception e) {
            log.error("获取文件强制下载URL失败: {}", objectName, e);
            throw new BusinessException(ReturnCodeEnum.INTERNAL_SERVER_ERROR, "获取文件下载链接失败");
        }
    }

    @Override
    public Map<String, Object> getPresignedUploadUrl(Long userId, String fileName, long fileSize, String contentType, String fileMd5, Long fileType) {
        log.info("开始生成普通上传预签名URL，用户ID: {}, 文件名: {}", userId, fileName);

        String storagePrefix = "";
        if (fileType != null) {
            FileUploadRules fileUploadRules = fileUploadRulesService.getById(fileType);
            if (fileUploadRules == null) {
                throw new BusinessException(ReturnCodeEnum.PARAM_ERROR, "文件类型规则不存在，文件类型ID: " + fileType);
            }

            long maxSizeBytes = fileUploadRules.getMaxSizeKb() * 1024L;
            if (fileSize > maxSizeBytes) {
                throw new FileUploadException(
                        "文件大小超过限制，最大允许: " + MediaUtils.formatFileSize(maxSizeBytes) +
                        ", 当前文件: " + MediaUtils.formatFileSize(fileSize),
                        ReturnCodeEnum.FILE_SIZE_ERROR
                );
            }

            if (fileUploadRules.getAllowedExtensions() != null &&
                !fileUploadRules.getAllowedExtensions().isEmpty() &&
                !fileUploadRules.getAllowedExtensions().contains(contentType)) {
                throw new FileUploadException(
                        "不支持的文件类型: " + contentType +
                        "，允许的类型: " + String.join(", ", fileUploadRules.getAllowedExtensions()),
                        ReturnCodeEnum.FILE_TYPE_ERROR
                );
            }

            storagePrefix = fileUploadRules.getStoragePrefix() != null ? fileUploadRules.getStoragePrefix() : "";
        }

        MediaAsset existingAsset = mediaAssetMapper.selectOne(
                new LambdaQueryWrapper<MediaAsset>()
                        .eq(MediaAsset::getFileMd5, fileMd5)
                        .eq(MediaAsset::getStatus, MediaStatusEnum.UPLOADED.getCode())
                        .last("LIMIT 1")
        );

        if (existingAsset != null) {
            log.info("文件秒传命中，MD5: {}。直接复用现有文件: {}", fileMd5, existingAsset.getObjectName());
            MediaAsset newAsset = new MediaAsset()
                    .setFileName(fileName)
                    .setObjectName(existingAsset.getObjectName())
                    .setBucketName(existingAsset.getBucketName())
                    .setFileSize(existingAsset.getFileSize())
                    .setFileType(existingAsset.getFileType())
                    .setFileMd5(fileMd5)
                    .setUploaderId(userId)
                    .setStatus(MediaStatusEnum.UPLOADED.getCode());
            mediaAssetMapper.insert(newAsset);

            Map<String, Object> result = new HashMap<>();
            result.put("instantUpload", true);
            result.put("objectName", newAsset.getObjectName());
            result.put("assetId", newAsset.getId());
            return result;
        }

        String fileExtension = MediaUtils.getFileExtension(fileName);
        if (fileExtension.isEmpty() && contentType != null && !contentType.isEmpty()) {
            String[] parts = contentType.split("/");
            if (parts.length == 2) {
                fileExtension = "." + parts[1];
            }
        }
        String objectName = storagePrefix + UUID.randomUUID().toString().replace("-", "") + fileExtension;

        MediaAsset asset = new MediaAsset()
                .setFileName(fileName)
                .setObjectName(objectName)
                .setBucketName(minioProperties.getBucketName())
                .setFileSize(fileSize)
                .setFileType(contentType)
                .setFileMd5(fileMd5)
                .setUploaderId(userId)
                .setStatus(MediaStatusEnum.UPLOADING.getCode());
        mediaAssetMapper.insert(asset);

        try {
            Map<String, String> headers = new HashMap<>();
            if (contentType != null && !contentType.isEmpty()) {
                headers.put("Content-Type", contentType);
            }

            String presignedUrl = minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.PUT)
                            .bucket(minioProperties.getBucketName())
                            .object(objectName)
                            .expiry(1, TimeUnit.HOURS)
                            .extraHeaders(headers)
                            .build());

            Map<String, Object> result = new HashMap<>();
            result.put("instantUpload", false);
            result.put("objectName", objectName);
            result.put("assetId", asset.getId());
            result.put("presignedUrl", presignedUrl);
            log.info("成功生成普通上传预签名URL，objectName: {}, assetId: {}", objectName, asset.getId());
            return result;
        } catch (Exception e) {
            log.error("生成普通上传预签名URL失败: {}", objectName, e);
            mediaAssetMapper.deleteById(asset.getId());
            throw new BusinessException(ReturnCodeEnum.INTERNAL_SERVER_ERROR, "生成上传链接失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long completeUpload(String objectName) {
        log.info("开始完成普通上传，objectName: {}", objectName);

        MediaAsset asset = mediaAssetMapper.selectOne(
                new LambdaQueryWrapper<MediaAsset>().eq(MediaAsset::getObjectName, objectName));
        if (asset == null) {
            throw new BusinessException(ReturnCodeEnum.NOT_FOUND, "文件不存在: " + objectName);
        }

        try {
            boolean objectExists = minioClient.statObject(
                    StatObjectArgs.builder()
                            .bucket(asset.getBucketName())
                            .object(asset.getObjectName())
                            .build()) != null;
            if (!objectExists) {
                throw new BusinessException(ReturnCodeEnum.NOT_FOUND, "文件在存储中不存在: " + objectName);
            }
        } catch (Exception e) {
            log.warn("检查文件是否存在时出错: {}", objectName, e);
            throw new BusinessException(ReturnCodeEnum.NOT_FOUND, "文件在存储中不存在: " + objectName);
        }

        MediaAsset assetUpdate = new MediaAsset()
                .setStatus(MediaStatusEnum.UPLOADED.getCode());
        mediaAssetMapper.update(assetUpdate,
                new LambdaQueryWrapper<MediaAsset>()
                        .eq(MediaAsset::getObjectName, objectName));

        MediaAsset completedAsset = mediaAssetMapper.selectOne(
                new LambdaQueryWrapper<MediaAsset>().eq(MediaAsset::getObjectName, objectName));
        mediaAssetService.processMediaAsset(completedAsset);

        log.info("成功完成普通上传: objectName: {}, assetId: {}", objectName, completedAsset.getId());
        return completedAsset.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteFile(Long userId, String objectName) {
        log.info("开始删除文件，用户ID: {}, objectName: {}", userId, objectName);

        MediaAsset asset = mediaAssetMapper.selectOne(
                new LambdaQueryWrapper<MediaAsset>()
                        .eq(MediaAsset::getObjectName, objectName)
                        .eq(MediaAsset::getUploaderId, userId));
        if (asset == null) {
            throw new BusinessException(ReturnCodeEnum.FORBIDDEN, "文件不存在或您没有权限删除此文件");
        }

        mediaAssetMapper.deleteById(asset.getId());
        log.info("成功删除媒体资产数据库记录: id={}, objectName={}", asset.getId(), objectName);

        Long remainingReferences = mediaAssetMapper.selectCount(
                new LambdaQueryWrapper<MediaAsset>().eq(MediaAsset::getObjectName, objectName));

        if (remainingReferences == 0) {
            log.info("对象 {} 的最后一个数据库引用已被删除，准备从MinIO中删除物理文件。", objectName);
            try {
                minioClient.removeObject(
                        RemoveObjectArgs.builder()
                                .bucket(asset.getBucketName())
                                .object(asset.getObjectName())
                                .build());
                log.info("成功从MinIO删除文件: {}", objectName);

                if (asset.getThumbnailName() != null) {
                    minioClient.removeObject(
                            RemoveObjectArgs.builder()
                                    .bucket(asset.getBucketName())
                                    .object(asset.getThumbnailName())
                                    .build());
                    log.info("成功从MinIO删除缩略图: {}", asset.getThumbnailName());
                }
            } catch (Exception e) {
                log.error("从MinIO删除文件 {} 失败。", objectName, e);
                throw new BusinessException(ReturnCodeEnum.INTERNAL_SERVER_ERROR, "删除文件失败: " + e.getMessage());
            }
        } else {
            log.info("对象 {} 仍有 {} 个数据库引用，本次不删除MinIO物理文件。", objectName, remainingReferences);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> uploadFileWithRule(Long userId, MultipartFile file, Long ruleId) {
        log.info("开始使用上传规则直接上传文件，用户ID: {}, 规则ID: {}, 文件名: {}", userId, ruleId, file.getOriginalFilename());

        FileUploadRules fileUploadRules = fileUploadRulesService.getById(ruleId);
        if (fileUploadRules == null) {
            throw new BusinessException(ReturnCodeEnum.PARAM_ERROR, "上传规则不存在，规则ID: " + ruleId);
        }

        if (!Boolean.TRUE.equals(fileUploadRules.getIsActive())) {
            throw new BusinessException(ReturnCodeEnum.PARAM_ERROR, "上传规则未启用，规则ID: " + ruleId);
        }

        String fileName = file.getOriginalFilename();
        String contentType = file.getContentType();
        long fileSize = file.getSize();

        long maxSizeBytes = fileUploadRules.getMaxSizeKb() * 1024L;
        if (fileSize > maxSizeBytes) {
            throw new FileUploadException(
                    "文件大小超过限制，最大允许: " + MediaUtils.formatFileSize(maxSizeBytes) +
                    ", 当前文件: " + MediaUtils.formatFileSize(fileSize),
                    ReturnCodeEnum.FILE_SIZE_ERROR
            );
        }

        if (fileUploadRules.getAllowedExtensions() != null &&
            !fileUploadRules.getAllowedExtensions().isEmpty() &&
            contentType != null &&
            !fileUploadRules.getAllowedExtensions().contains(contentType)) {
            throw new FileUploadException(
                    "不支持的文件类型: " + contentType +
                    "，允许的类型: " + String.join(", ", fileUploadRules.getAllowedExtensions()),
                    ReturnCodeEnum.FILE_TYPE_ERROR
            );
        }

        String fileExtension = MediaUtils.getFileExtension(fileName);
        if (fileExtension.isEmpty() && contentType != null && !contentType.isEmpty()) {
            String[] parts = contentType.split("/");
            if (parts.length == 2) {
                fileExtension = "." + parts[1];
            }
        }

        String storagePrefix = fileUploadRules.getStoragePrefix() != null ? fileUploadRules.getStoragePrefix() : "";
        String objectName = storagePrefix + UUID.randomUUID().toString().replace("-", "") + fileExtension;

        String fileMd5;
        try {
            fileMd5 = DigestUtils.md5DigestAsHex(file.getInputStream());
        } catch (Exception e) {
            log.error("计算文件MD5失败", e);
            throw new FileUploadException("计算文件MD5失败", ReturnCodeEnum.FILE_UPLOAD_ERROR);
        }

        MediaAsset existingAsset = mediaAssetMapper.selectOne(
                new LambdaQueryWrapper<MediaAsset>()
                        .eq(MediaAsset::getFileMd5, fileMd5)
                        .eq(MediaAsset::getStatus, MediaStatusEnum.UPLOADED.getCode())
                        .last("LIMIT 1")
        );

        if (existingAsset != null) {
            log.info("文件秒传命中，MD5: {}。直接复用现有文件: {}", fileMd5, existingAsset.getObjectName());
            MediaAsset newAsset = new MediaAsset()
                    .setFileName(fileName)
                    .setObjectName(existingAsset.getObjectName())
                    .setBucketName(existingAsset.getBucketName())
                    .setFileSize(existingAsset.getFileSize())
                    .setFileType(existingAsset.getFileType())
                    .setFileMd5(fileMd5)
                    .setUploaderId(userId)
                    .setStatus(MediaStatusEnum.UPLOADED.getCode());
            mediaAssetMapper.insert(newAsset);

            Map<String, Object> result = new HashMap<>();
            result.put("instantUpload", true);
            result.put("objectName", newAsset.getObjectName());
            result.put("assetId", newAsset.getId());
            result.put("fileUrl", getPresignedFileUrl(newAsset.getObjectName()));
            return result;
        }

        try {
            PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                    .bucket(minioProperties.getBucketName())
                    .object(objectName)
                    .stream(file.getInputStream(), fileSize, -1)
                    .contentType(contentType)
                    .build();

            minioClient.putObject(putObjectArgs);
            log.info("文件上传成功: {}", objectName);

        } catch (Exception e) {
            log.error("上传文件到MinIO失败: {}", objectName, e);
            throw new FileUploadException("上传文件失败: " + e.getMessage(), ReturnCodeEnum.FILE_UPLOAD_ERROR);
        }

        MediaAsset asset = new MediaAsset()
                .setFileName(fileName)
                .setObjectName(objectName)
                .setBucketName(minioProperties.getBucketName())
                .setFileSize(fileSize)
                .setFileType(contentType)
                .setFileMd5(fileMd5)
                .setUploaderId(userId)
                .setStatus(MediaStatusEnum.UPLOADED.getCode());
        mediaAssetMapper.insert(asset);

        CompletableFuture.runAsync(() -> {
            try {
                mediaAssetService.processMediaAsset(asset);
            } catch (Exception e) {
                log.error("异步处理媒体资产失败，assetId: {}", asset.getId(), e);
            }
        });

        Map<String, Object> result = new HashMap<>();
        result.put("instantUpload", false);
        result.put("objectName", objectName);
        result.put("assetId", asset.getId());
        result.put("fileUrl", getPresignedFileUrl(objectName));

        log.info("成功使用上传规则完成文件上传: objectName: {}, assetId: {}", objectName, asset.getId());
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> uploadFileWithRuleName(Long userId, MultipartFile file, String ruleName) {
        log.info("开始使用枚举规则直接上传文件，用户ID: {}, 规则名称: {}, 文件名: {}", userId, ruleName, file.getOriginalFilename());

        // 根据规则名称查找枚举
        MediaUploadRuleEnum ruleEnum = MediaUploadRuleEnum.fromRuleName(ruleName);
        if (ruleEnum == null) {
            throw new BusinessException(ReturnCodeEnum.PARAM_ERROR, "上传规则不存在，规则名称: " + ruleName);
        }

        // 检查规则是否启用
        if (!Boolean.TRUE.equals(ruleEnum.getIsActive())) {
            throw new BusinessException(ReturnCodeEnum.PARAM_ERROR, "上传规则未启用，规则名称: " + ruleName);
        }

        String fileName = file.getOriginalFilename();
        String contentType = file.getContentType();
        long fileSize = file.getSize();

        // 校验文件大小
        long fileSizeKb = fileSize / 1024;
        if (!ruleEnum.isFileSizeAllowed(fileSizeKb)) {
            throw new FileUploadException(
                    "文件大小超过限制，最大允许: " + MediaUtils.formatFileSize(ruleEnum.getMaxSizeKb() * 1024L) +
                    ", 当前文件: " + MediaUtils.formatFileSize(fileSize),
                    ReturnCodeEnum.FILE_SIZE_ERROR
            );
        }

        // 校验文件扩展名
        String fileExtension = MediaUtils.getFileExtension(fileName);
        if (!ruleEnum.isExtensionAllowed(fileExtension)) {
            throw new FileUploadException(
                    "不支持的文件类型: " + contentType +
                    "，允许的类型: " + String.join(", ", ruleEnum.getAllowedExtensions()),
                    ReturnCodeEnum.FILE_TYPE_ERROR
            );
        }

        // 如果扩展名为空但contentType不为空，尝试从contentType推断扩展名
        if (fileExtension.isEmpty() && contentType != null && !contentType.isEmpty()) {
            String[] parts = contentType.split("/");
            if (parts.length == 2) {
                fileExtension = "." + parts[1];
            }
        }

        // 生成对象名
        String objectName = ruleEnum.getStoragePrefix() + UUID.randomUUID().toString().replace("-", "") + fileExtension;

        // 计算文件MD5
        String fileMd5;
        try {
            fileMd5 = DigestUtils.md5DigestAsHex(file.getInputStream());
        } catch (Exception e) {
            log.error("计算文件MD5失败", e);
            throw new FileUploadException("计算文件MD5失败", ReturnCodeEnum.FILE_UPLOAD_ERROR);
        }

        // 检查是否可以秒传
        MediaAsset existingAsset = mediaAssetMapper.selectOne(
                new LambdaQueryWrapper<MediaAsset>()
                        .eq(MediaAsset::getFileMd5, fileMd5)
                        .eq(MediaAsset::getStatus, MediaStatusEnum.UPLOADED.getCode())
                        .last("LIMIT 1")
        );

        if (existingAsset != null) {
            log.info("文件秒传命中，MD5: {}。直接复用现有文件: {}", fileMd5, existingAsset.getObjectName());
            MediaAsset newAsset = new MediaAsset()
                    .setFileName(fileName)
                    .setObjectName(existingAsset.getObjectName())
                    .setBucketName(existingAsset.getBucketName())
                    .setFileSize(existingAsset.getFileSize())
                    .setFileType(existingAsset.getFileType())
                    .setFileMd5(fileMd5)
                    .setUploaderId(userId)
                    .setStatus(MediaStatusEnum.UPLOADED.getCode());
            mediaAssetMapper.insert(newAsset);

            Map<String, Object> result = new HashMap<>();
            result.put("instantUpload", true);
            result.put("objectName", newAsset.getObjectName());
            result.put("assetId", newAsset.getId());
            result.put("fileUrl", getPresignedFileUrl(newAsset.getObjectName()));
            return result;
        }

        // 上传文件到MinIO
        try {
            PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                    .bucket(minioProperties.getBucketName())
                    .object(objectName)
                    .stream(file.getInputStream(), fileSize, -1)
                    .contentType(contentType)
                    .build();

            minioClient.putObject(putObjectArgs);
            log.info("文件上传成功: {}", objectName);

        } catch (Exception e) {
            log.error("上传文件到MinIO失败: {}", objectName, e);
            throw new FileUploadException("上传文件失败: " + e.getMessage(), ReturnCodeEnum.FILE_UPLOAD_ERROR);
        }

        // 创建媒体资产记录
        MediaAsset asset = new MediaAsset()
                .setFileName(fileName)
                .setObjectName(objectName)
                .setBucketName(minioProperties.getBucketName())
                .setFileSize(fileSize)
                .setFileType(contentType)
                .setFileMd5(fileMd5)
                .setUploaderId(userId)
                .setStatus(MediaStatusEnum.UPLOADED.getCode());
        mediaAssetMapper.insert(asset);

        // 异步触发媒体处理
        CompletableFuture.runAsync(() -> {
            try {
                mediaAssetService.processMediaAsset(asset);
            } catch (Exception e) {
                log.error("异步处理媒体资产失败，assetId: {}", asset.getId(), e);
            }
        });

        Map<String, Object> result = new HashMap<>();
        result.put("instantUpload", false);
        result.put("objectName", objectName);
        result.put("assetId", asset.getId());
        result.put("fileUrl", getPresignedFileUrl(objectName));

        log.info("成功使用枚举规则完成文件上传: objectName: {}, assetId: {}", objectName, asset.getId());
        return result;
    }
}
