package com.overthinker.cloud.auth.utils;

import com.overthinker.cloud.auth.entity.DTO.MailDTO;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import java.util.Map;

@Slf4j
@Component
public class MailUtils {
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String fromEmail;

    /**
     * 发送 HTML 模板邮件
     *
     * @param mailDTO 邮件信息

     */
    public void sendHtmlMail(MailDTO mailDTO) {
        try {
            // 1. 准备 Thymeleaf 上下文
            Context context = new Context();
            context.setVariables(mailDTO.getVariables());

            // 2. 渲染模板
            String emailContent = templateEngine.process(mailDTO.getTemplateName(), context);


// 3. 构造邮件
            MimeMessage message = mailSender.createMimeMessage();
            // true 表示这是一封多部分邮件（可以带 HTML 和 附件）
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            // --- 核心逻辑开始 ---

            // 设置发件人 (建议从配置文件的 fromEmail 注入，必须与 QQ 邮箱账号一致)
            helper.setFrom(fromEmail);

            // 设置收件人 (从 DTO 获取)
            helper.setTo(mailDTO.getTo());

            // 设置标题 (建议在 DTO 中增加 subject 字段，或者根据模板名动态判断)
            helper.setSubject(mailDTO.getSubject());

            // 设置内容 (emailContent 是模板渲染后的 HTML 字符串)
            // 第二个参数为 true，表示按 HTML 格式解析
            helper.setText(emailContent, true);

            // --- 核心逻辑结束 ---

            // 4. 发送
            mailSender.send(message);
            log.info("邮件已成功发送至：{}", mailDTO.getTo());

        } catch (MessagingException e) {
            log.error("邮件发送失败，收件人：{}，原因：{}", mailDTO.getTo(), e.getMessage());
            throw new RuntimeException("邮件服务异常");
        }
    }
}
