# Cloud-Gateway 网关服务

## 简介
`cloud-gatway9527` 是基于 **Spring Cloud Gateway** 构建的 API 网关。它是微服务架构的流量入口和安全的第一道防线。

## 核心功能

1.  **动态路由**:
    *   基于 Consul 服务发现，将请求动态路由到后端微服务。
    *   当前仅使用**服务发现自动路由**模式，未配置静态路由。
2.  **统一鉴权 (Resource Server)**:
    *   集成了 **Spring Security OAuth2 Resource Server**。
    *   作为资源服务器，它会拦截所有非白名单请求。
    *   通过配置的 `jwk-set-uri` (指向 `cloud-auth`)，拉取公钥并**校验 JWT Token 的签名和有效期**。
    *   **只负责验票**：如果 Token 无效，直接拒绝 (401)；如果 Token 有效，透传给下游。
3.  **Knife4j 网关聚合**:
    *   聚合后端微服务的 Swagger 文档，提供统一的 API 文档入口。

## 路由配置说明

### 服务发现自动路由

网关开启了基于 Consul 服务发现的自动路由功能：

```yaml
spring:
  cloud:
    gateway:
      discovery:
        locator:
          enabled: true          # 开启自动路由
          lower-case-service-id: true # 服务名转为小写
```

**路由规则**:
- 请求路径格式：`http://网关地址:9527/服务名/**`
- 示例：`http://localhost:9527/cloud-auth/**` -> 转发到 `cloud-auth` 服务

> 注意：当前未配置静态路由，所有请求通过服务发现自动路由进行转发。

## 安全配置说明

### 安全白名单

在 [SecurityConfig.java](file:///root/code/java/gitcode/springcloud2024/cloud-gatway9527/src/main/java/com/overthinker/cloud/gateway/config/SecurityConfig.java) 中配置了以下路径为 `permitAll()`：

| 路径 | 说明 |
| :--- | :--- |
| `/actuator/**` | 健康检查和监控端点 |
| `/cloud-auth/**` | 认证服务的所有端点（用户登录、获取Token等） |

其他所有请求都必须携带有效的 JWT Token 才能访问。

## 配置说明

*   **端口**: `9527`
*   **Issuer URI**: `http://localhost:9123` (cloud-auth 的 Issuer URI)
*   **JWK URI**: `http://localhost:9123/oauth2/jwks` (必须能访问到 cloud-auth)
*   **配置文件**: 
    - `application.yaml`: 包含 Knife4j 聚合配置和 Security 调试日志
    - `application-local.yaml`: 本地开发配置
    - `bootstrap.yaml`: Consul 服务发现配置

## Knife4j 聚合文档

`application.yaml` 中配置了 Knife4j 网关聚合，用于统一查看后端微服务的 API 文档：

```yaml
knife4j:
  gateway:
    enabled: true
    strategy: manual
    routes:
      - name: 用户服务
        url: /user-service/v2/api-docs?group=default
        service-name: user-service
      - name: 订单服务
        url: /order-service/v2/api-docs?group=default
        service-name: order-service
```

## 注意事项
*   网关**不进行**细粒度的权限校验 (如 "是否有 ai:chat 权限")。细粒度校验下放给具体的微服务处理。
*   网关必须能够网络连通 `cloud-auth` 以获取公钥。
*   使用服务发现自动路由，不需要配置静态路由。
