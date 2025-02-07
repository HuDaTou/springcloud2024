package com.overthinker.cloud.web.entity.PO;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.overthinker.cloud.web.entity.BaseData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@TableName("t_video")
public class Video implements BaseData {

//        主键唯一id

        private Long id;

//        用户id 为0则表示公共的
        private Long userId;

//        分类id
        private Integer categoryId;

//        视频封面
        private String videoCover;

//        视频地址
        private String video;

//        视频标题
        private String videoTitle;

//        视频介绍
        private String videoContent;

//        视频的扩展名
        private String videoType;

//       默认为1 1表示未被审核状态，2表示审核通过 3表示审核未通过
        private Integer status;

//        视频大小
        private Integer videoSize;


        //文章创建时间
        @TableField(fill = FieldFill.INSERT)
        private Date createTime;
        //文章更新时间
        @TableField(fill = FieldFill.INSERT_UPDATE)
        private Date updateTime;
        //是否删除（0：未删除，1：已删除）
        private Integer isDeleted;

}
