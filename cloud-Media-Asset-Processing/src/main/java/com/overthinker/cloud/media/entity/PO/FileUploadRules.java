package com.overthinker.cloud.media.entity.PO;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;


import com.overthinker.cloud.common.core.entity.BasecopyProperties;
import com.overthinker.cloud.common.db.BaseData;
import com.overthinker.cloud.common.db.handler.mybatisTypeHandler.StringListTypeHandler;
import lombok.Data;

import java.util.List;

/**
 * 存储文件上传规则，如大小、类型和存储路径
 * @TableName file_upload_rules
 */
@TableName(value ="file_upload_rules")
@Data
public class FileUploadRules extends BaseData implements BasecopyProperties {
    /**
     * 
     */
    @TableId
    private Long id;

    /**
     * 规则的唯一名称，用于程序中引用
     */
    @TableField(value = "rule_name")
    private String ruleName;

    /**
     * 文件分类，如 AVATAR, PRODUCT_IMAGE, DOCUMENT
     */
    @TableField(value = "file_category", typeHandler = StringListTypeHandler.class)
    private List<String> fileCategory;

    /**
     * 允许的文件扩展名，用逗号分隔。如果为NULL或空，则不限制。
     */
    @TableField(value = "allowed_extensions", typeHandler = StringListTypeHandler.class)
    private List< String> allowedExtensions;

    /**
     * 单个文件的最大大小（单位：KB）
     */
    @TableField(value = "max_size_kb")
    private Integer maxSizeKb;

    /**
     * 文件在对象存储（如MinIO）中的存储路径或前缀
     */
    @TableField(value = "storage_path")
    private String storagePath;

    /**
     * 规则是否生效
     */
    @TableField(value = "is_active")
    private Boolean isActive;

    /**
     * 规则的描述信息
     */
    @TableField(value = "description")
    private String description;

}