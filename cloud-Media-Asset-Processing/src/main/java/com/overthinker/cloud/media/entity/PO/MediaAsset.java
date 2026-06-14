package com.overthinker.cloud.media.entity.PO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.overthinker.cloud.common.core.entity.BasecopyProperties;
import com.overthinker.cloud.common.db.BaseData;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 媒体资产表
 * <p>
 * 对应数据库表：media_asset
 * </p>
 *
 * @author overthinker
 * @since 2025-08-02
 */
@Schema(description = "媒体资产实体")
@TableName(value = "media_asset")
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class MediaAsset extends BaseData implements BasecopyProperties, Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @Schema(description = "主键ID", example = "1")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 客户端上传的原始文件名
     */
    @Schema(description = "客户端上传的原始文件名", example = "test-video.mp4")
    @TableField(value = "file_name")
    private String fileName;

    /**
     * 在MinIO存储中的唯一对象名称
     */
    @Schema(description = "在MinIO存储中的唯一对象名称", example = "a1b2c3d4e5f6.mp4")
    @TableField(value = "object_name")
    private String objectName;

    /**
     * 文件存储的MinIO存储桶
     */
    @Schema(description = "文件存储的MinIO存储桶", example = "cloud")
    @TableField(value = "bucket_name")
    private String bucketName;

    /**
     * 文件的MIME类型
     */
    @Schema(description = "文件的MIME类型", example = "video/mp4")
    @TableField(value = "file_type")
    private String fileType;

    /**
     * 文件大小（以字节为单位）
     */
    @Schema(description = "文件大小（以字节为单位）", example = "10485760")
    @TableField(value = "file_size")
    private Long fileSize;

    /**
     * 文件的MD5哈希值
     */
    @Schema(description = "文件的MD5哈希值", example = "d41d8cd98f00b204e9800998ecf8427e")
    @TableField(value = "file_md5")
    private String fileMd5;

    /**
     * 上传文件的用户ID
     */
    @Schema(description = "上传文件的用户ID", example = "1")
    @TableField(value = "uploader_id")
    private Long uploaderId;

    /**
     * 资产的状态（UPLOADING, UPLOADED, FAILED, PROCESSING, PROCESSED）
     */
    @Schema(description = "资产的状态：UPLOADING-上传中, UPLOADED-已上传, FAILED-上传失败, PROCESSING-处理中, PROCESSED-已处理完成", example = "UPLOADED")
    @TableField(value = "status")
    private String status;

    /**
     * 缩略图对象名称（图片/视频处理后生成）
     */
    @Schema(description = "缩略图对象名称", example = "a1b2c3d4e5f6_thumb.jpg")
    @TableField(value = "thumbnail_name")
    private String thumbnailName;

    /**
     * 文件宽度（图片/视频）
     */
    @Schema(description = "文件宽度（图片/视频），单位像素", example = "1920")
    @TableField(value = "width")
    private Integer width;

    /**
     * 文件高度（图片/视频）
     */
    @Schema(description = "文件高度（图片/视频），单位像素", example = "1080")
    @TableField(value = "height")
    private Integer height;

    /**
     * 视频时长（秒）
     */
    @Schema(description = "视频时长（秒）", example = "120")
    @TableField(value = "duration")
    private Integer duration;
}
