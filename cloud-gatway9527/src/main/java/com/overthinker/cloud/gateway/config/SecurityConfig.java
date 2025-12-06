package com.overthinker.cloud.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 * 网关安全配置
 * 负责配置Spring Cloud Gateway作为OAuth2资源服务器。
 * 它会拦截请求，校验JWT Token的有效性，但不对权限进行细粒度控制（权限控制下放到微服务）。
 */
@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
            // 1. 配置路径拦截规则
            .authorizeExchange(exchanges -> exchanges
                // 放行白名单路径 (如登录页、静态资源、认证服务的端点)
                // 注意：/auth/** 需要放行，让用户能访问 cloud-auth 去登录
                .pathMatchers("/actuator/**", "/auth/**", "/login/**", "/oauth2/**").permitAll()
                // 其他所有请求都必须经过认证 (必须带有效Token)
                .anyExchange().authenticated()
            )
            // 2. 配置资源服务器 (JWT校验)
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> {}) // 使用默认的JWT校验逻辑 (验签名、验过期)
            )
            // 3. 禁用CSRF (网关通常作为API入口，无状态，可禁用)
            .csrf(ServerHttpSecurity.CsrfSpec::disable);

        return http.build();
    }
}
