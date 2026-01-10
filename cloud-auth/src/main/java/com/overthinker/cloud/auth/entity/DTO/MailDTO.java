package com.overthinker.cloud.auth.entity.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class MailDTO implements Serializable {

    // 必须指定序列化 ID，确保生产者和消费者版本兼容
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 收件人邮箱地址
     * 例如: "user@example.com"
     */
    private String to;

    /**
     * Thymeleaf 模板名称
     * 对应 resources/templates 下的 HTML 文件名（不含 .html 后缀）
     * 例如: "welcome_email" 或 "reset_password_email"
     */
    private String templateName;

    /**
     * 邮件主题
     * 例如: "欢迎来到我们的网站" 或 "密码重置"
     */
    private String subject;

    /**
     * 模板动态变量
     * Key: 对应 HTML 中 th:text="${key}" 的名称
     * Value: 实际要替换的值
     * 例如: { "code": "123456", "expirationTime": "5分钟" }
     */
    private Map<String, Object> variables;
}
