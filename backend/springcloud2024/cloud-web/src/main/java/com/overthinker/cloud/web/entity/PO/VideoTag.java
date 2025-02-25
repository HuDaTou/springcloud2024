package com.overthinker.cloud.web.entity.PO;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("t_video_tag")
public class VideoTag {
    //关系表id
    private Long id;
    //文章id
    @TableId
    private Long videoId;
    //标签id
    private Long tagId;
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    //是否删除（0：未删除，1：已删除）
    private Integer isDeleted;
}
