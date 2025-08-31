package com.overthinker.cloud.web.entity.PO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.overthinker.cloud.common.entity.BasecopyProperties;
import com.overthinker.cloud.common.entity.PO.BaseData;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * (Tag)表实体类
 *
 * @author overH
 * @since 2023-10-15 02:29:14
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_tag")
public class Tag extends BaseData implements BasecopyProperties {
    // 标签id
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @Schema(description = "标签ID", example = "1234567890123456789")
    private Long id;

    // 标签名称
    @Schema(description = "标签名称", example = "技术")
    private String tagName;

    // 是否删除（0：未删除，1：已删除）
    @Schema(description = "是否删除（0：未删除，1：已删除）", example = "0")
    private Integer isDeleted;
}
