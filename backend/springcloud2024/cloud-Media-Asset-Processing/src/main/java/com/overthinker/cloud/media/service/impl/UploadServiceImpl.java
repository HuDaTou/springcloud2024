package com.overthinker.cloud.media.service.impl;

import com.overthinker.cloud.media.config.MinioProperties;
import com.overthinker.cloud.media.service.UploadService;
import io.minio.CreateMultipartUploadResponse;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioAsyncClient;
import io.minio.errors.*;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;


@Slf4j
@Service

@RequiredArgsConstructor
public class UploadServiceImpl implements UploadService {

    private final MinioProperties minioProperties;
    private final MinioAsyncClient minioAsyncClient;

    public Map<String, Object> handleFirstPartAndGenerateUrls(String filename, int totalParts) throws InsufficientDataException, IOException, NoSuchAlgorithmException, InvalidKeyException, XmlParserException, InternalException, ServerException, ErrorResponseException, InvalidResponseException {



        // Step 1: 异步初始化分片上传
        CompletableFuture<CreateMultipartUploadResponse> future = minioAsyncClient.createMultipartUploadAsync(minioProperties.getBucketName(),null, filename,null, null);

        // 阻塞等待结果（或者也可以用 thenApply/thenAccept 处理异步）
        CreateMultipartUploadResponse response = future.join();
        String uploadId = response.result().uploadId();

        log.info("uploadId: {}", uploadId);

        // Step 2: 为每个分片生成预签名 URL
        Map<Integer, String> presignedUrls = new LinkedHashMap<>();

        for (int partNumber = 1; partNumber <= totalParts; partNumber++) {
            String baseurl = minioAsyncClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.PUT)
                            .bucket(minioProperties.getBucketName())
                            .object(filename)
                            .expiry(2, TimeUnit.HOURS)
                            .build()
            );

            String signedUrl = UriComponentsBuilder.fromUriString(baseurl)
                    .queryParam("uploadId", uploadId)
                    .queryParam("partNumber", partNumber)
                    .toUriString();

            presignedUrls.put(partNumber, signedUrl);
        }

        // Step 3: 构造返回值
        Map<String, Object> result = new HashMap<>();
        result.put("uploadId", uploadId);
        result.put("presignedUrls", presignedUrls);
        result.put("filename", filename);
        result.put("totalParts", totalParts);

        return result;
    }


}
