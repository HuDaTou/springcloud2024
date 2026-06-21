package com.overthinker.cloud.api.apis.media.DTO;

import lombok.Data;

/**
 * 初始化分片上传请求参数
 *
 * @author overthinker
 * @since 2025-08-02
 */
@Data
public class InitiateMultipartUploadRequest {

    /**
     * 文件类型规则ID
     */
    private Long fileType;

    /**
     * 文件名
     */
    private String filename;

    /**
     * 文件被分割的总块数
     */
    private int totalParts;

    /**
     * 文件总大小（字节）
     */
    private long fileSize;

    /**
     * 文件的MIME类型
     */
    private String contentType;

    /**
     * 文件的MD5哈希值
     */
    private String fileMd5;
}
