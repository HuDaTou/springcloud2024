package com.overthinker.cloud.system.auth.utils;

import lombok.experimental.UtilityClass;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 安全工具类
 * 用于从Spring Security上下文（SecurityContextHolder）中获取当前登录用户的详细信息。
 * 依赖于Spring Security OAuth2 Resource Server解析后的Token。
 */
@UtilityClass
public class SecurityUtils {

    /**
     * 获取Authentication对象
     */
    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * 获取当前用户名称 (对应JWT中的 'sub' 字段或 'username' 字段)
     * @return 用户名，如果未登录则返回null
     */
    public String getUsername() {
        Authentication authentication = getAuthentication();
        if (authentication == null) {
            return null;
        }
        // 优先尝试从Jwt中获取username声明，如果为空则返回默认的Principal名称(sub)
        if (authentication instanceof JwtAuthenticationToken) {
            JwtAuthenticationToken jwtToken = (JwtAuthenticationToken) authentication;
            String username = (String) jwtToken.getTokenAttributes().get("username");
            return username != null ? username : authentication.getName();
        }
        return authentication.getName();
    }

    /**
     * 获取当前用户ID
     * 依赖于Cloud-Auth在生成Token时注入的 'user_id' 字段
     * @return 用户ID，如果获取失败则返回null
     */
    public Long getUserId() {
        Authentication authentication = getAuthentication();
        if (authentication instanceof JwtAuthenticationToken) {
            JwtAuthenticationToken jwtToken = (JwtAuthenticationToken) authentication;
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
        }
        return null;
    }

    /**
     * 获取当前用户的权限列表 (GrantedAuthority)
     * @return 权限字符串列表
     */
    public List<String> getAuthorities() {
        Authentication authentication = getAuthentication();
        if (authentication == null) {
            return Collections.emptyList();
        }
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
    }

    /**
     * 获取JWT原始Claims信息
     * @return Map<String, Object> Claims
     */
    public Map<String, Object> getClaims() {
        Authentication authentication = getAuthentication();
        if (authentication instanceof JwtAuthenticationToken) {
            return ((JwtAuthenticationToken) authentication).getTokenAttributes();
        }
        return Collections.emptyMap();
    }

    /**
     * 判断当前用户是否已认证
     */
    public boolean isAuthenticated() {
        Authentication authentication = getAuthentication();
        return authentication != null && authentication.isAuthenticated();
    }
}
