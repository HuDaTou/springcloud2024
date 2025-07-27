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

/**
 * Global authentication and authorization filter.
 */
@Component
@EnableConfigurationProperties(GatewayAuthProperties.class)
public class AuthGatewayFilter implements GlobalFilter, Ordered {

    private final WebClient.Builder webClientBuilder;
    private final GatewayAuthProperties authProperties;

    public AuthGatewayFilter(WebClient.Builder webClientBuilder, GatewayAuthProperties authProperties) {
        this.webClientBuilder = webClientBuilder;
        this.authProperties = authProperties;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();

        // Check if the request path is in the whitelist
        if (isWhitelisted(path)) {
            return chain.filter(exchange);
        }

        // Get the token from the header
        String token = exchange.getRequest().getHeaders().getFirst("Authorization");

        if (token == null || !token.startsWith("Bearer ")) {
            return unauthorized(exchange, "Missing or invalid Authorization header");
        }

        // Call the auth service to verify the token and permission
        return webClientBuilder.build().post()
                .uri("http://cloud-auth/internal/api/v1/auth/verify")
                .bodyValue(new VerifyRequest(token, path))
                .retrieve()
                .bodyToMono(VerifyResponse.class)
                .flatMap(response -> {
                    if (response.success() && response.userId() != null) {
                        // Add user info to headers for downstream services
                        ServerWebExchange modifiedExchange = exchange.mutate()
                                .request(r -> r.header("X-User-Id", response.userId()))
                                .build();
                        return chain.filter(modifiedExchange);
                    } else {
                        // Even if success is true, if userId is null, it's an invalid state
                        return unauthorized(exchange, response.message() != null ? response.message() : "Unauthorized");
                    }
                })
                .onErrorResume(e -> unauthorized(exchange, "Auth service call failed: " + e.getMessage()));
    }

    private boolean isWhitelisted(String path) {
        return whitelist.stream().anyMatch(path::startsWith);
    }

    private Mono<Void> unauthorized(ServerWebExchange exchange, String message) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        // You can customize the response body here if needed
        return exchange.getResponse().setComplete();
    }

    @Override
    public int getOrder() {
        // This filter should run before any other filters
        return -1;
    }
}
