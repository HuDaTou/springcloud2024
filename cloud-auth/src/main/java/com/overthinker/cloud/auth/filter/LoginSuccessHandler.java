package com.overthinker.cloud.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.overthinker.cloud.auth.entity.PO.LoginUser;
import com.overthinker.cloud.auth.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * 登录成功处理器
 * 负责处理登录成功后的逻辑：生成 Token、更新登录状态、记录日志等
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class LoginSuccessHandler implements AuthenticationSuccessHandler {
    
    private final JwtEncoder jwtEncoder;
    private final UserService userService;
    private final ObjectMapper objectMapper;
    
    private static final String TOKEN_ISSUER = "http://localhost:9123";
    private static final long TOKEN_EXPIRE_MINUTES = 30;
    
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, 
                                       HttpServletResponse response,
                                       Authentication authentication) throws IOException, ServletException {
        
        try {
            // 1. 获取登录用户信息
            LoginUser loginUser = (LoginUser) authentication.getPrincipal();
            
            // 2. 同步处理：更新登录状态
            updateLoginStatus(loginUser, request);
            
            // 3. 生成 Token
            String token = generateToken(loginUser);
            Instant expireTime = Instant.now().plus(TOKEN_EXPIRE_MINUTES, ChronoUnit.MINUTES);
            
            // 4. 构建响应
            Map<String, Object> result = new HashMap<>();
            result.put("code", 200);
            result.put("msg", "登录成功");
            result.put("token", token);
            result.put("expire", expireTime.toString());
            
            // 5. 异步处理：记录登录日志
            CompletableFuture.runAsync(() -> {
                saveLoginLog(loginUser, request, "成功");
            });
            
            // 6. 发送响应
            sendJsonResponse(response, result);
            
            log.info("用户登录成功: {}", authentication.getName());
            
        } catch (Exception e) {
            log.error("登录成功处理异常", e);
            handleException(response, e);
        }
    }
    
    /**
     * 生成 JWT Token
     */
    private String generateToken(LoginUser loginUser) {
        Instant now = Instant.now();
        
        String authorities = loginUser.getAuthorities().stream()
                .map(auth -> auth.getAuthority())
                .collect(Collectors.joining(","));

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(TOKEN_ISSUER)
                .issuedAt(now)
                .expiresAt(now.plus(TOKEN_EXPIRE_MINUTES, ChronoUnit.MINUTES))
                .subject(loginUser.getUser().getUsername())
                .claim("user_id", loginUser.getUser().getId())
                .claim("username", loginUser.getUser().getUsername())
                .claim("authorities", authorities)
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
    
    /**
     * 更新登录状态
     */
    private void updateLoginStatus(LoginUser loginUser, HttpServletRequest request) {
        String clientIp = getClientIp(request);
        String userAgent = request.getHeader("User-Agent");
        
        // 更新用户的登录信息（最后登录IP、最后登录时间等）
        userService.updateLoginInfo(loginUser.getUser().getId(), clientIp, userAgent);
    }
    
    /**
     * 记录登录日志
     */
    private void saveLoginLog(LoginUser loginUser, HttpServletRequest request, String result) {
        try {
            String clientIp = getClientIp(request);
            String userAgent = request.getHeader("User-Agent");
            
            log.info("登录日志 - 用户: {}, IP: {}, UA: {}, 结果: {}", 
                    loginUser.getUser().getUsername(), clientIp, userAgent, result);
            
            // 实际项目中应该调用 LoginLogService 保存到数据库
            // loginLogService.save(loginUser.getUser().getUsername(), clientIp, userAgent, result);
        } catch (Exception e) {
            log.error("记录登录日志异常", e);
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
     * 发送 JSON 响应
     */
    private void sendJsonResponse(HttpServletResponse response, Map<String, Object> result) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write(objectMapper.writeValueAsString(result));
    }
    
    /**
     * 处理异常
     */
    private void handleException(HttpServletResponse response, Exception e) throws IOException {
        Map<String, Object> errorResult = new HashMap<>();
        errorResult.put("code", 500);
        errorResult.put("msg", "服务器内部错误");
        
        sendJsonResponse(response, errorResult);
    }
}
