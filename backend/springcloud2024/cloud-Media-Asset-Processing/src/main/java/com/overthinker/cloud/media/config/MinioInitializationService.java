package com.overthinker.cloud.media.config;



import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Log4j2
public class MinioInitializationService {

    private final MinioClient minioClient;
    private final MinioProperties minioProperties;

    @PostConstruct
    public void ensureBucketExists() {
        try {
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder()
                    .bucket(minioProperties.getBucketName())
                    .build());
            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder()
                        .bucket(minioProperties.getBucketName())
                        .build());
                log.warn("Bucket '{}' created successfully.", minioProperties.getBucketName());
            }

            log.info("Bucket '{}' already exists.", minioProperties.getBucketName());
        } catch (Exception e) {
            throw new RuntimeException("Failed to create bucket: " + minioProperties.getBucketName(), e);
        }
    }
}
