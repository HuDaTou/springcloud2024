package com.overthinker.cloud.media.service;

import io.minio.MinioClient;
import io.minio.RemoveBucketArgs;
import io.minio.RemoveObjectArgs;
import io.minio.UploadObjectArgs;
import io.minio.errors.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class minioTest {
    @Value("${minio.endpoint}")
    String endpoint;
    @Value("${minio.accessKey}")
            String accessKey;
    @Value("${minio.secretKey}")
            String secretKey;

    MinioClient minioClient =
            MinioClient.builder()
                    .endpoint("http://home.overthinker.top:9000")
                    .credentials("overthinker","overH@xlq.com")
                    .build();

    @Test
    public void test_upload() throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        minioClient.uploadObject(
                UploadObjectArgs.builder()
                        .bucket("cloud2024")
                        .object("test")
                        .filename("D:\\OneDrive - MSFT\\图片\\宠物图片\\猫狗二号.jpg")
                        .build()
        );
    }

    @Test
    public void test_delete() throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        RemoveObjectArgs removeObjectArgs = RemoveObjectArgs.builder().bucket("cloud2024").object("test").build();
        minioClient.removeObject(removeObjectArgs);
    }
}
