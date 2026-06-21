package com.overthinker.cloud.gateway.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 全局过滤器：剥离内部服务调用请求头
 * <p>
 * 所有从网关进入的请求，移除 X-Internal-Service 和 X-Service-Key 请求头，
 * 防止外部客户端伪造这两个头绕过微服务的内部安全校验。
 * </p>
 *
 * @author overthinker
 * @since 2025-06-22
 */
@Slf4j
@Component
public class RemoveInternalHeadersFilter implements GlobalFilter, Ordered {

    private static final String INTERNAL_SERVICE_HEADER = "X-Internal-Service";
    private static final String SERVICE_KEY_HEADER = "X-Service-Key";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest().mutate()
                .headers(httpHeaders -> {
                    httpHeaders.remove(INTERNAL_SERVICE_HEADER);
                    httpHeaders.remove(SERVICE_KEY_HEADER);
                })
                .build();

        return chain.filter(exchange.mutate().request(request).build());
    }

    @Override
    public int getOrder() {
        // 最高优先级，在其他过滤器之前执行
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
