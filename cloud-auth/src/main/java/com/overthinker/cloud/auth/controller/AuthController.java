package com.overthinker.cloud.auth.controller;

import com.overthinker.cloud.auth.entity.DTO.UserRegisterDTO;
import com.overthinker.cloud.auth.entity.DTO.UserResetConfirmDTO;
import com.overthinker.cloud.auth.entity.DTO.UserResetPasswordDTO;
import com.overthinker.cloud.auth.service.AuthService;
import com.overthinker.cloud.auth.service.UserService;
import com.overthinker.cloud.common.core.resp.ResultData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "用户登录", description = "用户账户登录")
@RestController
@RequestMapping("/auth")
public class AuthController {


    @Resource
    private AuthService authService;

    @Operation(summary = "用户注册", description = "新用户通过邮箱验证码注册账号")
    @PostMapping("/register")
    public ResultData<Void> register(@RequestBody @Valid UserRegisterDTO userRegisterDTO) {
        return authService.register(userRegisterDTO);
    }

    @Operation(summary = "重置密码-确认邮件", description = "找回密码第一步：验证邮箱和验证码是否正确")
    @PostMapping("/reset-confirm")
    public ResultData<Void> resetConfirm(@RequestBody @Valid UserResetConfirmDTO userResetDTO) {
        return authService.resetConfirm(userResetDTO);
    }

    @Operation(summary = "重置密码", description = "找回密码第二步：设置新密码")
    @PostMapping("/reset-password")
    public ResultData<Void> resetPassword(@RequestBody @Valid UserResetPasswordDTO userResetDTO) {
        return authService.resetPassword(userResetDTO);
    }
}
