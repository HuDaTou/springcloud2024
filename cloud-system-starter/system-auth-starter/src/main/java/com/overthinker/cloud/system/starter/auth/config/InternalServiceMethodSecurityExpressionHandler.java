package com.overthinker.cloud.system.starter.auth.config;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;

/**
 * 内部服务方法安全表达式处理器
 * <p>
 * 替代默认的 DefaultMethodSecurityExpressionHandler。
 * 先创建标准的 SecurityExpressionRoot，再用 InternalServiceSecurityExpressionRoot 包装，
 * 使得持有 ROLE_INTERNAL_SERVICE 的服务 Token 可以绕过所有 @PreAuthorize 权限检查。
 * </p>
 *
 * @author overthinker
 * @since 2025-06-14
 */
public class InternalServiceMethodSecurityExpressionHandler
        extends DefaultMethodSecurityExpressionHandler {

    @Override
    protected MethodSecurityExpressionOperations createSecurityExpressionRoot(
            Authentication authentication, MethodInvocation invocation) {
        MethodSecurityExpressionOperations standardRoot =
                super.createSecurityExpressionRoot(authentication, invocation);
        return new InternalServiceSecurityExpressionRoot(standardRoot);
    }
}
