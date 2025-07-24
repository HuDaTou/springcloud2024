package com.overthinker.cloud.media.config;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;

/**
 * 在应用程序启动时初始化MinIO存储桶。
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MinioInitializationService implements ApplicationRunner {

    private final MinioClient minioClient;
    private final MinioProperties minioProperties;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        String bucketName = minioProperties.getBucketName();
        try {
            log.info("检查MinIO存储桶'{}'是否存在...", bucketName);
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (found) {
                log.info("MinIO存储桶'{}'已存在。", bucketName);
            } else {
                log.warn("未找到MinIO存储桶'{}'。正在尝试创建...", bucketName);
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
                log.info("成功创建MinIO存储桶'{}'。", bucketName);
            }
        } catch (Exception e) {
            log.error("检查或创建MinIO存储桶'{}'失败。请检查您的MinIO服务器连接和配置。", bucketName, e);
            throw new RuntimeException("MinIO存储桶初始化失败", e);
        }
    }
}
