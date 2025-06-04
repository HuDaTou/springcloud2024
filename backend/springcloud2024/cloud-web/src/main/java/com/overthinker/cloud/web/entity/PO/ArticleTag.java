package com.overthinker.cloud.web.entity.PO;

import com.baomidou.mybatisplus.annotation.*;
import com.overthinker.cloud.entity.BaseData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


/**
 * (ArticleTag)表实体类
 *
 * @author overH
 * @since 2023-10-15 02:29:13
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("t_article_tag")
public class ArticleTag implements BaseData {
    //关系表id

    @TableId(value = "id" ,type = IdType.ASSIGN_ID)
    private Long id;
    //文章id
    @TableId
    private Long articleId;
    //标签id
    private Long tagId;
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    //是否删除（0：未删除，1：已删除）
    private Integer isDeleted;
}

