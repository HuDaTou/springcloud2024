package com.overthinker.cloud.api.apis.media.DTO;

import lombok.Data;

import java.util.List;

/**
 * 文件上传规则请求参数（新增/更新）
 *
 * @author overthinker
 * @since 2025-08-02
 */
@Data
public class FileUploadRulesRequest {

    /**
     * 规则名称
     */
    private String ruleName;

    /**
     * 文件类别列表
     */
    private List<String> fileCategory;

    /**
     * 允许的文件MIME类型列表
     */
    private List<String> allowedExtensions;

    /**
     * 单个文件的最大大小（KB）
     */
    private Integer maxSizeKb;

    /**
     * 文件在对象存储中的存储路径
     */
    private String storagePath;

    /**
     * 文件在对象存储中的存储前缀
     */
    private String storagePrefix;

    /**
     * 规则是否生效
     */
    private Boolean isActive;

    /**
     * 规则描述信息
     */
    private String description;
}
