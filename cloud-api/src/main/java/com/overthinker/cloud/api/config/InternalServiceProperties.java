package com.overthinker.cloud.api.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 内部服务调用配置属性
 * <p>
 * 各微服务在 application.yml 中配置：
 * </p>
 *
 * <pre>
 * cloud:
 *   internal:
 *     service-key: your-service-key
 * </pre>
 */
@Data
@ConfigurationProperties(prefix = "cloud.internal")
public class InternalServiceProperties {
    /** 内部服务调用密钥，用于验证服务间调用 */
    private String serviceKey = "internal-service-key-2024";
}