package com.overthinker.cloud.auth.controller;

import com.overthinker.cloud.auth.entity.DTO.EmailCodeDTO;
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

@RestController
@RequestMapping("Email")
@RequiredArgsConstructor
@Validated
public class EmailCodeController {
    private final EmailService emailService;

    @Operation(summary = "发送邮箱验证码", description = "发送注册或重置密码的验证码")
    @PostMapping("/send-code")
    public ResultData<Void> sendCode(@RequestBody @Valid EmailCodeDTO emailCodeDTO) {
        emailService.sendEmailCode(emailCodeDTO.getEmail(), emailCodeDTO.getType());
        return ResultData.success();
    }
}
