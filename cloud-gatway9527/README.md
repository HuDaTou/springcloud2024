# Cloud-Gateway 网关服务

## 简介
`cloud-gatway9527` 是基于 **Spring Cloud Gateway** 构建的 API 网关。它是微服务架构的流量入口和安全的第一道防线。

## 核心功能

1.  **动态路由**:
    *   基于 Consul 服务发现，将请求动态路由到后端微服务。
    *   例如：`/auth/**` -> `cloud-auth`, `/ai/**` -> `cloud-ai`。
2.  **统一鉴权 (Resource Server)**:
    *   集成了 **Spring Security OAuth2 Resource Server**。
    *   作为资源服务器，它会拦截所有非白名单请求。
    *   通过配置的 `jwk-set-uri` (指向 `cloud-auth`)，拉取公钥并**校验 JWT Token 的签名和有效期**。
    *   **只负责验票**：如果 Token 无效，直接拒绝 (401)；如果 Token 有效，透传给下游。

## 配置说明

*   **端口**: `9527`
*   **JWK URI**: `http://localhost:9000/oauth2/jwks` (必须能访问到 cloud-auth)
*   **白名单**: `/login`, `/auth/**`, `/actuator/**` 等路径配置为 `permitAll()`。

## 注意事项
*   网关**不进行**细粒度的权限校验 (如 "是否有 ai:chat 权限")。细粒度校验下放给具体的微服务处理。
*   网关必须能够网络连通 `cloud-auth` 以获取公钥。
