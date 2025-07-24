package com.overthinker.cloud.media.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Entity for media asset metadata.
 */
@Data
@Accessors(chain = true)
@TableName("media_asset")
public class MediaAsset implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * Original file name uploaded by the client.
     */
    private String fileName;

    /**
     * Unique object name in MinIO storage (e.g., UUID + extension).
     */
    private String objectName;

    /**
     * The MinIO bucket where the file is stored.
     */
    private String bucketName;

    /**
     * MIME type of the file (e.g., "image/jpeg").
     */
    private String fileType;

    /**
     * File size in bytes.
     */
    private Long fileSize;

    /**
     * ID of the user who uploaded the file.
     */
    private Long uploaderId;

    /**
     * Status of the asset (e.g., UPLOADING, UPLOADED, FAILED).
     */
    private String status;

    /**
     * Creation timestamp.
     */
    private LocalDateTime createdAt;

    /**
     * Last update timestamp.
     */
    private LocalDateTime updatedAt;
}
