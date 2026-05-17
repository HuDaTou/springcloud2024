package com.overthinker.cloud.auth.controller;

import com.overthinker.cloud.auth.entity.DTO.UserRegisterDTO;
import com.overthinker.cloud.auth.service.AuthService;
import com.overthinker.cloud.common.core.resp.ResultData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "认证服务", description = "用户注册、登录、密码重置等公开认证接口")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "用户注册", description = "新用户通过邮箱验证码完成注册")
    @PostMapping("/register")
    public ResultData<Void> register(@RequestBody @Valid UserRegisterDTO userRegisterDTO) {
        return authService.register(userRegisterDTO);
    }

}
