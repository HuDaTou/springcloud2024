package com.overthinker.cloud.web.entity.PO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.overthinker.cloud.common.core.entity.BasecopyProperties;
import com.overthinker.cloud.common.db.BaseData;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;


/**
 * (Like)表实体类
 *
 * @author overH
 * @since 2023-10-18 19:41:18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@TableName("t_like")
public class Like extends BaseData implements BasecopyProperties {
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @Schema(description = "点赞记录ID", example = "1234567890123456789")
    private Long id;

    @Schema(description = "用户ID", example = "10001")
    private Long userId;

    @Schema(description = "点赞类型：1-文章 2-评论", allowableValues = {"1", "2"}, example = "1")
    private Integer type;

    @Schema(description = "对应类型的ID（如文章ID、评论ID）", example = "1001")
    private Integer typeId;
}
