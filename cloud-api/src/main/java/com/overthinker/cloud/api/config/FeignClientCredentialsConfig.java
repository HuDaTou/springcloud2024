package com.overthinker.cloud.api.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Feign 客户端凭证自动配置
 * <p>
 * 启用 OAuth2 Client Credentials 自动获取 Token 功能，
 * 由 {@link FeignClientCredentialsInterceptor} 在每次 Feign 调用时自动注入 Authorization 头
 * </p>
 *
 * <pre>
 * # 各微服务 application.yml 中配置：
 * cloud:
 *   internal:
 *     client-id: internal-service
 *     client-secret: internal-secret
 *     issuer-uri: http://localhost:9123
 * </pre>
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(ClientCredentialsProperties.class)
public class FeignClientCredentialsConfig {

    @Bean
    public FeignClientCredentialsInterceptor clientCredentialsInterceptor(ClientCredentialsProperties properties) {
        return new FeignClientCredentialsInterceptor(
                properties.getClientId(),
                properties.getClientSecret(),
                properties.getIssuerUri()
        );
    }
}
