package com.overthinker.cloud.web.entity.PO;

import com.baomidou.mybatisplus.annotation.*;
import com.overthinker.cloud.entity.BaseData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName(value =  "t_photo")
public class Photo implements BaseData {
    //自增id
    @TableId(value = "id" ,type = IdType.ASSIGN_ID)
    private Long id;
    //创建者id
    private Long userId;
    //名称
    private String name;
    //描述
    private String description;
    //类型（1：相册 2：照片）
    private Integer type;
    //父相册id
    private Long parentId;
    //图片地址
    private String url;
    //是否通过 (0否 1是)
    private Integer isCheck;
    //照片体积大小(kb)
    private Double size;
    //创建时间
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    //更新时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
    //是否删除（0：未删除，1：已删除）
    private Integer isDeleted;
}