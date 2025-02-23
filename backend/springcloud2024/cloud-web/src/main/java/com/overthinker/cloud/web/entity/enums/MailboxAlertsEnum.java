package com.overthinker.cloud.web.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/** 发送邮箱提醒枚举
 * @author overH
 * @since 2024/8/10 上午2:41
 */
@Getter
@AllArgsConstructor
public enum MailboxAlertsEnum {

    /**
     * 注册邮箱
     */
    REGISTER("register","欢迎注册overthinker", "register-email-template","注册邮箱"),
    /**
     * 重置密码邮箱
     */
    RESET("reset","overthinker重置密码", "reset-password-template","重置密码邮箱"),
    /**
     * 重置邮箱的邮箱
     */
    RESET_EMAIL("resetEmail","overthinker重置电子邮箱", "reset-email-template","重置邮箱的邮箱"),
    /**
     * 友链申请邮箱
     */
    FRIEND_LINK_APPLICATION("friendLinkApplication","overthinker友链申请通知", "link-email-template","友链申请邮箱"),
    /**
     * 友链审核通知
     */
    FRIEND_LINK_APPLICATION_PASS("friendLinkApplicationPass","overthinker友链审核通知", "email-getThrough-template","友链审核通知"),
    /**
     * 有新的评论的邮箱提醒
     */
    COMMENT_NOTIFICATION_EMAIL("commentNotificationEmail","overthinker--有新的评论", "comment-email-template","有新的评论的邮箱提醒"),
    /**
     * 有新的回复的邮箱提醒
     */
    REPLY_COMMENT_NOTIFICATION_EMAIL("replyCommentNotificationEmail","overthinker--有新的回复", "reply-comment-email-template","有新的回复的邮箱提醒"),
    /**
     * 有新的留言
     */
    MESSAGE_NOTIFICATION_EMAIL("messageNotificationEmail","overthinker--有新的留言", "message-email-template","有新的留言提醒");

    // 字符标识
    private final String codeStr;
    // 邮箱主题
    private final String subject;
    // 模板名称
    private final String templateName;
    // 描述
    private final String desc;
}
