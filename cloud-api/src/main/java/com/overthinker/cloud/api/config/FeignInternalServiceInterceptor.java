package com.overthinker.cloud.api.config;

import lombok.extern.slf4j.Slf4j;

/**
 * Feign 内部服务调用拦截器
 * <p>
 * 在发起 Feign 调用时，自动在请求头添加内部服务标识，
 * 用于服务端验证是否为内部服务调用。
 * </p>
 *
 * @author overthinker
 * @since 2025-06-21
 */
@Slf4j
public class FeignInternalServiceInterceptor implements feign.RequestInterceptor {

    /** 内部服务调用标识头 */
    private static final String INTERNAL_SERVICE_HEADER = "X-Internal-Service";

    /** 服务密钥头 */
    private static final String SERVICE_KEY_HEADER = "X-Service-Key";

    /** 服务密钥 */
    private final String serviceKey;

    /**
     * @param serviceKey 内部服务调用密钥
     */
    public FeignInternalServiceInterceptor(String serviceKey) {
        this.serviceKey = serviceKey;
    }

    @Override
    public void apply(feign.RequestTemplate template) {
        // 添加内部服务标识
        template.header(INTERNAL_SERVICE_HEADER, "true");
        // 添加服务密钥
        template.header(SERVICE_KEY_HEADER, serviceKey);
        log.debug("Feign 内部服务调用：添加服务标识头");
    }
}