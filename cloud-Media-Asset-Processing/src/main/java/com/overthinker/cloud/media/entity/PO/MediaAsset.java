package com.overthinker.cloud.media.entity.PO;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;


import com.overthinker.cloud.common.db.BaseData;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 媒体资产表
 * @TableName media_asset
 */
@TableName(value ="media_asset")
@Data
@Accessors(chain = true)
public class MediaAsset extends BaseData {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId
    private Long id;

    /**
     * 客户端上传的原始文件名
     */
    @TableField(value = "file_name")
    private String fileName;

    /**
     * 在MinIO存储中的唯一对象名称
     */
    @TableField(value = "object_name")
    private String objectName;

    /**
     * 文件存储的MinIO存储桶
     */
    @TableField(value = "bucket_name")
    private String bucketName;

    /**
     * 文件的MIME类型
     */
    @TableField(value = "file_type")
    private String fileType;

    /**
     * 文件大小（以字节为单位）
     */
    @TableField(value = "file_size")
    private Long fileSize;

    /**
     * 文件的MD5哈希值
     */
    @TableField(value = "file_md5")
    private String fileMd5;

    /**
     * 上传文件的用户ID
     */
    @TableField(value = "uploader_id")
    private Long uploaderId;

    /**
     * 资产的状态（例如，UPLOADING, UPLOADED, FAILED）
     */
    @TableField(value = "status")
    private String status;

}