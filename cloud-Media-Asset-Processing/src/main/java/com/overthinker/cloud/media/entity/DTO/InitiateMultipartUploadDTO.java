package com.overthinker.cloud.media.entity.DTO;

import io.swagger.v3.oas.annotations.media.Schema;



@Schema(name = "initiateMultipartUploadDTO", description = "文件上传规则信息数据传输对象")
public record InitiateMultipartUploadDTO(
        @Schema(description = "文件类型选择")
        Long fileType,

        @Schema(description = "文件名")
        String filename,

        @Schema(description = "文件被分割的总块数")
        int totalParts,

        @Schema(description = "文件总大小（以字节为单位）")
        long fileSize,

        @Schema(description = "文件的MIME类型")
        String contentType,

        @Schema(description = "文件的MD5哈希值")
        String fileMd5
) {}
