package com.overthinker.cloud.system.auth.webflux.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverter;
import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 * WebFlux 资源服务器安全配置
 * 负责将当前服务配置为OAuth2资源服务器，校验传入的JWT Token，并从Token中提取用户权限。
 * 同时启用方法级别的安全注解，如@PreAuthorize。
 */
@Configuration
@EnableReactiveMethodSecurity // 启用响应式方法级别的安全注解
@EnableWebFluxSecurity // 启用WebFlux安全
public class ResourceServerConfig {

    /**
     * 配置资源服务器的安全过滤器链。
     * @param http ServerHttpSecurity配置对象
     * @return SecurityWebFilterChain实例
     */
    @Bean
    public SecurityWebFilterChain resourceServerSecurityFilterChain(ServerHttpSecurity http) {
        http
            // 禁用CSRF，因为JWT是无状态的，不需要CSRF保护
            .csrf(csrf -> csrf.disable())
            // 配置授权规则
            .authorizeExchange(exchanges ->
                exchanges
                    // 允许未认证访问健康检查、Swagger文档等公共路径
                    .pathMatchers("/actuator/**", "/swagger-ui/**", "/v3/api-docs/**", "/login").permitAll()
                    // 其他所有请求都需要认证
                    .anyExchange().authenticated()
            )
            // 配置OAuth2资源服务器，处理JWT Token
            .oauth2ResourceServer(oauth2 ->
                oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
            );

        return http.build();
    }

    /**
     * 配置JWT认证转换器。
     * 负责从JWT Token中提取用户的权限（Granted Authorities）。
     * @return ReactiveJwtAuthenticationConverter实例
     */
    @Bean
    public ReactiveJwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        // 设置从JWT的"authorities"声明中提取权限，而不是默认的"scope"或"scp"
        grantedAuthoritiesConverter.setAuthoritiesClaimName("authorities");
        // 移除默认的"SCOPE_"前缀，如果你的权限字符串没有这个前缀
        grantedAuthoritiesConverter.setAuthorityPrefix("");

        // 创建响应式的JWT认证转换器
        ReactiveJwtAuthenticationConverter jwtAuthenticationConverter = new ReactiveJwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }
}