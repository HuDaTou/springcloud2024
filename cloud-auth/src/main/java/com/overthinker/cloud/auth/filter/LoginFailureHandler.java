package com.overthinker.cloud.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * 登录失败处理器
 * 负责处理登录失败后的逻辑：记录失败日志、返回统一错误响应等
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class LoginFailureHandler implements AuthenticationFailureHandler {
    
    private final ObjectMapper objectMapper;
    
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, 
                                       HttpServletResponse response,
                                       AuthenticationException exception) throws IOException, ServletException {
        
        try {
            // 1. 获取异常信息
            String errorMessage = getErrorMessage(exception);
            
            // 2. 获取用户名（即使认证失败，用户名也可能被提取）
            String username = request.getParameter("username");
            
            // 3. 异步处理：记录登录失败日志
            CompletableFuture.runAsync(() -> {
                saveLoginFailureLog(username, request, exception);
            });
            
            // 4. 发送错误响应
            sendErrorResponse(response, errorMessage);
            
            log.warn("用户登录失败: {}, 原因: {}", username, exception.getMessage());
            
        } catch (Exception e) {
            log.error("登录失败处理异常", e);
            handleException(response, e);
        }
    }
    
    /**
     * 获取友好的错误消息
     */
    private String getErrorMessage(AuthenticationException exception) {
        String exceptionName = exception.getClass().getSimpleName();
        
        return switch (exceptionName) {
            case "UsernameNotFoundException" -> "用户不存在或已被禁用";
            case "BadCredentialsException" -> "验证码错误或用户名密码错误";
            case "AccountExpiredException" -> "账户已过期";
            case "CredentialsExpiredException" -> "凭证已过期，请修改密码";
            case "DisabledException" -> "账户已被禁用";
            case "LockedException" -> "账户已被锁定";
            case "AccountStatusException" -> "账户状态异常";
            default -> exception.getMessage() != null ? exception.getMessage() : "登录失败";
        };
    }
    
    /**
     * 记录登录失败日志
     */
    private void saveLoginFailureLog(String username, HttpServletRequest request, 
                                    AuthenticationException exception) {
        try {
            String clientIp = getClientIp(request);
            String userAgent = request.getHeader("User-Agent");
            String errorType = exception.getClass().getSimpleName();
            
            log.info("登录失败日志 - 用户: {}, IP: {}, UA: {}, 错误类型: {}, 错误信息: {}", 
                    username, clientIp, userAgent, errorType, exception.getMessage());
            
            // 实际项目中应该调用 LoginLogService 保存到数据库
            // loginLogService.saveFailure(username, clientIp, userAgent, errorType, exception.getMessage());
            
        } catch (Exception e) {
            log.error("记录登录失败日志异常", e);
        }
    }
    
    /**
     * 获取客户端 IP
     */
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // 如果是多个 IP，取第一个
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }
    
    /**
     * 发送错误响应
     */
    private void sendErrorResponse(HttpServletResponse response, String errorMessage) throws IOException {
        Map<String, Object> errorResult = new HashMap<>();
        errorResult.put("code", 401);
        errorResult.put("msg", errorMessage);
        
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(objectMapper.writeValueAsString(errorResult));
    }
    
    /**
     * 处理异常
     */
    private void handleException(HttpServletResponse response, Exception e) throws IOException {
        Map<String, Object> errorResult = new HashMap<>();
        errorResult.put("code", 500);
        errorResult.put("msg", "服务器内部错误");
        
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        response.getWriter().write(objectMapper.writeValueAsString(errorResult));
    }
}
