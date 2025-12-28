package com.overthinker.cloud.auth.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;

@Configuration
@EnableWebSecurity
public class AuthorizationServerConfig {

    /**
     * 【Bean: 授权服务器安全过滤链】
     * 作用：这是优先级最高的过滤链（Order 1）。
     * 1. 它专门负责处理 OAuth2 的协议端点，比如 /oauth2/authorize、/oauth2/token 等。
     * 2. 结合了你之前提到的 MediaTypeRequestMatcher：当用户未登录访问授权端点时，
     * 如果是通过浏览器访问（Accept包含text/html），则跳转到 /login 登录页。
     * 3. 启用了 OIDC（OpenID Connect 1.0）支持。
     */
    @Bean
    @Order(1)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
        // 创建配置器
        OAuth2AuthorizationServerConfigurer authorizationServerConfigurer =
                new OAuth2AuthorizationServerConfigurer();

        http
                // 【关键点】只拦截授权服务器相关的端点（如 /oauth2/token, /oauth2/authorize）
                // 这样剩下的请求才会流转到你的 SecurityConfig (Order 2)
                .securityMatcher(authorizationServerConfigurer.getEndpointsMatcher())

                // 开启 OIDC
                .with(authorizationServerConfigurer, (authServer) ->
                        authServer.oidc(Customizer.withDefaults())
                )

                // 身份验证请求：所有匹配到上述 Matcher 的请求都需要认证
                .authorizeHttpRequests((authorize) ->
                        authorize.anyRequest().authenticated()
                )

                // 异常处理
                .exceptionHandling((exceptions) -> exceptions
                        .defaultAuthenticationEntryPointFor(
                                new LoginUrlAuthenticationEntryPoint("/login"),
                                new MediaTypeRequestMatcher(MediaType.TEXT_HTML)
                        )
                )
                // 资源服务器支持
                .oauth2ResourceServer((resourceServer) ->
                        resourceServer.jwt(Customizer.withDefaults())
                );

        return http.build();
    }
    /**
     * 【Bean: 客户端仓库】
     * 作用：用于管理注册的“客户端应用”（比如你的前端 Vue 项目或其他微服务）。
     * 1. 这里目前使用的是 InMemory 内存模式，实际生产环境通常改为从数据库（JDBC）读取。
     * 2. 配置了客户端 ID（oidc-client）、秘钥（secret）、授权模式（授权码模式、刷新令牌）等。
     */
    @Bean
    public RegisteredClientRepository registeredClientRepository() {
        RegisteredClient oidcClient = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId("oidc-client")
                // {noop} 表示明文存储（仅用于 Demo），生产环境应使用加密后的 Hash 值
                .clientSecret("{noop}secret")
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                // 授权后的重定向回调地址
                .redirectUri("http://127.0.0.1:8080/login/oauth2/code/oidc-client")
                .postLogoutRedirectUri("http://127.0.0.1:8080/")
                .scope(OidcScopes.OPENID)
                .scope(OidcScopes.PROFILE)
                // 配置是否需要用户手动点击“确认授权”确认框
                .clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build())
                .build();

        return new InMemoryRegisteredClientRepository(oidcClient);
    }

    /**
     * 【Bean: JWK 源码】
     * 作用：提供用于签署（Sign）和验证（Verify）JWT 令牌的秘钥对（公钥和私钥）。
     * 1. 授权服务器用私钥对生成的 Access Token 进行签名。
     * 2. 资源服务器用公钥验证该 Token 是否由本服务器颁发且未被篡改。
     */
    @Bean
    public JWKSource<SecurityContext> jwkSource() {
        KeyPair keyPair = generateRsaKey(); // 生成 RSA 秘钥对
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        RSAKey rsaKey = new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID(UUID.randomUUID().toString())
                .build();
        JWKSet jwkSet = new JWKSet(rsaKey);
        return new ImmutableJWKSet<>(jwkSet);
    }

    /**
     * 辅助方法：生成 RSA 秘钥对（2048位）
     */
    private static KeyPair generateRsaKey() {
        KeyPair keyPair;
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            keyPair = keyPairGenerator.generateKeyPair();
        }
        catch (Exception ex) {
            throw new IllegalStateException("RSA 秘钥生成失败", ex);
        }
        return keyPair;
    }

    /**
     * 【Bean: 授权服务器设置】
     * 作用：用于配置授权服务器本身的端点路径（Endpoint）。
     * 默认情况下，它定义了例如 /oauth2/token、/oauth2/authorize 等标准路径。
     * 你可以在此处通过 builder 修改这些默认的 URL。
     */
    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder().build();
    }
}