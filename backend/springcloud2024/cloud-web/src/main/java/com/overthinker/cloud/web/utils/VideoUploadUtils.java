package com.overthinker.cloud.web.utils;

import cn.hutool.core.io.file.FileNameUtil;
import com.overthinker.cloud.web.entity.enums.VideoUploadEnum;
import com.overthinker.cloud.web.exception.FileUploadException;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Component
public class VideoUploadUtils {
    // 封面支持格式

    private static final int MAX_COVER_SIZE_MB = 5;

    @Resource
    private MinioClient client;

    @Value("${minio.bucketName}")
    private String bucketName;

    @Value("${minio.endpoint}")
    private String endpoint;


    /**
     * 统一上传到MinIO
     */
    public String uploadToMinio(String objectName, InputStream stream,
                                long size, String contentType) throws Exception {
        try {
            client.putObject(PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .stream(stream, size, -1)
                    .contentType(contentType)
                    .build());
//            return endpoint + "/" + bucketName + "/" + objectName;
            return objectName;
        } catch (Exception e) {
            log.error("MinIO上传失败: {}", objectName, e);
            throw e;
        }
    }

    /**
     * 生成用户基础路径如果为公共视频则不需要用户id
     */
    public String buildUserBasePath(VideoUploadEnum config) {
        if (config == VideoUploadEnum.VIDEO_PUBLIC) {
            return String.format("%s/%s/",
                    config.getDir(),
                    UUID.randomUUID());
        }
        return String.format("%s/user-%d/%s/",
                config.getDir(),
                SecurityUtils.getUserId(),
                UUID.randomUUID());
    }


    /**
     * 视频校验
     */
    public void validateVideo(VideoUploadEnum config,
                              MultipartFile video
                              ) throws FileUploadException {
        // 视频校验
        if (video.isEmpty()) throw new FileUploadException("视频文件不能为空");
        if (video.getSize() > config.getVideoLimitSize() * 1024 * 1024) {
            throw new FileUploadException("视频大小超过限制");
        }
        if (isValidFormat(video, config.getVideoFormat())) {
            throw new FileUploadException("视频格式不支持");
        }

    }

    /**
     * 增强格式校验
     */
    private boolean isValidFormat(MultipartFile file, Set<String> allowedExtensions) {
        String fileName = FileNameUtil.extName((File) file);
        return allowedExtensions.stream()
                .noneMatch(fileName::endsWith);
    }

    /**
     * 清理部分上传的文件
     */
    public void cleanupPartialUploads(String... objectNames) {
        Arrays.stream(objectNames).forEach(name -> {
            try {
                client.removeObject(RemoveObjectArgs.builder()
                        .bucket(bucketName)
                        .object(name)
                        .build());
            } catch (Exception e) {
                log.warn("文件清理失败: {}", name, e);
            }
        });
    }

    /**
     * 将视频文件大小转换成合适的单位
     */
    public String convertVideoSize(long size) {
        if (size < 0) {
            throw new IllegalArgumentException("文件大小不能为负数: " + size);
        }

        String[] units = {"B", "KB", "MB", "GB", "TB", "PB", "EB"};
        if (size == 0) {
            return "0B";
        }

        int unitIndex = 0;
        double formattedSize = size;

        // 找到适合的单位
        while (formattedSize >= 1024 && unitIndex < units.length - 1) {
            formattedSize /= 1024;
            unitIndex++;
        }

        // 格式化数值，移除不必要的零
        String sizeString;
        if (formattedSize == (long) formattedSize) {
            sizeString = String.format("%d", (long) formattedSize); // 整数部分无小数
        } else {
            sizeString = String.format("%.1f", formattedSize).replace(".0", ""); // 保留一位小数并去除.0
        }

        return sizeString + units[unitIndex];
    }

    /**
     * 给地址加上域名
     */
    public String addEndpoint(String objectName) {
        return endpoint + "/" + bucketName + "/" + objectName;
    }
}