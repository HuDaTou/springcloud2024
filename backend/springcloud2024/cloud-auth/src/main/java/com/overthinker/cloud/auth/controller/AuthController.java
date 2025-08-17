package com.overthinker.cloud.auth.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.overthinker.cloud.auth.entity.DTO.LoginRequest;
import com.overthinker.cloud.auth.entity.PO.LoginUser;
import com.overthinker.cloud.auth.service.impl.UserServiceImpl;
import com.overthinker.cloud.auth.utils.JwtUtils;

import com.overthinker.cloud.common.resp.ResultData;
import jakarta.annotation.Resource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 用户认证控制器。
 */
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Resource
    private AuthenticationManager authenticationManager;

    @Resource
    private JwtUtils jwtUtils;

    @Resource
    private UserServiceImpl userService;

    @PostMapping("/login")
    public ResultData<String> login(@RequestBody LoginRequest loginRequest) {
        // 使用 Spring Security 的 AuthenticationManager 来验证用户
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password())
        );

        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        Long userId = loginUser.getUser().getId();

        // 创建JWT
        String jwt = jwtUtils.createJwt(loginUser, userId, loginUser.getUsername());

        return ResultData.success(jwt);
    }

    @PostMapping("/internal/api/v1/auth/verify")
    public ResultData<Void> verify(String token, String url) {
        DecodedJWT decodedJWT = jwtUtils.resolveJwt(token);
        if (decodedJWT == null) {
            return ResultData.failure("无效的token");
        }

        Long userId = jwtUtils.toId(decodedJWT);
        List<String> userAuthorities = userService.getUserAuthorities(userId);

        // 这是一个简化的检查。在实际系统中，您将从数据库中查找给定URL所需的权限。
        boolean hasPermission = userAuthorities.stream().anyMatch(url::contains);

        if (hasPermission) {
            return ResultData.success();
        } else {
            return ResultData.failure("没有权限");
        }
    }
}
