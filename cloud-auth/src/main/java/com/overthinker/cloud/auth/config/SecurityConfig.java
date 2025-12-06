package com.overthinker.cloud.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import com.overthinker.cloud.auth.service.UserDetailsServiceImpl;

/**
 * Web安全配置
 * 负责配置Spring Security的Web安全部分，包括表单登录、用户服务和密码编码器等。
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * 配置Web安全过滤器链 (Order 2)
     * 处理表单登录请求和其他非OAuth2协议的Web请求
     */
    @Bean
    @Order(2)
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests((authorize) -> authorize
                // 放行静态资源和登录页
                .requestMatchers("/actuator/**", "/css/**", "/js/**", "/favicon.ico", "/error").permitAll()
                // 其他请求需要认证
                .anyRequest().authenticated()
            )
            // 启用表单登录
            .formLogin(Customizer.withDefaults());

        // 禁用CSRF (API服务通常禁用，或者配置CookieCsrfTokenRepository)
        http.csrf(csrf -> csrf.disable());

        return http.build();
    }

    /**
     * 配置从数据库加载用户的服务
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl(); // 依赖自动注入
    }

    /**
     * 配置密码编码器
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
