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
 * (Favorite)表实体类
 *
 * @author overH
 * @since 2023-10-18 14:12:23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@TableName("t_favorite")
public class Favorite extends BaseData implements BasecopyProperties {

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @Schema(description = "收藏记录ID", example = "1234567890123456789")
    private Long id;

    @Schema(description = "用户ID", example = "10001")
    private Long userId;

    @Schema(description = "收藏类型：1-文章 2-留言板", allowableValues = { "1", "2" }, example = "1")
    private Integer type;

    @Schema(description = "对应类型的ID（如文章ID、留言板ID）", example = "1001")
    private Long typeId;

    @Schema(description = "收藏是否有效（0：无效，1：有效）", example = "1")
    private Integer isCheck;
}
