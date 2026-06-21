package com.overthinker.cloud.system.starter.auth.config;

import com.overthinker.cloud.api.config.InternalServiceProperties;
import com.overthinker.cloud.system.starter.auth.constants.SecurityConst;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * 内部服务认证过滤器
 * <p>
 * 检测请求头中的内部服务标识（X-Internal-Service 和 X-Service-Key），
 * 如果验证成功，则创建具有 ROLE_INTERNAL_SERVICE 权限的认证对象，
 * 配合 {@link InternalServiceSecurityExpressionRoot} 和
 * {@code internalServiceBypassAuthorizationManager} 进行放行。
 * </p>
 *
 * @author overthinker
 * @since 2025-06-21
 */
@Slf4j
public class InternalServiceAuthenticationFilter extends OncePerRequestFilter {

    /** 内部服务调用标识头 */
    private static final String INTERNAL_SERVICE_HEADER = "X-Internal-Service";

    /** 服务密钥头 */
    private static final String SERVICE_KEY_HEADER = "X-Service-Key";

    private final String expectedServiceKey;

    public InternalServiceAuthenticationFilter(InternalServiceProperties properties) {
        this.expectedServiceKey = properties.getServiceKey();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String internalService = request.getHeader(INTERNAL_SERVICE_HEADER);
        String serviceKey = request.getHeader(SERVICE_KEY_HEADER);

        // 如果请求头包含内部服务标识，验证密钥并创建认证对象
        if ("true".equals(internalService)) {
            if (expectedServiceKey.equals(serviceKey)) {
                // 创建具有 ROLE_INTERNAL_SERVICE 权限的认证对象
                InternalServiceAuthentication authentication = new InternalServiceAuthentication(
                        List.of(new SimpleGrantedAuthority(SecurityConst.ROLE_INTERNAL_SERVICE))
                );
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.debug("内部服务调用认证成功: {}", request.getRequestURI());
            } else {
                log.warn("内部服务调用认证失败: 无效的服务密钥");
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.getWriter().write("{\"code\":403,\"msg\":\"无效的服务密钥\"}");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
