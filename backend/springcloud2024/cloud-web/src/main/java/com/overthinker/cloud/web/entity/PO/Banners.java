package com.overthinker.cloud.web.entity.PO;

import com.baomidou.mybatisplus.annotation.*;
import com.overthinker.cloud.entity.BaseData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


/**
 * (Banners)表实体类
 *
 * @author overH
 * @since 2024-08-28 09:51:16
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("t_banners")
public class Banners implements BaseData {
    //主键id

    @TableId(value = "id" ,type = IdType.ASSIGN_ID)
    private Long id;
    //图片路径
    private String path;
    //图片大小 (字节)
    private Long size;
    //图片类型 (MIME)
    private String type;
    //上传人id
    private Long userId;
    //图片顺序
    private Integer sortOrder;
    //创建时间
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
}

