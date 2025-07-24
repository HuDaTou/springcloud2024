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

    /**
     * 合并所有分片
     *
     * @param filename a {@link java.lang.String} object.
     * @param uploadId a {@link java.lang.String} object.
     */
    void completeMultipartUpload(String filename, String uploadId);

    /**
     * List files in the bucket with pagination.
     *
     * @param pageSize the number of items to return.
     * @param nextMarker the marker for the next page.
     * @return a map containing the list of files and the next marker.
     */
    Map<String, Object> listFiles(int pageSize, String nextMarker);

    /**
     * Delete a file from the bucket.
     *
     * @param filename the name of the file to delete.
     */
    void deleteFile(String filename);

    /**
     * Get a presigned URL for downloading a file.
     *
     * @param filename the name of the file.
     * @return the presigned URL for downloading.
     */
    String getPresignedFileUrl(String filename);
}
