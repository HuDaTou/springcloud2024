package com.overthinker.cloud.media.entity.DTO;


import lombok.Data;

@Data
public class FileUplodRulesInfoDTO {



    private Integer id;



    private String ruleName;


    private String fileCategory;


    private String allowedExtensions;

    /**
     * 单个文件的最大大小（单位：KB）
     */
    private Integer maxSizeKb;

    /**
     * 文件在对象存储（如MinIO）中的存储路径或前缀
     */
    private String storagePath;

    /**
     * 规则是否生效
     */
    private Boolean isActive;

    /**
     * 规则的描述信息
     */
    private String description;
}
