package com.overthinker.cloud.auth.token;

import com.overthinker.cloud.auth.entity.PO.LoginUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 自定义OAuth2 Token的生成逻辑。
 * 在颁发的JWT Token中注入用户的User ID和权限列表(Authorities)。
 * 下游微服务可以直接从Token中获取这些信息，无需再次查询数据库。
 */
@Slf4j
public class CustomOAuth2TokenCustomizer implements OAuth2TokenCustomizer<JwtEncodingContext> {

    @Override
    public void customize(JwtEncodingContext context) {
        log.debug("Customizing OAuth2 Token for: {}", context.getPrincipal().getName());
        
        // 获取用户信息
        Object principal = context.getPrincipal().getPrincipal();
        if (principal instanceof LoginUser loginUser) {
            // 注入用户ID
            context.getClaims().claim("user_id", loginUser.getUser().getId());
            
            // 注入用户名
            context.getClaims().claim("username", loginUser.getUser().getUsername());
            
            // 注入权限列表
            List<String> authorities = loginUser.getAuthorities().stream()
                    .map(auth -> auth.getAuthority())
                    .collect(Collectors.toList());
            context.getClaims().claim("authorities", authorities);
            
            log.info("Token enhanced for user: {}, user_id: {}", 
                    loginUser.getUser().getUsername(), 
                    loginUser.getUser().getId());
        }
    }
}
