package com.overthinker.cloud.api.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Feign 内部服务调用自动配置
 * <p>
 * 启用内部服务调用标识功能，
 * 由 {@link FeignInternalServiceInterceptor} 在每次 Feign 调用时自动注入服务标识头
 * </p>
 *
 * <pre>
 * # 各微服务 application.yml 中配置：
 * cloud:
 *   internal:
 *     service-key: your-service-key
 * </pre>
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(InternalServiceProperties.class)
public class FeignInternalServiceConfig {

    @Bean
    public FeignInternalServiceInterceptor internalServiceInterceptor(InternalServiceProperties properties) {
        return new FeignInternalServiceInterceptor(properties.getServiceKey());
    }
}