package com.overthinker.cloud.system.starter.auth.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;


/**
 * 资源服务器安全配置
 * 负责将当前服务配置为OAuth2资源服务器，校验传入的JWT Token，并从Token中提取用户权限。
 * 同时启用方法级别的安全注解，如@PreAuthorize。
 *
 * 条件装配：仅当项目中没有自定义 SecurityFilterChain 时才自动配置
 * 这样 auth 服务（有自己的 SecurityConfig）不会被覆盖，其他服务会自动生效
 */
@Configuration
@EnableMethodSecurity
@EnableWebSecurity
@ConditionalOnMissingBean(name = "defaultSecurityFilterChain")
public class ResourceServerConfig {

    private final JwtAuthenticationConverter jwtAuthenticationConverter;

    public ResourceServerConfig(JwtAuthenticationConverter jwtAuthenticationConverter) {
        this.jwtAuthenticationConverter = jwtAuthenticationConverter;
    }

    @Bean
    public SecurityFilterChain resourceServerSecurityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(authorizeRequests ->
                authorizeRequests
                    .requestMatchers("/actuator/**", "/swagger-ui/**", "/v3/api-docs/**", "/login").permitAll()
                    .anyRequest().authenticated()
            )
            .oauth2ResourceServer(oauth2ResourceServer ->
                oauth2ResourceServer.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter))
            );

        return http.build();
    }
}
