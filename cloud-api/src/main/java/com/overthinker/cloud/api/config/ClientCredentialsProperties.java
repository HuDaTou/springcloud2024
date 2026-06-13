package com.overthinker.cloud.api.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 内部服务间 OAuth2 Client Credentials 配置属性
 * <p>
 * 各微服务在 application.yml 中配置：
 * </p>
 *
 * <pre>
 * cloud:
 *   internal:
 *     client-id: internal-service
 *     client-secret: internal-secret
 *     issuer-uri: http://localhost:9123
 * </pre>
 */
@Data
@ConfigurationProperties(prefix = "cloud.internal")
public class ClientCredentialsProperties {
    /** 客户端ID，默认 internal-service */
    private String clientId = "internal-service";
    /** 客户端密钥 */
    private String clientSecret = "internal-secret";
    /** 授权服务器地址 */
    private String issuerUri = "http://localhost:9123";
}
