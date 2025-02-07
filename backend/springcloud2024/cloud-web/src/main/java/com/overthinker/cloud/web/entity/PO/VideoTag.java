package com.overthinker.cloud.web.entity.PO;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.overthinker.cloud.web.entity.BaseData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("t_video_tag")
public class VideoTag implements BaseData {

    //关系表id
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
