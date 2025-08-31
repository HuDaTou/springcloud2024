package com.overthinker.cloud.gateway.filter;


import com.overthinker.cloud.api.dto.VerifyRequest;
import com.overthinker.cloud.api.dto.VerifyResponse;
import com.overthinker.cloud.gateway.config.GatewayAuthProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * 全局认证和授权过滤器。
 */
@Component
@EnableConfigurationProperties(GatewayAuthProperties.class)
public class AuthGatewayFilter implements GlobalFilter, Ordered {

    private final WebClient.Builder webClientBuilder;
    private final GatewayAuthProperties authProperties;
    private final List<String> whitelist;

    public AuthGatewayFilter(WebClient.Builder webClientBuilder, GatewayAuthProperties authProperties) {
        this.webClientBuilder = webClientBuilder;
        this.authProperties = authProperties;
        this.whitelist = authProperties.getWhitelist();
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();

        // 检查请求路径是否在白名单中
        if (isWhitelisted(path)) {
            return chain.filter(exchange);
        }

        // 从请求头中获取令牌
        String token = exchange.getRequest().getHeaders().getFirst("Authorization");

        if (token == null || !token.startsWith("Bearer ")) {
            return unauthorized(exchange, "缺失或无效的Authorization请求头");
        }

        // 调用认证服务以验证令牌和权限
        return webClientBuilder.build().post()
                .uri("http://cloud-auth/internal/api/v1/auth/verify")
                .bodyValue(new VerifyRequest(token, path))
                .retrieve()
                .bodyToMono(VerifyResponse.class)
                .flatMap(response -> {
                    if (response.success() && response.userId() != null) {
                        // 将用户信息添加到请求头中，供下游服务使用
                        ServerWebExchange modifiedExchange = exchange.mutate()
                                .request(r -> r.header("X-User-Id", response.userId()))
                                .build();
                        return chain.filter(modifiedExchange);
                    } else {
                        // 即使success为true，如果userId为null，也是无效状态
                        return unauthorized(exchange, response.message() != null ? response.message() : "未授权");
                    }
                })
                .onErrorResume(e -> unauthorized(exchange, "认证服务调用失败: " + e.getMessage()));
    }

    private boolean isWhitelisted(String path) {
        return whitelist.stream().anyMatch(path::startsWith);
    }

    private Mono<Void> unauthorized(ServerWebExchange exchange, String message) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        // 如果需要，可以在此处自定义响应体
        return exchange.getResponse().setComplete();
    }

    @Override
    public int getOrder() {
        // 此过滤器应在任何其他过滤器之前运行
        return -1;
    }
}
