package com.overthinker.cloud.media.service.config;

import com.overthinker.cloud.media.service.properties.MinioProperties;
import io.minio.MinioClient;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioConfig {

    @Resource
    MinioProperties minioProperties;



    @Bean
    public MinioClient minioClient() {

        MinioClient minioClient;
        minioClient =
                MinioClient.builder()
                        .endpoint(minioProperties.getEndpoint())
                        .credentials(minioProperties.getAccessKey(), minioProperties.getSecretKey())
                        .build();
        return minioClient;
    }
}

