package com.overthinker.cloud.system.starter.auth.config;

import com.overthinker.cloud.api.config.InternalServiceProperties;
import com.overthinker.cloud.system.starter.auth.constants.SecurityConst;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

/**
 * 内部服务认证自动配置
 * <p>
 * 提供内部服务调用的通用组件，无论 {@link ResourceServerConfig} 是否加载都会生效。
 * 适用于所有需要支持内部 Feign 调用绕过的微服务（包括 cloud-auth）。
 * </p>
 * <p>
 * 提供的 Bean：
 * <ul>
 *   <li>{@link InternalServiceAuthenticationFilter} — 请求头验证过滤器</li>
 *   <li>{@code internalServiceBypassAuthorizationManager} — URL 级别绕过策略</li>
 * </ul>
 * </p>
 *
 * @author overthinker
 * @since 2025-06-22
 */
@AutoConfiguration
@EnableConfigurationProperties(InternalServiceProperties.class)
public class InternalServiceAuthAutoConfiguration {

    /**
     * 内部服务认证过滤器
     * <p>
     * 检测 X-Internal-Service 和 X-Service-Key 请求头，
     * 验证通过后创建 ROLE_INTERNAL_SERVICE 认证对象放入 SecurityContext。
     * </p>
     */
    @Bean
    @ConditionalOnMissingBean
    public InternalServiceAuthenticationFilter internalServiceAuthenticationFilter(
            InternalServiceProperties properties) {
        return new InternalServiceAuthenticationFilter(properties);
    }

    /**
     * 内部服务 URL 级别绕过策略
     * <p>
     * 若请求持有 ROLE_INTERNAL_SERVICE 权限，直接授权；
     * 否则返回 null 交由后续规则判断。
     * </p>
     */
    @Bean
    @ConditionalOnMissingBean(name = "internalServiceBypassAuthorizationManager")
    public AuthorizationManager<RequestAuthorizationContext> internalServiceBypassAuthorizationManager() {
        return (authentication, context) -> {
            if (authentication.get() != null && authentication.get().getAuthorities().stream()
                    .anyMatch(a -> SecurityConst.ROLE_INTERNAL_SERVICE.equals(a.getAuthority()))) {
                return new AuthorizationDecision(true);
            }
            return null;
        };
    }
}
