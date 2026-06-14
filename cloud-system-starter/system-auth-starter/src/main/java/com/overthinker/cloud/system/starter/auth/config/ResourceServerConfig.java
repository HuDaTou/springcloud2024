package com.overthinker.cloud.system.starter.auth.config;

import com.overthinker.cloud.system.starter.auth.constants.SecurityConst;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;


/**
 * 资源服务器安全配置
 * <p>
 * 负责将当前服务配置为OAuth2资源服务器，校验传入的JWT Token，并从Token中提取用户权限。
 * 同时启用方法级别的安全注解，如@PreAuthorize。
 * </p>
 * <p>
 * 支持内部服务调用绕过：
 * 1. URL 级别：持有 ROLE_INTERNAL_SERVICE 的请求完全放行
 * 2. 方法级别：通过 InternalServiceMethodSecurityExpressionHandler 使 @PreAuthorize 检查对内部服务放行
 * </p>
 * <p>
 * 条件装配：仅当项目中没有自定义 SecurityFilterChain 时才自动配置
 * 这样 auth 服务（有自己的 SecurityConfig）不会被覆盖，其他服务会自动生效
 * </p>
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
    public SecurityFilterChain resourceServerSecurityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(authorizeRequests ->
                authorizeRequests
                    // 第一优先级：服务 Token 内部调用完全放行
                    .requestMatchers("/**").access(internalServiceBypass())
                    // 公开白名单
                    .requestMatchers("/actuator/**", "/swagger-ui/**", "/v3/api-docs/**", "/login").permitAll()
                    // 其余需认证
                    .anyRequest().authenticated()
            )
            .oauth2ResourceServer(oauth2ResourceServer ->
                oauth2ResourceServer.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter))
            );

        return http.build();
    }
}
