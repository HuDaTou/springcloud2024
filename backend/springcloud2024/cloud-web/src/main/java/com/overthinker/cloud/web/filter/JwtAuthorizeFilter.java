package com.overthinker.cloud.web.filter;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.overthinker.cloud.web.entity.PO.LoginUser;
import com.overthinker.cloud.web.utils.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * @author overH
 * <p>
 * 创建时间：2023/10/11 20:32
 */
@Component
@RequiredArgsConstructor
public class JwtAuthorizeFilter extends OncePerRequestFilter {


    private final JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain) throws ServletException, IOException {

        // 提取 Header
        String authorization = request.getHeader("Authorization");
        // 解析jwt
        DecodedJWT jwt = jwtUtils.resolveJwt(authorization);


        if (!ObjectUtils.isEmpty(jwt)) {
            // 获取UserDetails
            LoginUser user = (LoginUser) jwtUtils.toUser(jwt);
            // 创建认证对象
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            // 保存认证详细信息
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            // 验证通过，设置上下文中
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }
}
