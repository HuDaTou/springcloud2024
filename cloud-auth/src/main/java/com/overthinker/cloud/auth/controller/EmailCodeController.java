package com.overthinker.cloud.auth.controller;

import com.overthinker.cloud.auth.entity.DTO.EmailCodeDTO;
import com.overthinker.cloud.auth.service.EmailService;
import com.overthinker.cloud.common.core.annotation.LogAnnotation;
import com.overthinker.cloud.common.core.annotation.LogConst;

import com.overthinker.cloud.redis.annotation.AccessLimit;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("Email")
@RequiredArgsConstructor
@Validated
public class EmailCodeController {
    private final EmailService emailService;

    /**
     * 通过邮件发送验证码
     */
    @Operation(summary = "通过邮箱获取验证码")
    @Parameter(name = "email-code", description = "验证码")
    @LogAnnotation(module = "黑名单管理", operation = LogConst.GET)
    @AccessLimit(seconds = 60, maxCount = 30)
    @PostMapping("/getCode")
    public void sendEmailCode(@RequestBody EmailCodeDTO emailCodeDTO) {
        emailService.getEmailCode(emailCodeDTO.getEmail(), emailCodeDTO.getType());
    }
}
