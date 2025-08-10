package com.overthinker.cloud.media.entity.DTO;


import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;


@Data
@Schema(name = "FileUploadRulesInfoDTO", description = "文件上传规则信息数据传输对象")
public class FileUploadRulesInfoDTO {

    @Schema(description = "规则唯一标识", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer id;

    @Schema(description = "规则名称", example = "通用文档上传规则", requiredMode = Schema.RequiredMode.REQUIRED)
    private String ruleName;

    @Schema(description = "文件类别", example = "document", allowableValues = {"image", "document", "video", "audio"})
    private String fileCategory;

    @Schema(description = "允许的文件扩展名（逗号分隔）", example = ".pdf,.doc,.docx")
    private String allowedExtensions;

    @Schema(description = "单个文件的最大大小（单位：KB）", example = "10240", minimum = "1")
    private Integer maxSizeKb;

    @Schema(description = "文件在对象存储（如 MinIO）中的存储路径或前缀", example = "uploads/documents/")
    private String storagePath;

    @Schema(description = "规则是否生效", example = "true")
    private Boolean isActive;

    @Schema(description = "规则的描述信息", example = "允许上传 PDF 和 Word 文档，最大 10MB")
    private String description;
}