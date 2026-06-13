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
 * (CommentEmail)表实体类
 *
 * @author overH
 * @since 2023-10-19 15:44:56
 */
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@TableName("t_comment")
public class Comment extends BaseData implements BasecopyProperties {
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @Schema(description = "评论ID", example = "1234567890123456789")
    private Long id;

    @Schema(description = "评论类型：1-文章 2-留言板", allowableValues = {"1", "2"}, example = "1")
    private Integer type;

    @Schema(description = "对应类型的ID（如文章ID、留言板ID）", example = "1001")
    private Integer typeId;

    @Schema(description = "父评论ID，顶级评论为null", example = "0")
    private Long parentId;

    @Schema(description = "回复的评论ID，直接评论为null", example = "1234567890123456789")
    private Long replyId;

    @Schema(description = "评论内容", example = "这篇文章写得真好！")
    private String commentContent;

    @Schema(description = "评论用户ID", example = "10001")
    private Long commentUserId;

    @Schema(description = "被回复用户ID", example = "10002")
    private Long replyUserId;

    @Schema(description = "评论审核状态：0-未通过 1-通过", allowableValues = {"0", "1"}, example = "1")
    private Integer isCheck;
}
