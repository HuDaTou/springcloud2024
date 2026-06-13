package com.overthinker.cloud.api.apis.auth.dto;

import com.overthinker.cloud.common.core.entity.BasecopyProperties;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 用户评论DTO
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class UserCommentDTO implements BasecopyProperties {

    @NotNull
    private Integer type;

    @NotNull
    private Integer typeId;

    private Long parentId;

    private Long replyId;

    @NotNull
    private String commentContent;

    private Long replyUserId;
}
