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
@Builder
@NoArgsConstructor
@TableName("t_video")
public class Video implements BaseData {

        @TableId(value = "id" ,type = IdType.ASSIGN_ID)
        private Long id;


        private Long userId;


        private Long categoryId;


        private String videoCover;


        private String video;


        private String videoTitle;


        private String videoDescription;


        private String videoType;


        private Boolean permission;


        private Boolean status;


        private String videoSize;

        private Long videoVisit;


        //文章创建时间
        @TableField(fill = FieldFill.INSERT)
        private Date createTime;
        //文章更新时间
        @TableField(fill = FieldFill.INSERT_UPDATE)
        private Date updateTime;
        //是否删除（0：未删除，1：已删除）
        private Integer isDeleted;

}
