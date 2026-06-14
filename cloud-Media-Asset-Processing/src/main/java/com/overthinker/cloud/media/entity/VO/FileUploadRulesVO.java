package com.overthinker.cloud.media.entity.VO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 文件上传规则视图对象
 * <p>
 * 用于返回给前端的文件上传规则信息
 * </p>
 *
 * @author overthinker
 * @since 2025-08-02
 */
@Data
@Schema(description = "文件上传规则视图对象")
public class FileUploadRulesVO {

    @Schema(description = "主键ID", example = "1")
    private Long id;

    @Schema(description = "规则名称，用于程序中引用", example = "AVATAR_UPLOAD")
    private String ruleName;

    @Schema(description = "文件分类列表，如 AVATAR, PRODUCT_IMAGE, DOCUMENT", example = "[\"AVATAR\", \"PROFILE\"]")
    private List<String> fileCategory;

    @Schema(description = "允许的文件MIME类型列表", example = "[\"image/jpeg\", \"image/png\"]")
    private List<String> allowedExtensions;

    @Schema(description = "单个文件的最大大小（单位：KB）", example = "5120")
    private Integer maxSizeKb;

    @Schema(description = "文件在对象存储中的存储路径", example = "avatars/")
    private String storagePath;

    @Schema(description = "文件在对象存储中的存储前缀", example = "avatars/")
    private String storagePrefix;

    @Schema(description = "规则是否生效", example = "true")
    private Boolean isActive;

    @Schema(description = "规则描述信息", example = "用户头像上传规则")
    private String description;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
