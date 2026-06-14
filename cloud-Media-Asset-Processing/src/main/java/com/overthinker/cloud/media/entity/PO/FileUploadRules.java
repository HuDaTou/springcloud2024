package com.overthinker.cloud.media.entity.PO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.overthinker.cloud.common.core.entity.BasecopyProperties;
import com.overthinker.cloud.common.db.BaseData;
import com.overthinker.cloud.common.db.handler.mybatisTypeHandler.StringListTypeHandler;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * 文件上传规则表
 * <p>
 * 存储文件上传规则，如大小、类型和存储路径
 * 对应数据库表：file_upload_rules
 * </p>
 *
 * @author overthinker
 * @since 2025-08-02
 */
@Schema(description = "文件上传规则实体")
@TableName(value = "file_upload_rules")
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class FileUploadRules extends BaseData implements BasecopyProperties, Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @Schema(description = "主键ID", example = "1")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 规则的唯一名称，用于程序中引用
     */
    @Schema(description = "规则名称，用于程序中引用", example = "AVATAR_UPLOAD")
    @TableField(value = "rule_name")
    private String ruleName;

    /**
     * 文件分类，如 AVATAR, PRODUCT_IMAGE, DOCUMENT
     */
    @Schema(description = "文件分类列表，如 AVATAR, PRODUCT_IMAGE, DOCUMENT", example = "[\"AVATAR\", \"PROFILE\"]")
    @TableField(value = "file_category", typeHandler = StringListTypeHandler.class)
    private List<String> fileCategory;

    /**
     * 允许的文件扩展名，用逗号分隔。如果为NULL或空，则不限制。
     */
    @Schema(description = "允许的文件MIME类型列表", example = "[\"image/jpeg\", \"image/png\"]")
    @TableField(value = "allowed_extensions", typeHandler = StringListTypeHandler.class)
    private List<String> allowedExtensions;

    /**
     * 单个文件的最大大小（单位：KB）
     */
    @Schema(description = "单个文件的最大大小（单位：KB）", example = "5120")
    @TableField(value = "max_size_kb")
    private Integer maxSizeKb;

    /**
     * 文件在对象存储（如MinIO）中的存储路径
     */
    @Schema(description = "文件在对象存储中的存储路径", example = "avatars/")
    @TableField(value = "storage_path")
    private String storagePath;

    /**
     * 规则是否生效
     */
    @Schema(description = "规则是否生效", example = "true")
    @TableField(value = "is_active")
    private Boolean isActive;

    /**
     * 规则的描述信息
     */
    @Schema(description = "规则描述信息", example = "用户头像上传规则")
    @TableField(value = "description")
    private String description;


    /**
     * 文件在对象存储中的存储前缀，用于拼接最终的对象名
     */
    @Schema(description = "文件在对象存储中的存储前缀", example = "avatars/")
    @TableField(value = "storage_prefix")
    private String storagePrefix;
}
