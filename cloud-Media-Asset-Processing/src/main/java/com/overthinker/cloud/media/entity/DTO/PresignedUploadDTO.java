package com.overthinker.cloud.media.entity.DTO;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "PresignedUploadDTO", description = "普通上传预签名URL请求参数")
public record PresignedUploadDTO(
        @Schema(description = "文件名", requiredMode = Schema.RequiredMode.REQUIRED, example = "test.jpg")
        String fileName,

        @Schema(description = "文件大小（字节）", requiredMode = Schema.RequiredMode.REQUIRED, example = "1048576")
        long fileSize,

        @Schema(description = "文件MIME类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "image/jpeg")
        String contentType,

        @Schema(description = "文件MD5值", requiredMode = Schema.RequiredMode.REQUIRED, example = "d41d8cd98f00b204e9800998ecf8427e")
        String fileMd5,

        @Schema(description = "文件类型ID（可选，用于校验文件规则）", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "1")
        Long fileType
) {}
