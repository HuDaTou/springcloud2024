package com.overthinker.cloud.auth.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.overthinker.cloud.auth.filter.JsonUsernamePasswordAuthenticationFilter;
import com.overthinker.cloud.auth.filter.LoginFailureHandler;
import com.overthinker.cloud.auth.filter.LoginSuccessHandler;
import com.overthinker.cloud.system.starter.auth.config.InternalServiceMethodSecurityExpressionHandler;
import com.overthinker.cloud.system.starter.auth.constants.SecurityConst;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.authorization.AuthorizationDecision;
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
 * 2. URL 级别：白名单路径无需认证
 * 3. URL 级别：其余路径需有效 JWT
 * 4. 方法级别：自定义表达式处理器，服务 Token 绕过所有 @PreAuthorize 检查
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

    /**
     * 服务 Token 全放行策略
     * 若请求持有 ROLE_INTERNAL_SERVICE，直接授权；否则返回 null 交由后续规则判断
     */
    private AuthorizationManager<RequestAuthorizationContext> internalServiceBypass() {
        return (authentication, context) -> {
            if (authentication.get() != null && authentication.get().getAuthorities().stream()
                    .anyMatch(a -> SecurityConst.ROLE_INTERNAL_SERVICE.equals(a.getAuthority()))) {
                return new AuthorizationDecision(true);
            }
            return null;
        };
    }

    /**
     * 自定义方法安全表达式处理器
     * 使持有 ROLE_INTERNAL_SERVICE 的服务 Token 绕过所有 @PreAuthorize 权限检查
     */
    @Bean
    static MethodSecurityExpressionHandler methodSecurityExpressionHandler() {
        return new InternalServiceMethodSecurityExpressionHandler();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests((authorize) -> authorize
                // 第一优先级：服务 Token 内部调用完全放行
                .requestMatchers("/**").access(internalServiceBypass())
                // 公开白名单
                .requestMatchers("/actuator/**", "/css/**", "/js/**", "/favicon.ico", "/error").permitAll()
                .requestMatchers("/auth/register", "/auth/send-code", "/auth/captcha", "/auth/login", "/auth/logout", "/Email/send-code").permitAll()
                // 其余需认证
                .anyRequest().authenticated()
            )
            .oauth2ResourceServer(oauth2ResourceServer ->
                oauth2ResourceServer.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter))
            )
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
