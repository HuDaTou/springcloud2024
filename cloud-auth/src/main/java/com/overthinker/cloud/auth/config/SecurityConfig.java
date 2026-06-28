package com.overthinker.cloud.auth.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.overthinker.cloud.auth.filter.JsonUsernamePasswordAuthenticationFilter;
import com.overthinker.cloud.auth.filter.LoginFailureHandler;
import com.overthinker.cloud.auth.filter.LoginSuccessHandler;
import com.overthinker.cloud.system.starter.auth.config.InternalServiceAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * 安全配置（Order 2）
 * <p>
 * 负责非 OAuth2 协议端点的安全校验和鉴权：
 * 1. URL 级别：服务 Token（ROLE_INTERNAL_SERVICE）第一优先级完全放行
 *    通过注入的 internalServiceBypassAuthorizationManager 实现（由 system-auth-starter 提供）
 * 2. URL 级别：白名单路径无需认证
 * 3. URL 级别：其余路径需有效 JWT
 * </p>
 */
@Slf4j
@Configuration
//@EnableMethodSecurity  // 临时关闭方法级权限校验
@RequiredArgsConstructor
public class SecurityConfig {

    private final LoginSuccessHandler loginSuccessHandler;
    private final LoginFailureHandler loginFailureHandler;
    private final ObjectMapper objectMapper;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final JwtAuthenticationConverter jwtAuthenticationConverter;
    private final InternalServiceAuthenticationFilter internalServiceAuthenticationFilter;
    private final AuthorizationManager<RequestAuthorizationContext> internalServiceBypassAuthorizationManager;


    /**
     * JSON 登录接口认证过滤器
     * @return
     * @throws Exception
     */
    @Bean
    public JsonUsernamePasswordAuthenticationFilter jsonAuthenticationFilter() throws Exception {
        JsonUsernamePasswordAuthenticationFilter filter =
                new JsonUsernamePasswordAuthenticationFilter(objectMapper);
        filter.setFilterProcessesUrl("/auth/login");
        filter.setUsernameParameter("username");
        filter.setPasswordParameter("password");
        filter.setAuthenticationManager(authenticationConfiguration.getAuthenticationManager());
        filter.setAuthenticationSuccessHandler(loginSuccessHandler);
        filter.setAuthenticationFailureHandler(loginFailureHandler);
        return filter;
    }

    @Bean
    @Order(2)
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests((authorize) -> authorize
                // 第一优先级：服务 Token 内部调用完全放行
                .requestMatchers("/**").access(internalServiceBypassAuthorizationManager)
                // 公开白名单
                .requestMatchers("/actuator/**", "/css/**", "/js/**", "/favicon.ico", "/error").permitAll()
                .requestMatchers("/auth/register", "/auth/captcha", "/auth/login", "/auth/logout", "/auth/reset-confirm", "/auth/reset-password", "/email/send-code").permitAll()
                // 其余需认证
                .anyRequest().authenticated()
            )
            .oauth2ResourceServer(oauth2ResourceServer ->
                oauth2ResourceServer.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter))
            )
            // 添加内部服务认证过滤器，在 JWT 认证之前执行
            .addFilterBefore(internalServiceAuthenticationFilter, org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class)
            .addFilterAt(jsonAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
            .logout(logout -> logout
                .logoutUrl("/auth/logout")
                .logoutSuccessHandler((request, response, authentication) -> {
                    response.setContentType("application/json;charset=UTF-8");
                    response.getWriter().write("{\"code\":200,\"msg\":\"退出成功\"}");
                    log.info("用户退出登录：{}", authentication != null ? authentication.getName() : "未知");
                })
            )
            .csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }
}
