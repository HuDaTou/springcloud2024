package com.overthinker.cloud.system.starter.auth.config;

import com.overthinker.cloud.system.starter.auth.constants.SecurityConst;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.function.Supplier;

/**
 * 内部服务安全表达式委托
 * <p>
 * 包装标准的 MethodSecurityExpressionRoot，拦截 hasAuthority/hasAnyAuthority/hasRole/hasAnyRole 调用。
 * 当请求持有 ROLE_INTERNAL_SERVICE 时，这四个方法恒返回 true，其余方法委托给原始根对象。
 * </p>
 * <p>
 * 由于 Spring Security 6 中 SecurityExpressionRoot 的 hasAuthority 等方法是 final 的，
 * 无法通过继承重写，因此采用委托模式。
 * </p>
 *
 * @author overthinker
 * @since 2025-06-14
 */
public class InternalServiceSecurityExpressionRoot implements MethodSecurityExpressionOperations {

    private final MethodSecurityExpressionOperations delegate;
    private final Supplier<Boolean> internalServiceChecker;

    private Object filterObject;
    private Object returnObject;

    public InternalServiceSecurityExpressionRoot(MethodSecurityExpressionOperations delegate) {
        this.delegate = delegate;
        // 将检查逻辑提取为 Supplier，避免反复遍历 authorities
        this.internalServiceChecker = () -> delegate.getAuthentication().getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(SecurityConst.ROLE_INTERNAL_SERVICE::equals);
    }

    // ========== 被拦截的方法：服务 Token 直接返回 true ==========

    @Override
    public boolean hasAuthority(String authority) {
        return internalServiceChecker.get() || delegate.hasAuthority(authority);
    }

    @Override
    public boolean hasAnyAuthority(String... authorities) {
        return internalServiceChecker.get() || delegate.hasAnyAuthority(authorities);
    }

    @Override
    public boolean hasRole(String role) {
        return internalServiceChecker.get() || delegate.hasRole(role);
    }

    @Override
    public boolean hasAnyRole(String... roles) {
        return internalServiceChecker.get() || delegate.hasAnyRole(roles);
    }

    // ========== 其余方法全部委托 ==========

    @Override
    public Authentication getAuthentication() {
        return delegate.getAuthentication();
    }

    @Override
    public boolean permitAll() {
        return delegate.permitAll();
    }

    @Override
    public boolean denyAll() {
        return delegate.denyAll();
    }

    @Override
    public boolean isAnonymous() {
        return delegate.isAnonymous();
    }

    @Override
    public boolean isAuthenticated() {
        return delegate.isAuthenticated();
    }

    @Override
    public boolean isRememberMe() {
        return delegate.isRememberMe();
    }

    @Override
    public boolean isFullyAuthenticated() {
        return delegate.isFullyAuthenticated();
    }

    @Override
    public boolean hasPermission(Object target, Object permission) {
        return delegate.hasPermission(target, permission);
    }

    @Override
    public boolean hasPermission(Object targetId, String targetType, Object permission) {
        return delegate.hasPermission(targetId, targetType, permission);
    }

    @Override
    public void setFilterObject(Object filterObject) {
        this.filterObject = filterObject;
    }

    @Override
    public Object getFilterObject() {
        return this.filterObject;
    }

    @Override
    public void setReturnObject(Object returnObject) {
        this.returnObject = returnObject;
    }

    @Override
    public Object getReturnObject() {
        return this.returnObject;
    }

    @Override
    public Object getThis() {
        return null;
    }

    // ========== 可通过 SpEL 直接调用的辅助方法 ==========

    /**
     * 由 SpEL 表达式调用：是否为服务 Token
     */
    public boolean isInternal() {
        return internalServiceChecker.get();
    }
}
