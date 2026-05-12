package com.overthinker.cloud.auth.config;

import com.overthinker.cloud.auth.service.UserService;
import com.overthinker.cloud.auth.service.impl.UserServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Web安全配置
 * 负责配置Spring Security的Web安全部分，包括表单登录、用户服务和密码编码器等。
 * 与 JweAuthorizationServerConfig 配合使用，处理非 OAuth2 端点的安全。
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
                .requestMatchers("/actuator/**", "/css/**", "/js/**", "/favicon.ico", "/error", "/internal/**").permitAll()
                // 放行 OAuth2 相关的公开端点
                .requestMatchers("/oauth2/jwks", "/oauth2/token", "/oauth2/authorize", "/oauth2/logout").permitAll()
                // 放行注册和验证码相关接口
                .requestMatchers("/auth/register", "/auth/send-code").permitAll()
                // 其他请求需要认证
                .anyRequest().authenticated()
            )
            // 启用表单登录
            .formLogin(form -> form
                .loginProcessingUrl("/auth/login")
                // 成功后的处理：返回 JSON
                .successHandler((request, response, authentication) -> {
                    response.setContentType("application/json;charset=UTF-8");
                    response.getWriter().write("{\"code\":200,\"msg\":\"登录成功\"}");
                })
                // 失败后的处理：返回 JSON
                .failureHandler((request, response, exception) -> {
                    response.setStatus(401);
                    response.setContentType("application/json;charset=UTF-8");
                    response.getWriter().write("{\"code\":401,\"msg\":\"用户名或密码错误\"}");
                })
            )
            // 配置退出登录
            .logout(logout -> logout
                .logoutUrl("/auth/logout")
                .logoutSuccessHandler((request, response, authentication) -> {
                    response.setContentType("application/json;charset=UTF-8");
                    response.getWriter().write("{\"code\":200,\"msg\":\"退出成功\"}");
                })
            );

        // 禁用CSRF (API服务通常禁用)
        http.csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }

    /**
     * 配置从数据库加载用户的服务
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return new UserServiceImpl();
    }

    /**
     * 配置密码编码器
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
