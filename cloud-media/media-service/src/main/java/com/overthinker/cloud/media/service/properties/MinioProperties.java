package com.overthinker.cloud.media.service.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;



@ConfigurationProperties(prefix = "minio")
@Data
public class MinioProperties {

    private String endpoint;
    private String accessKey;
    private String secretKey;
    private BucketName bucketName;

    @Data
    public static class BucketName {
        private String file;
        private String image;
        private String video;

    }

}
