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
@Builder
@NoArgsConstructor
@TableName("t_video")
public class Video implements BaseData {

        @TableId
        private Long id;


        private Long userId;


        private Long categoryId;


        private String vedioCover;


        private String vedio;


        private String vedioTitle;


        private String vedioDescription;


        private String vedioType;


        private Boolean permission;


        private Boolean status;


        private String vedioSize;

        private Long vedioCount;


        //文章创建时间
        @TableField(fill = FieldFill.INSERT)
        private Date createTime;
        //文章更新时间
        @TableField(fill = FieldFill.INSERT_UPDATE)
        private Date updateTime;
        //是否删除（0：未删除，1：已删除）
        private Integer isDeleted;

}
