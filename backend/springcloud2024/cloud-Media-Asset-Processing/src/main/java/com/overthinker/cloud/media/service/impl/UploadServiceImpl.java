package com.overthinker.cloud.media.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.overthinker.cloud.media.config.MinioProperties;
import com.overthinker.cloud.media.constants.MediaStatus;
import com.overthinker.cloud.media.entity.MediaAsset;
import com.overthinker.cloud.media.mapper.MediaAssetMapper;
import com.overthinker.cloud.media.service.UploadService;

import com.overthinker.cloud.redis.utils.MyRedisCache;
import io.minio.*;
import io.minio.errors.*;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class UploadServiceImpl implements UploadService {

    private final MinioClient minioClient;
    private final MinioAsyncClient minioAsyncClient;
    private final MinioProperties minioProperties;
    private final MediaAssetMapper mediaAssetMapper;
    private final MyRedisCache redisCache;

    private static final String UPLOAD_ID_CACHE_PREFIX = "media:uploadId:";

    @Override
    @Transactional
    public Map<String, Object> handleFirstPartAndGenerateUrls(String filename, int totalParts) throws Exception {
        String fileExtension = filename.substring(filename.lastIndexOf("."));
        String objectName = UUID.randomUUID().toString().replace("-", "") + fileExtension;

        MediaAsset asset = new MediaAsset()
                .setFileName(filename)
                .setObjectName(objectName)
                .setBucketName(minioProperties.getBucketName())
                .setStatus(MediaStatus.UPLOADING)
                .setCreatedAt(LocalDateTime.now())
                .setUpdatedAt(LocalDateTime.now());
        mediaAssetMapper.insert(asset);

        CreateMultipartUploadResponse response = minioAsyncClient.createMultipartUploadAsync(
                minioProperties.getBucketName(), null, objectName, null, null).join();
        String uploadId = response.result().uploadId();
        log.info("生成分片上传任务ID: {} for object: {}", uploadId, objectName);

        redisCache.setCacheObject(UPLOAD_ID_CACHE_PREFIX + uploadId, objectName, 2, TimeUnit.HOURS);

        Map<Integer, String> presignedUrls = new LinkedHashMap<>();
        for (int partNumber = 1; partNumber <= totalParts; partNumber++) {
            String url = minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.PUT)
                            .bucket(minioProperties.getBucketName())
                            .object(objectName)
                            .expiry(2, TimeUnit.HOURS)
                            .extraQueryParam("uploadId", uploadId)
                            .extraQueryParam("partNumber", String.valueOf(partNumber))
                            .build());
            presignedUrls.put(partNumber, url);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("uploadId", uploadId);
        result.put("objectName", objectName);
        result.put("presignedUrls", presignedUrls);
        return result;
    }

    @Override
    @Transactional
    public void completeMultipartUpload(String uploadId) {
        String objectName = redisCache.getCacheObject(UPLOAD_ID_CACHE_PREFIX + uploadId);
        if (objectName == null) {
            throw new RuntimeException("上传会话已过期或uploadId无效: " + uploadId);
        }

        try {
            minioClient.completeMultipartUpload(minioProperties.getBucketName(), null, objectName, uploadId, null, null, null);

            MediaAsset assetUpdate = new MediaAsset()
                    .setStatus(MediaStatus.UPLOADED)
                    .setUpdatedAt(LocalDateTime.now());
            mediaAssetMapper.update(assetUpdate, new QueryWrapper<MediaAsset>().eq("object_name", objectName));

            log.info("成功完成文件分片上传: objectName: {}, uploadId: {}", objectName, uploadId);

        } catch (Exception e) {
            log.error("完成文件分片上传失败: objectName: {}, uploadId: {}", objectName, uploadId, e);
            MediaAsset assetUpdate = new MediaAsset().setStatus(MediaStatus.FAILED);
            mediaAssetMapper.update(assetUpdate, new QueryWrapper<MediaAsset>().eq("object_name", objectName));
            throw new RuntimeException("完成分片上传失败", e);
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
    public void deleteFile(String objectName) {
        MediaAsset asset = mediaAssetMapper.selectOne(new QueryWrapper<MediaAsset>().eq("object_name", objectName));
        if (asset == null) {
            log.warn("尝试删除一个不存在的媒体资产记录: {}", objectName);
            return;
        }

        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(asset.getBucketName())
                            .object(asset.getObjectName())
                            .build());
            log.info("成功从MinIO删除文件: {}", objectName);
        } catch (Exception e) {
            log.error("从MinIO删除文件失败: {}", objectName, e);
            throw new RuntimeException("从MinIO删除文件失败", e);
        }

        mediaAssetMapper.deleteById(asset.getId());
        log.info("成功删除媒体资产数据库记录: {}", objectName);
    }
}