package com.overthinker.cloud.auth.filter;

import com.overthinker.cloud.auth.service.CaptchaService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * 验证码校验过滤器
 * 在 UsernamePasswordAuthenticationFilter 之前执行，校验用户提交的验证码
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CaptchaAuthenticationFilter extends OncePerRequestFilter {
    
    private final CaptchaService captchaService;
    
    private static final String LOGIN_URL = "/auth/login";
    
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, 
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) 
            throws ServletException, IOException {
        
        // 只处理登录请求
        if (isLoginRequest(request)) {
            try {
                validateCaptcha(request);
            } catch (AuthenticationException e) {
                log.warn("验证码校验失败: {}", e.getMessage());
                // 注意：这里不直接处理失败，而是抛出异常让 Security 处理
                // 或者可以自定义失败处理
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write(String.format("{\"code\":401,\"msg\":\"%s\"}", e.getMessage()));
                return;
            }
        }
        
        filterChain.doFilter(request, response);
    }
    
    /**
     * 判断是否为登录请求
     */
    private boolean isLoginRequest(HttpServletRequest request) {
        return LOGIN_URL.equals(request.getRequestURI()) && "POST".equalsIgnoreCase(request.getMethod());
    }
    
    /**
     * 校验验证码
     */
    private void validateCaptcha(HttpServletRequest request) throws AuthenticationException {
        String captcha = request.getParameter("captcha");
        String captchaKey = request.getParameter("captchaKey");
        
        // 如果没有提交验证码参数，跳过校验（验证码可选）
        if (captcha == null || captcha.trim().isEmpty()) {
            log.debug("未提交验证码参数，跳过验证码校验");
            return;
        }
        
        // 校验验证码
        if (!captchaService.validate(captchaKey, captcha)) {
            throw new org.springframework.security.core.AuthenticationException("验证码错误") {};
        }
        
        log.debug("验证码校验成功");
    }
}
