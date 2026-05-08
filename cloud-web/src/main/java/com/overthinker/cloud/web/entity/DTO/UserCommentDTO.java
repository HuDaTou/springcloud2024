package com.overthinker.cloud.web.entity.DTO;

import com.overthinker.cloud.common.core.entity.BasecopyProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserCommentDTO implements BasecopyProperties {

    @Schema(description = "评论类型 (1文章 2留言板)")
    @NotNull
    private Integer type;

    @Schema(description = "类型id")
    @NotNull
    private Integer typeId;

    @Schema(description = "父评论id")
    private Long parentId;

    @Schema(description = "回复评论id")
    private Long replyId;

    @Schema(description = "评论的内容")
    @NotNull
    private String commentContent;

    @Schema(description = "回复用户的id")
    private Long replyUserId;
}