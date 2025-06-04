package com.overthinker.cloud.web.entity.PO;

import com.baomidou.mybatisplus.annotation.*;
import com.overthinker.cloud.entity.BaseData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


/**
 * (Like)表实体类
 *
 * @author overH
 * @since 2023-10-18 19:41:18
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("t_like")
public class Like implements BaseData {
    //点赞表id
    @TableId(value = "id" ,type = IdType.ASSIGN_ID)
    private String id;
    //点赞的用户id
    private Long userId;
    //点赞类型(1,文章，2,评论)
    private Integer type;
    //点赞的文章id
    private Integer typeId;
    //点赞时间
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    // 修改时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}

