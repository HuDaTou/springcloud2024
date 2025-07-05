package com.overthinker.cloud.media.service;

import io.minio.errors.*;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

public interface UploadService {


    /**
     * 上传第一个分片 初始化分片上传任务，并返回剩余分片的 Presigned URL
     *
     * @param filename
     * @param totalParts
     * @return
     */
    Map<String, Object> handleFirstPartAndGenerateUrls(String filename, int totalParts) throws InsufficientDataException, IOException, NoSuchAlgorithmException, InvalidKeyException, XmlParserException, InternalException, ServerException, ErrorResponseException, InvalidResponseException;
}
