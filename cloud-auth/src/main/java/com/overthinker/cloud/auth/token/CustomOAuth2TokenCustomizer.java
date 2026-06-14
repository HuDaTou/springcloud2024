package com.overthinker.cloud.auth.token;

import com.overthinker.cloud.auth.entity.PO.LoginUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 自定义 OAuth2 Token 生成逻辑
 * <p>
 * 在颁发的 JWT Token 中注入业务声明：
 * <ul>
 *   <li>用户 Token（authorization_code）：注入 user_id、username、authorities</li>
 *   <li>服务间 Token（client_credentials）：注入 service、authorities（ROLE_INTERNAL_SERVICE）</li>
 * </ul>
 * 下游微服务可直接从 Token 中获取这些信息，无需再次查询数据库
 * </p>
 */
@Slf4j
public class CustomOAuth2TokenCustomizer implements OAuth2TokenCustomizer<JwtEncodingContext> {

    private static final String ROLE_INTERNAL_SERVICE = "ROLE_INTERNAL_SERVICE";

    @Override
    public void customize(JwtEncodingContext context) {
        AuthorizationGrantType grantType = context.getAuthorizationGrantType();

        if (AuthorizationGrantType.CLIENT_CREDENTIALS.equals(grantType)) {
            customizeClientCredentialsToken(context);
        } else {
            customizeUserToken(context);
        }
    }

    /**
     * 为用户 Token 注入 user_id、username、authorities
     */
    private void customizeUserToken(JwtEncodingContext context) {
        Object principal = context.getPrincipal().getPrincipal();
        if (principal instanceof LoginUser loginUser) {
            context.getClaims().claim("user_id", loginUser.getUser().getId());
            context.getClaims().claim("username", loginUser.getUser().getUsername());

            List<String> authorities = loginUser.getAuthorities().stream()
                    .map(auth -> auth.getAuthority())
                    .collect(Collectors.toList());
            context.getClaims().claim("authorities", authorities);

            log.info("为用户签发 Token：{}, user_id：{}",
                    loginUser.getUser().getUsername(),
                    loginUser.getUser().getId());
        }
    }

    /**
     * 为服务间调用的 client_credentials Token 注入 service 和 ROLE_INTERNAL_SERVICE
     */
    private void customizeClientCredentialsToken(JwtEncodingContext context) {
        String clientId = context.getRegisteredClient().getClientId();
        context.getClaims().claim("service", clientId);
        context.getClaims().claim("authorities", List.of(ROLE_INTERNAL_SERVICE));

        log.info("为内部服务签发 Token，客户端：{}", clientId);
    }
}
