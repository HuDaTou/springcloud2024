package com.overthinker.cloud.web.entity.PO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.overthinker.cloud.entity.BasecopyProperties;
import com.overthinker.cloud.web.entity.PO.base.BaseData;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * (Category)表实体类
 *
 * @author overH
 * @since 2023-10-15 02:29:13
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_category")
public class Category extends BaseData implements BasecopyProperties {
    // 分类id
    // 表id
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @Schema(description = "分类主键ID", example = "1234567890123456789")
    private Long id;

    // 分类名
    @Schema(description = "分类名称", example = "电子产品")
    private String categoryName;

    // 是否删除（0：未删除，1：已删除）
    @Schema(description = "是否删除（0：未删除，1：已删除）", example = "0")
    private Integer isDeleted;
}
