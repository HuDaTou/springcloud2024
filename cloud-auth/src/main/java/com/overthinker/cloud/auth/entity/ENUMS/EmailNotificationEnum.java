package com.overthinker.cloud.auth.entity.ENUMS;

import com.overthinker.cloud.auth.entity.DTO.MailDTO;
import com.overthinker.cloud.common.core.exception.BusinessException;
import com.overthinker.cloud.common.core.resp.ReturnCodeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Getter
@AllArgsConstructor
public enum EmailNotificationEnum {

    FRIEND_LINK_APPLICATION(
            "friendLinkApplication",
            "overthinker友链申请通知",
            "link-email-template"
    ),

    FRIEND_LINK_APPLICATION_PASS(
            "friendLinkApplicationPass",
            "overthinker友链审核通知",
            "email-getThrough-template"
    ),

    COMMENT_NOTIFICATION_EMAIL(
            "commentNotificationEmail",
            "overthinker--有新的评论",
            "comment-email-template"
    ),

    REPLY_COMMENT_NOTIFICATION_EMAIL(
            "replyCommentNotificationEmail",
            "overthinker--有新的回复",
            "reply-comment-email-template"
    ),

    MESSAGE_NOTIFICATION_EMAIL(
            "messageNotificationEmail",
            "overthinker--有新的留言",
            "message-email-template"
    );

    private final String type;
    private final String subject;
    private final String templateName;

    public MailDTO toMailDTO(String email, Map<String, Object> content) {
        Map<String, Object> variables = content != null ? new HashMap<>(content) : new HashMap<>();
        return new MailDTO()
                .setTo(email)
                .setSubject(getSubject())
                .setTemplateName(getTemplateName())
                .setVariables(variables);
    }

    public static EmailNotificationEnum getByType(String type) {
        return Arrays.stream(values())
                .filter(item -> item.getType().equalsIgnoreCase(type))
                .findFirst()
                .orElseThrow(() -> new BusinessException(ReturnCodeEnum.TYPE_NOT_RECOGNIZED, "未知的邮件通知类型: " + type));
    }
}
