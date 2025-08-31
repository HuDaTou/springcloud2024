package com.overthinker.cloud.web.config;


import com.overthinker.cloud.web.exception.MinioConnectionException;
import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author overH
 * <p>
 * 创建时间：2023/12/26 16:01
 */
@Configuration
public class MinioConfig {

    @Value("${minio.endpoint}")
    private String endpoint;

    @Value("${minio.accessKey}")
    private String accessKey;

    @Value("${minio.secretKey}")
    private String secretKey;

    @Bean
    public MinioClient minioClient() {
        try {
            return MinioClient.builder()
                    .endpoint(endpoint)
                    .credentials(accessKey, secretKey)
                    .build();
        } catch (Exception e) {
            throw new MinioConnectionException("Unexpected error while connecting to MinIO: " + e.getMessage(), e);
        }
    }
}