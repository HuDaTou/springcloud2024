package com.overthinker.cloud.system.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * 资源服务器安全配置
 * 负责将当前服务配置为OAuth2资源服务器，校验传入的JWT Token，并从Token中提取用户权限。
 * 同时启用方法级别的安全注解，如@PreAuthorize。
 */
@Configuration
@EnableMethodSecurity // 启用方法级别的安全注解，如@PreAuthorize
@EnableWebSecurity // <--- 必须有这个注解，它会触发 HttpSecurity 的创建
public class ResourceServerConfig {

    /**
     * 配置资源服务器的安全过滤器链。
     * @param http HttpSecurity配置对象
     * @return SecurityFilterChain实例
     * @throws Exception 配置异常
     */
    @Bean
    public SecurityFilterChain resourceServerSecurityFilterChain(HttpSecurity http) throws Exception {
        http
            // 禁用CSRF，因为JWT是无状态的，不需要CSRF保护
            .csrf(AbstractHttpConfigurer::disable)
            // 配置授权规则
            .authorizeHttpRequests(authorizeRequests ->
                authorizeRequests
                    // 允许未认证访问健康检查、Swagger文档等公共路径
                    .requestMatchers("/actuator/**", "/swagger-ui/**", "/v3/api-docs/**", "/login").permitAll()
                    // 其他所有请求都需要认证
                    .anyRequest().authenticated()
            )
            // 配置OAuth2资源服务器，处理JWT Token
            .oauth2ResourceServer(oauth2ResourceServer ->
                oauth2ResourceServer.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
            );

        return http.build();
    }

    /**
     * 配置JWT认证转换器。
     * 负责从JWT Token中提取用户的权限（Granted Authorities）。
     * 默认情况下，Spring Security从"scope"或"scp"声明中提取权限。
     * 如果你的Token中的权限声明在其他字段（如"authorities"），需要自定义此转换器。
     * @return JwtAuthenticationConverter实例
     */
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        // 设置从JWT的"authorities"声明中提取权限，而不是默认的"scope"或"scp"
        grantedAuthoritiesConverter.setAuthoritiesClaimName("authorities");
        // 移除默认的"SCOPE_"前缀，如果你的权限字符串没有这个前缀
        grantedAuthoritiesConverter.setAuthorityPrefix("");

        // 创建自定义的JWT认证转换器
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter); // 使用自定义的权限转换器
        return jwtAuthenticationConverter;
    }
}
