package com.overthinker.cloud.system.starter.auth.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * 内部服务认证对象
 * <p>
 * 用于表示内部服务调用的认证信息，持有 ROLE_INTERNAL_SERVICE 权限。
 * 由 {@link InternalServiceAuthenticationFilter} 创建并放入 SecurityContext。
 * </p>
 *
 * @author overthinker
 * @since 2025-06-21
 */
public class InternalServiceAuthentication implements Authentication {

    private final Collection<GrantedAuthority> authorities;
    private boolean authenticated = true;

    public InternalServiceAuthentication(Collection<GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return "internal-service";
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.authenticated = isAuthenticated;
    }

    @Override
    public String getName() {
        return "internal-service";
    }
}
