package com.overthinker.cloud.api.apis.media.VO;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 媒体资产视图对象
 * <p>
 * 用于 Feign 客户端返回媒体资产信息
 * </p>
 *
 * @author overthinker
 * @since 2025-08-02
 */
@Data
public class MediaAssetVO {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 客户端上传的原始文件名
     */
    private String fileName;

    /**
     * 在MinIO存储中的唯一对象名称
     */
    private String objectName;

    /**
     * 文件存储的MinIO存储桶
     */
    private String bucketName;

    /**
     * 文件的MIME类型
     */
    private String fileType;

    /**
     * 文件大小（以字节为单位）
     */
    private Long fileSize;

    /**
     * 格式化后的文件大小，如 10MB
     */
    private String fileSizeFormatted;

    /**
     * 上传文件的用户ID
     */
    private Long uploaderId;

    /**
     * 资产的状态
     */
    private String status;

    /**
     * 状态描述
     */
    private String statusDesc;

    /**
     * 缩略图对象名称
     */
    private String thumbnailName;

    /**
     * 文件宽度（像素）
     */
    private Integer width;

    /**
     * 文件高度（像素）
     */
    private Integer height;

    /**
     * 视频时长（秒）
     */
    private Integer duration;

    /**
     * 临时下载URL（仅在查询单个文件时返回）
     */
    private String downloadUrl;

    /**
     * 缩略图临时访问URL（如果有缩略图）
     */
    private String thumbnailUrl;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
