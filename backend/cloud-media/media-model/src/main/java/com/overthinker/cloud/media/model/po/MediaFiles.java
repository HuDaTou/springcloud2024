package com.overthinker.cloud.media.model.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * <p>
 * 媒资信息
 * </p>
 *
 * @author itcast
 */
@Data
@TableName("media_files")
public class MediaFiles implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 文件类型
     */
    private String type;

    /**
     * 标签
     */
    private String tags;

    /**
     * 存储桶
     */
    private String bucket;

    /**
     * 存储路径
     */
    private String filePath;


    /**
     * 文件标识
     */
    private String fileId;

    /**
     * 媒资文件访问地址
     */
    private String url;


    /**
     * 上传人
     */
    private String username;

    /**
     * 上传时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createDate;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime changeDate;

    /**
     * 备注
     */
    private String remark;

    /**
     * 文件大小
     */
    private Long fileSize;

}
