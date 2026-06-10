package com.overthinker.cloud.auth.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.overthinker.cloud.auth.filter.JsonUsernamePasswordAuthenticationFilter;
import com.overthinker.cloud.auth.filter.LoginFailureHandler;
import com.overthinker.cloud.auth.filter.LoginSuccessHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final LoginSuccessHandler loginSuccessHandler;
    private final LoginFailureHandler loginFailureHandler;
    private final ObjectMapper objectMapper;
    private final AuthenticationConfiguration authenticationConfiguration;

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
                .requestMatchers("/actuator/**", "/css/**", "/js/**", "/favicon.ico", "/error", "/internal/**").permitAll()
                .requestMatchers("/oauth2/jwks", "/oauth2/token", "/oauth2/authorize", "/oauth2/logout").permitAll()
                .requestMatchers("/auth/register", "/auth/send-code", "/auth/captcha", "/auth/login", "/auth/logout", "/Email/send-code").permitAll()
                .anyRequest().authenticated()
            )
            .addFilterAt(jsonAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
            .logout(logout -> logout
                .logoutUrl("/auth/logout")
                .logoutSuccessHandler((request, response, authentication) -> {
                    response.setContentType("application/json;charset=UTF-8");
                    response.getWriter().write("{\"code\":200,\"msg\":\"退出成功\"}");
                    log.info("用户退出登录: {}", authentication != null ? authentication.getName() : "unknown");
                })
            )
            .csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }
}
