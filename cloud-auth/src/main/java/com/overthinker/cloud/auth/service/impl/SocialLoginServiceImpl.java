//package com.overthinker.cloud.auth.service.impl;
//
//import com.overthinker.cloud.auth.entity.PO.LoginUser;
//import com.overthinker.cloud.auth.entity.PO.User;
//import com.overthinker.cloud.auth.service.SocialLoginService;
//import com.overthinker.cloud.auth.service.UserService;
//import com.overthinker.cloud.common.core.resp.ResultData;
//import com.xkcoding.justauth.AuthRequestFactory;
//
//import jakarta.annotation.Resource;
//import lombok.extern.slf4j.Slf4j;
//
//import me.zhyd.oauth.model.AuthCallback;
//import me.zhyd.oauth.model.AuthResponse;
//import me.zhyd.oauth.model.AuthUser;
//import me.zhyd.oauth.request.AuthRequest;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//
//import org.springframework.security.oauth2.core.OAuth2AccessToken;
//import org.springframework.security.oauth2.core.OAuth2Token;
//import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
//import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
//import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
//import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
//import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
//import org.springframework.security.oauth2.server.authorization.context.AuthorizationServerContextHolder;
//import org.springframework.security.oauth2.server.authorization.token.DefaultOAuth2TokenContext;
//import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenContext;
//import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
//import org.springframework.stereotype.Service;
//import org.springframework.util.Assert;
//
//import java.util.Objects;
//import java.util.Set;
//
//@Slf4j
//@Service
//public class SocialLoginServiceImpl implements SocialLoginService {
//
//    @Resource
//    private AuthRequestFactory justAuthRequestFactory;
//
//    @Resource
//    private UserService userService;
//
//    @Resource
//    private RegisteredClientRepository registeredClientRepository;
//
//    @Resource
//    private OAuth2AuthorizationService authorizationService;
//
//    @Resource
//    private OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator;
//
//    @Override
//    public String renderAuth(String source) {
//        AuthRequest authRequest = justAuthRequestFactory.get(source);
//        return authRequest.authorize(source);
//    }
//
//    @Override
//    public ResultData<String> login(String source, AuthCallback callback) {
//        AuthRequest authRequest = justAuthRequestFactory.get(source);
//        AuthResponse<AuthUser> response = authRequest.login(callback);
//        if (response.ok()) {
//            AuthUser authUser = response.getData();
//            User user = userService.findAccountByNameOrEmail(authUser.getUsername());
//            if (Objects.isNull(user)) {
//                // Create a new user
//                user = new User();
//                user.setUsername(authUser.getUsername());
//                user.setNickname(authUser.getNickname());
//                user.setAvatar(authUser.getAvatar());
//                user.setRegisterType(getRegisterType(source));
//                userService.save(user);
//            }
//
//            // --- Start: New token generation logic ---
//            RegisteredClient registeredClient = this.registeredClientRepository.findByClientId("oidc-client");
//            Assert.notNull(registeredClient, "Client 'oidc-client' not found");
//
//            LoginUser loginUser = new LoginUser(user, userService.getUserAuthorities(user.getId()));
//            Authentication principal = new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
//
//            Set<String> authorizedScopes = registeredClient.getScopes();
//
//            OAuth2Authorization.Builder authorizationBuilder = OAuth2Authorization.withRegisteredClient(registeredClient)
//                    .principalName(principal.getName())
//                    .authorizationGrantType(new org.springframework.security.oauth2.core.AuthorizationGrantType("social_login")) // Custom grant type
//                    .authorizedScopes(authorizedScopes);
//
//            OAuth2TokenContext tokenContext = DefaultOAuth2TokenContext.builder()
//                    .registeredClient(registeredClient)
//                    .principal(principal)
//                    .authorizationServerContext(AuthorizationServerContextHolder.getContext())
//                    .authorizedScopes(authorizedScopes)
//                    .tokenType(OAuth2TokenType.ACCESS_TOKEN)
//                    .build();
//
//            OAuth2Token generatedToken = this.tokenGenerator.generate(tokenContext);
//            if (generatedToken == null) {
//                return ResultData.failure("Failed to generate token for user " + user.getUsername());
//            }
//
//            OAuth2AccessToken accessToken = new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER,
//                    generatedToken.getTokenValue(), generatedToken.getIssuedAt(),
//                    generatedToken.getExpiresAt(), tokenContext.getAuthorizedScopes());
//
//            OAuth2Authorization authorization = authorizationBuilder.accessToken(accessToken).build();
//
//            this.authorizationService.save(authorization);
//            // --- End: New token generation logic ---
//
//            return ResultData.success(accessToken.getTokenValue());
//        } else {
//            return ResultData.failure(response.getMsg());
//        }
//    }
//
//    private Integer getRegisterType(String source) {
//        switch (source.toLowerCase()) {
//            case "gitee":
//                return 1;
//            case "github":
//                return 2;
//            case "wechat":
//                return 3;
//            default:
//                return 0;
//        }
//    }
//}
