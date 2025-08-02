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
 * (Like)表实体类
 *
 * @author overH
 * @since 2023-10-18 19:41:18
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("t_like")
public class Like extends BaseData implements BasecopyProperties {
    // 点赞表id
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @Schema(description = "点赞记录ID", example = "1234567890123456789")
    private String id;

    // 点赞的用户id
    @Schema(description = "用户ID", example = "10001")
    private Long userId;

    // 点赞类型(1,文章，2,评论)
    @Schema(description = "点赞类型：1-文章 2-评论", allowableValues = {"1", "2"}, example = "1")
    private Integer type;

    // 点赞的文章id
    @Schema(description = "对应类型的ID（如文章ID、评论ID）", example = "1001")
    private Integer typeId;
}
