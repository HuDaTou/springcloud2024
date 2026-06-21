package com.overthinker.cloud.api.apis.media.DTO;

import lombok.Data;

/**
 * 普通上传预签名URL请求参数
 *
 * @author overthinker
 * @since 2025-08-02
 */
@Data
public class PresignedUploadRequest {

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 文件大小（字节）
     */
    private long fileSize;

    /**
     * 文件MIME类型
     */
    private String contentType;

    /**
     * 文件MD5值
     */
    private String fileMd5;

    /**
     * 文件类型规则ID（可选）
     */
    private Long fileType;
}
