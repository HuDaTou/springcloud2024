package com.overthinker.cloud.media.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.overthinker.cloud.media.config.MinioProperties;
import com.overthinker.cloud.media.constants.MediaStatus;
import com.overthinker.cloud.media.entity.DTO.InitiateMultipartUploadDTO;
import com.overthinker.cloud.media.entity.PO.FileUploadRules;
import com.overthinker.cloud.media.entity.PO.MediaAsset;
import com.overthinker.cloud.media.mapper.MediaAssetMapper;
import com.overthinker.cloud.media.service.FileUploadRulesService;
import com.overthinker.cloud.media.service.MediaAssetService;
import com.overthinker.cloud.media.service.UploadService;

import com.overthinker.cloud.media.config.MediaProperties;
import com.overthinker.cloud.redis.utils.MyRedisCache;
import io.minio.messages.Part;
import io.minio.ListPartsResponse;

import io.minio.*;


import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UploadServiceImpl implements UploadService {

    private final MinioClient minioClient;
    private final MinioAsyncClient minioAsyncClient;
    private final MinioProperties minioProperties;
    private final MediaAssetMapper mediaAssetMapper;
    private final MyRedisCache redisCache;
    private final MediaAssetService MediaAssetService; // 注入媒体处理服务

    private static final String UPLOAD_ID_CACHE_PREFIX = "media:uploadId:";
    private final FileUploadRulesService fileUploadRulesService;

    @Override
    @Transactional
    public Map<String, Object> handleFirstPartAndGenerateUrls(Long userId, InitiateMultipartUploadDTO DTO) throws Exception {






        // 1. 配置校验
        FileUploadRules fileUploadRules = fileUploadRulesService.getById(DTO.fileType());



        // 使用注入的 mediaProperties 进行校验
        if (DTO.fileSize() > fileUploadRules.getMaxSizeKb()) {
            throw new IllegalArgumentException("文件大小超过 " + (fileUploadRules.getMaxSizeKb() / 1024 / 1024) + "MB 限制");
        }
        if (!fileUploadRules.getAllowedExtensions().contains(DTO.contentType())) {
            throw new IllegalArgumentException("不支持的文件类型: " + DTO.contentType());
        }



        // 2. 秒传功能实现
        // 根据MD5查找是否已存在相同文件
        MediaAsset existingAsset = mediaAssetMapper.selectOne(new QueryWrapper<MediaAsset>()
                .eq("file_md5", DTO.fileMd5())
                .eq("status", MediaStatus.UPLOADED)
                .last("LIMIT 1"));

        if (existingAsset != null) {
            log.info("文件秒传命中，MD5: {}。直接复用现有文件: {}", DTO.fileMd5(), existingAsset.getObjectName());
            // 创建一条新的媒体记录，但指向同一个MinIO对象
            MediaAsset newAsset = new MediaAsset()
                    .setFileName(DTO.filename())
                    .setObjectName(existingAsset.getObjectName()) // 指向已存在的文件
                    .setBucketName(existingAsset.getBucketName())
                    .setFileSize(existingAsset.getFileSize())
                    .setFileType(existingAsset.getFileType())
                    .setFileMd5(DTO.fileMd5())
                    .setUploaderId(userId)
                    .setStatus(MediaStatus.UPLOADED); // 直接设为已上传
            mediaAssetMapper.insert(newAsset);
            Long id = newAsset.getId();


            // 返回一个特殊标记，表示秒传成功，前端无需再上传
            Map<String, Object> result = new HashMap<>();
            result.put("instantUpload", true);
            result.put("objectName", newAsset.getObjectName());
            return result;
        }

        // 3. 正常分片上传流程
        String fileExtension = DTO.filename().substring(DTO.filename().lastIndexOf("."));
        String objectName = UUID.randomUUID().toString().replace("-", "") + fileExtension;

        MediaAsset asset = new MediaAsset()
                .setFileName(DTO.filename())
                .setObjectName(objectName)
                .setBucketName(minioProperties.getBucketName())
                .setFileSize(DTO.fileSize())
                .setFileType(DTO.contentType())
                .setFileMd5(DTO.fileMd5())
                .setUploaderId(userId)
                .setStatus(MediaStatus.UPLOADING);
        mediaAssetMapper.insert(asset);

//        初始化分片上传任务
        CreateMultipartUploadResponse response = minioAsyncClient.createMultipartUploadAsync(
                minioProperties.getBucketName(),
                null,
                objectName,
                null,
                null
        ).join();

//        minioAsyncClient.composeObject(
//                ComposeObjectArgs.builder()
//                        .bucket(minioProperties.getBucketName())
//                        .object(objectName)
//                        .uploadId(uploadId)
//                        .parts(new Part(1, objectName))
//                        .build()
//        ).join();
        String uploadId = response.result().uploadId();
        log.info("为文件 {} 生成分片上传任务ID: {} for object: {}", DTO.filename(), uploadId, objectName);

        // 将 objectName 和 MD5 存入缓存，以便完成时校验
        Map<String, String> cacheValue = new HashMap<>();
        cacheValue.put("objectName", objectName);
        cacheValue.put("fileMd5", DTO.fileMd5());
        redisCache.setCacheObject(UPLOAD_ID_CACHE_PREFIX + uploadId, cacheValue, 2, TimeUnit.HOURS);

        Map<Integer, String> presignedUrls = new LinkedHashMap<>();
        for (int partNumber = 1; partNumber <= DTO.totalParts(); partNumber++) {
            String url = null;
            Map<String, String> queryParams = new HashMap<>();
            queryParams.put("uploadId", uploadId);
            queryParams.put("partNumber", String.valueOf(partNumber));
            try {
                url = minioAsyncClient.getPresignedObjectUrl(
                        GetPresignedObjectUrlArgs.builder()
                                .method(Method.PUT)
                                .bucket(minioProperties.getBucketName())
                                .object(objectName)
                                .expiry(2, TimeUnit.HOURS)
                                .extraQueryParams(queryParams)
                                .build());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            presignedUrls.put(partNumber, url);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("instantUpload", false);
        result.put("uploadId", uploadId);
        result.put("objectName", objectName);
        result.put("presignedUrls", presignedUrls);
        return result;
    }

    @Override
    @Transactional
    public void completeMultipartUpload(String uploadId) {
        Map<String, String> cacheValue = redisCache.getCacheObject(UPLOAD_ID_CACHE_PREFIX + uploadId);
        if (cacheValue == null || cacheValue.get("objectName") == null) {
            throw new RuntimeException("上传会话已过期或uploadId无效: " + uploadId);
        }
        String objectName = cacheValue.get("objectName");
        String originalMd5 = cacheValue.get("fileMd5");

        try {
            // 1. 查询已上传的分片
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

            // ✅ 调用 .join() 获取结果
            ListPartsResponse listPartsResponse = future.join();

            // 2. 提取 partNumber 和 ETag
            List<Part> completeParts = listPartsResponse.result().partList().stream()
                    .map(part -> new Part(part.partNumber(), part.etag()))
                    .collect(Collectors.toList());


            // 3. 使用 completeMultipartUploadAsync（不是 completeMultipartUpload）
            CompletableFuture<ObjectWriteResponse> completeFuture = minioAsyncClient.completeMultipartUploadAsync(
                    minioProperties.getBucketName(),  // bucketName
                    null,                            // region - 一般为 null
                    objectName,                      // objectName
                    uploadId,                        // uploadId
                    completeParts.toArray(new Part[0]), // parts 数组
                    null,                            // extraHeaders
                    null                             // extraQueryParams
            );
            // ✅ 关键：调用 .join() 获取结果
            ObjectWriteResponse completeResponse = completeFuture.join();


            System.out.println("✅ 分片合并成功，文件已生成：" + completeResponse.object());
            // 2. 完整性校验
            // 从MinIO获取刚上传完成的文件并计算其MD5
            String serverSideMd5 = calculateMinioObjectMd5(objectName);

            if (!serverSideMd5.equalsIgnoreCase(originalMd5)) {
                log.error("文件完整性校验失败！客户端MD5: {}, 服务端MD5: {}. 文件: {}", originalMd5, serverSideMd5, objectName);
                // 校验失败，删除MinIO中刚上传的文件，并将数据库状态更新为FAILED
                minioClient.removeObject(RemoveObjectArgs.builder().bucket(minioProperties.getBucketName()).object(objectName).build());
                MediaAsset assetUpdate = new MediaAsset().setStatus(MediaStatus.FAILED);
                mediaAssetMapper.update(assetUpdate, new QueryWrapper<MediaAsset>().eq("object_name", objectName));
                throw new RuntimeException("文件完整性校验失败");
            }

            // 3. 更新数据库状态
            MediaAsset assetUpdate = new MediaAsset()
                    .setStatus(MediaStatus.UPLOADED);
            mediaAssetMapper.update(assetUpdate, new QueryWrapper<MediaAsset>().eq("object_name", objectName));

            log.info("成功完成文件分片上传并校验通过: objectName: {}, uploadId: {}", objectName, uploadId);

            // 4. 触发异步媒体处理
            // 获取完整的资产信息以传递给异步服务
            MediaAsset completedAsset = mediaAssetMapper.selectOne(new QueryWrapper<MediaAsset>().eq("object_name", objectName));
            MediaAssetService.processMediaAsset(completedAsset);

        } catch (Exception e) {
            log.error("完成文件分片上传失败: objectName: {}, uploadId: {}", objectName, uploadId, e);
            MediaAsset assetUpdate = new MediaAsset().setStatus(MediaStatus.FAILED);
            mediaAssetMapper.update(assetUpdate, new QueryWrapper<MediaAsset>().eq("object_name", objectName));
            throw new RuntimeException("完成分片上传失败", e);
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
    public Page<MediaAsset> listFiles(int pageNum, int pageSize) {
        return mediaAssetMapper.selectPage(new Page<>(pageNum, pageSize), new QueryWrapper<MediaAsset>().orderByDesc("created_at"));
    }

    @Override
    public String getPresignedFileUrl(String objectName) {
        MediaAsset asset = mediaAssetMapper.selectOne(new QueryWrapper<MediaAsset>().eq("object_name", objectName));
        if (asset == null) {
            throw new RuntimeException("文件不存在: " + objectName);
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
            throw new RuntimeException("获取文件预签名URL失败", e);
        }
    }

    @Override
    @Transactional
    public void deleteFile(Long userId, String objectName) {
        MediaAsset asset = mediaAssetMapper.selectOne(new QueryWrapper<MediaAsset>().eq("object_name", objectName).eq("uploader_id", userId));
        if (asset == null) {
            log.warn("尝试删除一个不存在或不属于用户 {} 的媒体资产记录: {}", userId, objectName);
            throw new SecurityException("文件不存在或您没有权限删除此文件");
        }

        // 先删除数据库记录
        mediaAssetMapper.deleteById(asset.getId());
        log.info("成功删除媒体资产数据库记录: id={}, objectName={}", asset.getId(), objectName);

        // 检查是否还有其他记录引用此MinIO对象
        Long remainingReferences = mediaAssetMapper.selectCount(new QueryWrapper<MediaAsset>().eq("object_name", objectName));

        if (remainingReferences == 0) {
            // 这是最后一个引用，可以安全地删除物理文件
            log.info("对象 {} 的最后一个数据库引用已被删除，准备从MinIO中删除物理文件。", objectName);
            try {
                minioClient.removeObject(
                        RemoveObjectArgs.builder()
                                .bucket(asset.getBucketName())
                                .object(asset.getObjectName())
                                .build());
                log.info("成功从MinIO删除文件: {}", objectName);
            } catch (Exception e) {
                log.error("从MinIO删除文件 {} 失败。数据库记录已回滚。", objectName, e);
                // 注意：由于@Transactional注解，此处的RuntimeException会触发整个方法的回滚
                throw new RuntimeException("从MinIO删除文件失败", e);
            }
        } else {
            log.info("对象 {} 仍有 {} 个数据库引用，本次不删除MinIO物理文件。", objectName, remainingReferences);
        }
    }
}