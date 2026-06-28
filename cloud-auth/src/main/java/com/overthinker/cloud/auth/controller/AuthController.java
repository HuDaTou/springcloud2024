package com.overthinker.cloud.auth.controller;

import com.overthinker.cloud.auth.entity.DTO.UserRegisterDTO;
import com.overthinker.cloud.auth.entity.DTO.UserResetConfirmDTO;
import com.overthinker.cloud.auth.entity.DTO.UserResetPasswordDTO;
import com.overthinker.cloud.auth.service.AuthService;
import com.overthinker.cloud.auth.service.UserService;
import com.overthinker.cloud.common.core.resp.ResultData;
import com.overthinker.cloud.system.starter.auth.utils.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "认证管理", description = "用户注册、登录、密码重置等公开认证接口")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    @Operation(summary = "用户注册", description = "新用户通过邮箱验证码完成注册")
    @PostMapping("/register")
    public ResultData<Void> register(@RequestBody @Valid UserRegisterDTO userRegisterDTO) {
        return authService.register(userRegisterDTO);
    }

    @Operation(summary = "获取当前用户权限码", description = "从 SecurityContext 中获取当前登录用户的权限码列表")
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/codes")
    public ResultData<List<String>> getCodes() {
        return ResultData.success(SecurityUtils.getAuthorities());
    }

    @Operation(summary = "根据用户ID获取权限码", description = "从数据库查询指定用户的权限码列表，供内部服务调用")
    @PreAuthorize("hasAuthority('auth:user:query')")
    @GetMapping("/codes/user")
    public ResultData<List<String>> getUserCodes(
            @Parameter(description = "用户ID", required = true) @RequestParam @NotNull Long userId) {
        return ResultData.success(userService.getUserAuthorities(userId));
    }

    @Operation(summary = "重置密码确认", description = "验证邮箱验证码，确认重置密码操作")
    @PostMapping("/reset-confirm")
    public ResultData<Void> resetConfirm(@RequestBody @Valid UserResetConfirmDTO dto) {
        return authService.resetConfirm(dto);
    }

    @Operation(summary = "重置密码", description = "通过邮箱验证码重置用户密码")
    @PostMapping("/reset-password")
    public ResultData<Void> resetPassword(@RequestBody @Valid UserResetPasswordDTO dto) {
        return authService.resetPassword(dto);
    }
}
