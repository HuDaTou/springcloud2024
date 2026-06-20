package com.overthinker.cloud.auth.controller;

import com.overthinker.cloud.api.apis.auth.api.EmailClient;
import com.overthinker.cloud.auth.entity.DTO.EmailCodeDTO;
import com.overthinker.cloud.auth.entity.DTO.EmailNotificationDTO;
import com.overthinker.cloud.auth.service.EmailService;
import com.overthinker.cloud.common.core.resp.ResultData;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 邮件控制器
 * <p>
 * 同时提供：
 * <ul>
 *   <li>对外 HTTP 接口：供前端/网关直接调用</li>
 * </ul>
 * 内部接口的访问控制由 SecurityConfig 中的 IP 白名单限制
 * </p>
 *
 * @author overthinker
 * @since 2025-06-13
 */
@RestController
@RequestMapping("/email")
@RequiredArgsConstructor
@Validated
public class EmailController implements EmailClient {

    private final EmailService emailService;

    @Operation(summary = "发送邮箱验证码", description = "发送注册或重置密码的验证码")
    @PostMapping("/send-code")
    public ResultData<Void> sendCode(@RequestBody @Valid EmailCodeDTO emailCodeDTO) {
        emailService.sendEmailCode(emailCodeDTO.getEmail(), emailCodeDTO.getType());
        return ResultData.success();
    }

    @Override
    public ResultData<Void> sendEmailCode(String email, String type) {
        emailService.sendEmailCode(email, type);
        return ResultData.success();
    }

    @Operation(summary = "发送邮件通知", description = "发送各类业务通知邮件，如评论通知、留言通知、友链申请通知等")
    @PostMapping("/send-notification")
    public ResultData<Void> sendNotification(@RequestBody @Valid EmailNotificationDTO notificationDTO) {
        emailService.sendEmailNotification(
                notificationDTO.getEmail(),
                notificationDTO.getType(),
                notificationDTO.getContent()
        );
        return ResultData.success();
    }

    @Override
    public ResultData<Void> sendEmailNotification(String email, String type, Map<String, Object> content) {
        emailService.sendEmailNotification(email, type, content);
        return ResultData.success();
    }
}
