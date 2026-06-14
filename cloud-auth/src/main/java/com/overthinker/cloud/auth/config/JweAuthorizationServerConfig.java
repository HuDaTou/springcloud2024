package com.overthinker.cloud.auth.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.overthinker.cloud.auth.token.CustomOAuth2TokenCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.JwtGenerator;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Duration;
import java.util.UUID;

/**
 * JWE (JSON Web Encryption) 授权服务器配置
 * 使用 JWE 加密 Token，确保 Token 内容不可被篡改和泄露
 */
@Configuration
public class JweAuthorizationServerConfig {

    private final KeyPair jweKeyPair;

    public JweAuthorizationServerConfig() {
        this.jweKeyPair = generateRsaKey();
    }

    /**
     * JWE 授权服务器安全过滤器链 (Order 1)
     * 处理 OAuth2 协议端点，使用 JWE 加密 Token
     */
    @Bean
    @Order(1)
    public SecurityFilterChain jweAuthorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
        OAuth2AuthorizationServerConfigurer authorizationServerConfigurer =
                new OAuth2AuthorizationServerConfigurer();

        http
                .securityMatcher(authorizationServerConfigurer.getEndpointsMatcher())
                .with(authorizationServerConfigurer, (authServer) ->
                        authServer
                                .oidc(Customizer.withDefaults())
                                .tokenGenerator(jweTokenGenerator())
                )
                .authorizeHttpRequests((authorize) ->
                        authorize.anyRequest().authenticated()
                )
                .exceptionHandling((exceptions) -> exceptions
                        .defaultAuthenticationEntryPointFor(
                                new LoginUrlAuthenticationEntryPoint("/login"),
                                new MediaTypeRequestMatcher(MediaType.TEXT_HTML)
                        )
                )
                .oauth2ResourceServer((resourceServer) ->
                        resourceServer.jwt(Customizer.withDefaults())
                );

        return http.build();
    }

    /**
     * JWE Token 生成器
     * 配置 Token 使用 JWE 加密格式
     */
    @Bean
    @Primary
    public OAuth2TokenGenerator<?> jweTokenGenerator() {
        return new JwtGenerator(jweJwtEncoder());
    }

    /**
     * JWE JWT 编码器
     * 使用 RSA 密钥对进行加密
     */
    @Bean
    public JwtEncoder jweJwtEncoder() {
        return new NimbusJwtEncoder(jweJwkSource());
    }

    /**
     * JWE JWK 密钥源
     * 使用 RSA 密钥对进行加密
     */
    @Bean
    public JWKSource<SecurityContext> jweJwkSource() {
        return new ImmutableJWKSet<>(jwkSet());
    }

    /**
     * JWK Set Bean，直接供其他组件使用
     */
    @Bean
    public JWKSet jwkSet() {
        RSAPublicKey publicKey = (RSAPublicKey) jweKeyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) jweKeyPair.getPrivate();

        RSAKey rsaKey = new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID(UUID.randomUUID().toString())
                .build();

        return new JWKSet(rsaKey);
    }

    /**
     * JWE JWT 解码器
     * 用于验证和解密 JWE Token
     */
    @Bean
    public JwtDecoder jweJwtDecoder() {
        return NimbusJwtDecoder.withPublicKey((RSAPublicKey) jweKeyPair.getPublic()).build();
    }

    /**
     * 客户端仓库
     * 配置允许访问授权服务器的客户端应用
     */
    @Bean
    public RegisteredClientRepository registeredClientRepository() {
        RegisteredClient oidcClient = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId("oidc-client")
                .clientSecret("{noop}secret")
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .redirectUri("http://127.0.0.1:8080/login/oauth2/code/oidc-client")
                .redirectUri("http://localhost:9527/login/oauth2/code/oidc-client")
                .postLogoutRedirectUri("http://127.0.0.1:8080/")
                .scope(OidcScopes.OPENID)
                .scope(OidcScopes.PROFILE)
                .scope("read")
                .scope("write")
                .clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build())
                .tokenSettings(tokenSettings())
                .build();

        // Internal service client for inter-service communication via client_credentials
        RegisteredClient internalServiceClient = RegisteredClient.withId("internal-service")
                .clientId("internal-service")
                .clientSecret("{noop}internal-secret")
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .scope("internal")
                .clientSettings(ClientSettings.builder()
                        .requireAuthorizationConsent(false)
                        .build())
                .tokenSettings(tokenSettings())
                .build();

        return new InMemoryRegisteredClientRepository(oidcClient, internalServiceClient);
    }

    /**
     * Token 设置
     * 配置 Token 的有效期和格式
     */
    @Bean
    public TokenSettings tokenSettings() {
        return TokenSettings.builder()
                .accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED)
                .accessTokenTimeToLive(Duration.ofMinutes(30))
                .refreshTokenTimeToLive(Duration.ofDays(7))
                .reuseRefreshTokens(false)
                .idTokenSignatureAlgorithm(SignatureAlgorithm.RS256)
                .build();
    }

    /**
     * 授权服务器设置
     */
    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder()
                .issuer("http://localhost:9123")
                .build();
    }

    /**
     * Token 自定义器
     * 在生成 Token 时注入自定义声明
     */
    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> tokenCustomizer() {
        return new CustomOAuth2TokenCustomizer();
    }

    /**
     * 暴露 RSA 私钥供其他组件使用
     */
    @Bean
    public RSAPrivateKey jwePrivateKey() {
        return (RSAPrivateKey) jweKeyPair.getPrivate();
    }

    /**
     * 生成 RSA 密钥对
     */
    private static KeyPair generateRsaKey() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            return keyPairGenerator.generateKeyPair();
        } catch (Exception ex) {
            throw new IllegalStateException("RSA 密钥生成失败", ex);
        }
    }
}
