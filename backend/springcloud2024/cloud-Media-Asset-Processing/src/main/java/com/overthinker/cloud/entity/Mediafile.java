package com.overthinker.cloud.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @TableName mediafile
 */
@TableName(value = "mediafile")
@Data
public class Mediafile implements Serializable {
    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 文件名称
     */
    private String filename;

    /**
     * 类型
     */
    private String type;

    /**
     * 文件路径
     */
    private String filePath;

    /**
     * 标签
     */
    private String tags;

    /**
     * 文件id
     */
    private Integer fileId;

    /**
     * 访问地址
     */
    private String url;

    /**
     * 上传人
     */
    private String username;

    /**
     * 上传时间
     */
    private Date createDate;

    /**
     * 修改时间
     */
    private Date changeDate;

    /**
     * 状态
     */
    private String status;

    /**
     * 备注
     */
    private String remark;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}