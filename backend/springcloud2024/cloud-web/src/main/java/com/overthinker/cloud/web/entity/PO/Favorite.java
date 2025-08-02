package com.overthinker.cloud.web.entity.PO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.overthinker.cloud.entity.BasecopyProperties;
import com.overthinker.cloud.entity.PO.BaseData;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * (Favorite)表实体类
 *
 * @author overH
 * @since 2023-10-18 14:12:23
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_favorite")
public class Favorite extends BaseData implements BasecopyProperties {

    // 收藏id
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @Schema(description = "收藏记录ID", example = "1234567890123456789")
    private Long id;

    // 收藏的用户id
    @Schema(description = "用户ID", example = "10001")
    private Long userId;

    // 收藏类型(1,文章 2,留言板)
    @Schema(description = "收藏类型：1-文章 2-留言板", allowableValues = {"1", "2"}, example = "1")
    private Integer type;

    // 类型id
    @Schema(description = "对应类型的ID（如文章ID、留言板ID）", example = "1001")
    private Long typeId;

    // 是否有效 (0否 1是)
    @Schema(description = "收藏是否有效（0：无效，1：有效）", example = "1")
    private Integer isCheck;
}

