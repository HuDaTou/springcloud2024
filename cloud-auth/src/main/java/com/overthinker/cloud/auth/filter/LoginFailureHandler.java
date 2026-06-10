package com.overthinker.cloud.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.overthinker.cloud.common.core.exception.BusinessException;
import com.overthinker.cloud.common.core.resp.ResultData;
import com.overthinker.cloud.common.core.resp.ReturnCodeEnum;
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
            String username = request.getParameter("username");

            BusinessException businessException = mapToBusinessException(exception);

            CompletableFuture.runAsync(() -> {
                saveLoginFailureLog(username, request, exception);
            });

            sendErrorResponse(response, businessException);

            log.warn("用户登录失败: {}, 原因: {}", username, businessException.getMessage());

        } catch (Exception e) {
            log.error("登录失败处理异常", e);
            handleException(response, e);
        }
    }

    /**
     * 将 Spring Security 认证异常映射为 BusinessException
     */
    private BusinessException mapToBusinessException(AuthenticationException exception) {
        String exceptionName = exception.getClass().getSimpleName();

        return switch (exceptionName) {
            case "UsernameNotFoundException" ->
                    new BusinessException(ReturnCodeEnum.USERNAME_OR_PASSWORD_ERROR, "用户不存在或已被禁用");
            case "BadCredentialsException" ->
                    new BusinessException(ReturnCodeEnum.USERNAME_OR_PASSWORD_ERROR, "用户名密码错误");
            case "AccountExpiredException" ->
                    new BusinessException(ReturnCodeEnum.USERNAME_OR_PASSWORD_ERROR, "账户已过期");
            case "CredentialsExpiredException" ->
                    new BusinessException(ReturnCodeEnum.USERNAME_OR_PASSWORD_ERROR, "凭证已过期，请修改密码");
            case "DisabledException" ->
                    new BusinessException(ReturnCodeEnum.BLACK_LIST_ERROR, "账户已被禁用");
            case "LockedException" ->
                    new BusinessException(ReturnCodeEnum.BLACK_LIST_ERROR, "账户已被锁定");
            case "AccountStatusException" ->
                    new BusinessException(ReturnCodeEnum.BLACK_LIST_ERROR, "账户状态异常");
            default ->
                    new BusinessException(ReturnCodeEnum.USERNAME_OR_PASSWORD_ERROR,
                            exception.getMessage() != null ? exception.getMessage() : "登录失败");
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
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }

    /**
     * 发送错误响应
     */
    private void sendErrorResponse(HttpServletResponse response, BusinessException e) throws IOException {
        ResultData<Object> resultData = ResultData.failure(e.getReturnCodeEnum(), e.getMessage());

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write(objectMapper.writeValueAsString(resultData));
    }

    /**
     * 处理异常
     */
    private void handleException(HttpServletResponse response, Exception e) throws IOException {
        ResultData<Object> resultData = ResultData.failure(ReturnCodeEnum.INTERNAL_SERVER_ERROR, "服务器内部错误");

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write(objectMapper.writeValueAsString(resultData));
    }
}
