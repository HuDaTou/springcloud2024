package com.overthinker.cloud.media.entity.VO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 媒体资产视图对象
 * <p>
 * 用于返回给前端的媒体资产信息
 * </p>
 *
 * @author overthinker
 * @since 2025-08-02
 */
@Data
@Schema(description = "媒体资产视图对象")
public class MediaAssetVO {

    @Schema(description = "主键ID", example = "1")
    private Long id;

    @Schema(description = "客户端上传的原始文件名", example = "test-video.mp4")
    private String fileName;

    @Schema(description = "在MinIO存储中的唯一对象名称", example = "a1b2c3d4e5f6.mp4")
    private String objectName;

    @Schema(description = "文件存储的MinIO存储桶", example = "cloud")
    private String bucketName;

    @Schema(description = "文件的MIME类型", example = "video/mp4")
    private String fileType;

    @Schema(description = "文件大小（以字节为单位）", example = "10485760")
    private Long fileSize;

    @Schema(description = "格式化后的文件大小，如 10MB", example = "10.00 MB")
    private String fileSizeFormatted;

    @Schema(description = "上传文件的用户ID", example = "1")
    private Long uploaderId;

    @Schema(description = "资产的状态", example = "UPLOADED")
    private String status;

    @Schema(description = "状态描述", example = "已上传")
    private String statusDesc;

    @Schema(description = "缩略图对象名称", example = "a1b2c3d4e5f6_thumb.jpg")
    private String thumbnailName;

    @Schema(description = "文件宽度（像素）", example = "1920")
    private Integer width;

    @Schema(description = "文件高度（像素）", example = "1080")
    private Integer height;

    @Schema(description = "视频时长（秒）", example = "120")
    private Integer duration;

    @Schema(description = "临时下载URL（仅在查询单个文件时返回）")
    private String downloadUrl;

    @Schema(description = "缩略图临时访问URL（如果有缩略图）")
    private String thumbnailUrl;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
