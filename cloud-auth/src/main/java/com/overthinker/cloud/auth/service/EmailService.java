package com.overthinker.cloud.auth.service;

import java.util.Map;

public interface EmailService {

    /**
     * 根据邮箱发送验证码
     * 
     * @param email 邮箱
     * @param type  邮件模板
     */
    void sendEmailCode(String email, String type);

    /**
     * 发送邮件通知
     *
     * @param email   收件人邮箱
     * @param type    邮件类型
     * @param content 邮件内容变量
     */
    void sendEmailNotification(String email, String type, Map<String, Object> content);
}
