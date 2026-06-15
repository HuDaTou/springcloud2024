package com.overthinker.cloud.api.config;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Feign 客户端凭证拦截器
 * <p>
 * 在发起 Feign 调用前，自动通过 OAuth2 client_credentials 流程获取 JWT Token，
 * 并将 Token 注入 Authorization 请求头。Token 缓存至过期前 60 秒自动刷新。
 * </p>
 *
 * @author overthinker
 * @since 2025-06-13
 */
@Slf4j
public class FeignClientCredentialsInterceptor implements feign.RequestInterceptor {

    /** Token 端点路径 */
    private static final String TOKEN_ENDPOINT = "/oauth2/token";
    /** 授权类型 */
    private static final String GRANT_TYPE = "client_credentials";
    /** Token 过期前 60 秒即刷新 */
    private static final Duration REFRESH_AHEAD = Duration.ofSeconds(60);
    /** 获取 Token 的请求超时 */
    private static final Duration REQUEST_TIMEOUT = Duration.ofSeconds(10);

    /** Token 端点完整地址 */
    private final String tokenUri;
    /** Basic 认证头（client_id:client_secret 的 Base64 编码） */
    private final String basicAuthHeader;

    /** HTTP 客户端 */
    private final HttpClient httpClient;
    /** 锁，防止并发刷新 Token */
    private final ReentrantLock lock = new ReentrantLock();

    /** 缓存的 Token */
    private volatile String cachedToken;
    /** Token 过期时间（已减去提前刷新时间） */
    private volatile Instant expiresAt = Instant.EPOCH;

    /**
     * @param clientId     OAuth2 客户端ID
     * @param clientSecret OAuth2 客户端密钥
     * @param issuerUri    授权服务器地址（如 http://localhost:9123）
     */
    public FeignClientCredentialsInterceptor(String clientId, String clientSecret, String issuerUri) {
        this.tokenUri = issuerUri + TOKEN_ENDPOINT;
        this.basicAuthHeader = "Basic " + Base64.getEncoder()
                .encodeToString((clientId + ":" + clientSecret).getBytes());
        this.httpClient = HttpClient.newHttpClient();
    }

    @Override
    public void apply(feign.RequestTemplate template) {
        template.header("Authorization", "Bearer " + getToken());
    }

    /**
     * 获取有效的访问令牌，Token 缓存至过期前自动刷新
     *
     * @return 访问令牌
     */
    String getToken() {
        if (cachedToken != null && Instant.now().isBefore(expiresAt)) {
            return cachedToken;
        }

        lock.lock();
        try {
            // 双重检查：锁内再次确认是否需要刷新
            if (cachedToken != null && Instant.now().isBefore(expiresAt)) {
                return cachedToken;
            }

            log.info("从 {} 获取 client_credentials Token", tokenUri);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(tokenUri))
                    .header("Authorization", basicAuthHeader)
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .POST(HttpRequest.BodyPublishers.ofString("grant_type=" + GRANT_TYPE))
                    .timeout(REQUEST_TIMEOUT)
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new IllegalStateException(
                        "Failed to obtain client_credentials token, status: " + response.statusCode());
            }

            JSONObject json = JSON.parseObject(response.body());
            this.cachedToken = json.getString("access_token");
            int expiresIn = json.getIntValue("expires_in", 1800); // 默认 30 分钟
            this.expiresAt = Instant.now().plusSeconds(expiresIn).minus(REFRESH_AHEAD);

            log.info("Client credentials Token 获取成功，有效期 {} 秒", expiresIn);

        } catch (Exception e) {
            log.error("获取 client_credentials Token 失败", e);
            throw new IllegalStateException("无法获取内部 API 调用的服务 Token", e);
        } finally {
            lock.unlock();
        }

        return cachedToken;
    }
}
