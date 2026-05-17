package com.overthinker.cloud.auth.config;

import com.overthinker.cloud.auth.filter.CaptchaAuthenticationFilter;
import com.overthinker.cloud.auth.filter.LoginFailureHandler;
import com.overthinker.cloud.auth.filter.LoginSuccessHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Web安全配置
 * 负责配置Spring Security的Web安全部分，包括表单登录、用户服务和密码编码器等。
 * 与 JweAuthorizationServerConfig 配合使用，处理非 OAuth2 端点的安全。
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    
    private final CaptchaAuthenticationFilter captchaAuthenticationFilter;
    private final LoginSuccessHandler loginSuccessHandler;
    private final LoginFailureHandler loginFailureHandler;

    /**
     * 配置Web安全过滤器链 (Order 2)
     * 处理表单登录请求和其他非OAuth2协议的Web请求
     */
    @Bean
    @Order(2)
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
            // 1. 配置端点权限
            .authorizeHttpRequests((authorize) -> authorize
                .requestMatchers("/actuator/**", "/css/**", "/js/**", "/favicon.ico", "/error", "/internal/**").permitAll()
                .requestMatchers("/oauth2/jwks", "/oauth2/token", "/oauth2/authorize", "/oauth2/logout").permitAll()
                .requestMatchers("/auth/register", "/auth/send-code", "/auth/captcha").permitAll()
                .anyRequest().authenticated()
            )
            // 2. 添加验证码校验过滤器（在用户名密码认证之前）
            .addFilterBefore(captchaAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            // 3. 表单登录配置
            .formLogin(form -> form
                .loginProcessingUrl("/auth/login")
                .usernameParameter("username")
                .passwordParameter("password")
                .successHandler(loginSuccessHandler)
                .failureHandler(loginFailureHandler)
            )
            // 4. 退出登录配置
            .logout(logout -> logout
                .logoutUrl("/auth/logout")
                .logoutSuccessHandler((request, response, authentication) -> {
                    response.setContentType("application/json;charset=UTF-8");
                    response.getWriter().write("{\"code\":200,\"msg\":\"退出成功\"}");
                    log.info("用户退出登录: {}", authentication != null ? authentication.getName() : "unknown");
                })
            )
            // 5. 禁用 CSRF
            .csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }

    /**
     * 配置密码编码器
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
