package com.overthinker.cloud.system.starter.auth.webflux.utils;

import lombok.experimental.UtilityClass;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import reactor.core.publisher.Mono;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * WebFlux 安全工具类
 * 用于从Reactive Security上下文（ReactiveSecurityContextHolder）中获取当前登录用户的详细信息。
 * 依赖于Spring Security OAuth2 Resource Server解析后的Token。
 */
@UtilityClass
public class SecurityUtils {

    /**
     * 获取Authentication Mono对象
     */
    public Mono<Authentication> getAuthenticationMono() {
        return ReactiveSecurityContextHolder.getContext()
                .map(ctx -> ctx.getAuthentication());
    }

    /**
     * 获取当前用户名称 (对应JWT中的 'sub' 字段或 'username' 字段)
     * @return 用户名Mono，如果未登录则返回空Mono
     */
    public Mono<String> getUsername() {
        return getAuthenticationMono()
                .filter(auth -> auth != null)
                .map(auth -> {
                    // 优先尝试从Jwt中获取username声明，如果为空则返回默认的Principal名称(sub)
                    if (auth instanceof JwtAuthenticationToken) {
                        JwtAuthenticationToken jwtToken = (JwtAuthenticationToken) auth;
                        String username = (String) jwtToken.getTokenAttributes().get("username");
                        return username != null ? username : auth.getName();
                    }
                    return auth.getName();
                })
                .defaultIfEmpty(null);
    }

    /**
     * 获取当前用户ID
     * 依赖于Cloud-Auth在生成Token时注入的 'user_id' 字段
     * @return 用户ID Mono，如果获取失败则返回空Mono
     */
    public Mono<Long> getUserId() {
        return getAuthenticationMono()
                .filter(auth -> auth instanceof JwtAuthenticationToken)
                .cast(JwtAuthenticationToken.class)
                .map(jwtToken -> {
                    Map<String, Object> attributes = jwtToken.getTokenAttributes();
                    Object userId = attributes.get("user_id");
                    if (userId instanceof Long) {
                        return (Long) userId;
                    } else if (userId instanceof Integer) {
                        return ((Integer) userId).longValue();
                    } else if (userId instanceof String) {
                        try {
                            return Long.parseLong((String) userId);
                        } catch (NumberFormatException e) {
                            return null;
                        }
                    }
                    return null;
                })
                .filter(userId -> userId != null);
    }

    /**
     * 获取当前用户的权限列表 (GrantedAuthority)
     * @return 权限字符串列表Mono
     */
    public Mono<List<String>> getAuthorities() {
        return getAuthenticationMono()
                .filter(auth -> auth != null)
                .map(auth -> auth.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()))
                .defaultIfEmpty(Collections.emptyList());
    }

    /**
     * 获取JWT原始Claims信息
     * @return Map<String, Object> Claims Mono
     */
    public Mono<Map<String, Object>> getClaims() {
        return getAuthenticationMono()
                .filter(auth -> auth instanceof JwtAuthenticationToken)
                .cast(JwtAuthenticationToken.class)
                .map(JwtAuthenticationToken::getTokenAttributes)
                .defaultIfEmpty(Collections.emptyMap());
    }

    /**
     * 判断当前用户是否已认证
     * @return 是否已认证的Mono
     */
    public Mono<Boolean> isAuthenticated() {
        return getAuthenticationMono()
                .map(auth -> auth != null && auth.isAuthenticated())
                .defaultIfEmpty(false);
    }
}