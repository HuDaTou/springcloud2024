package com.overthinker.cloud.auth.token;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenClaimsContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

/**
 * 自定义OAuth2 Token的生成逻辑。
 * 可以在这里向JWT中添加额外的用户信息、权限、角色等声明。
 * TODO: 后续实现具体的Token增强逻辑。
 */
@Slf4j
public class CustomOAuth2TokenCustomizer implements OAuth2TokenCustomizer<OAuth2TokenClaimsContext> {
    @Override
    public void customize(OAuth2TokenClaimsContext context) {
        log.debug("Customizing OAuth2 Token for: {}", context.getPrincipal().getName());
        // 可以在此处添加自定义声明到Token中
        // 例如：
        // context.getClaims().claim("user_id", "someUserId");
        // context.getClaims().claim("roles", "someRoles");
    }
}
