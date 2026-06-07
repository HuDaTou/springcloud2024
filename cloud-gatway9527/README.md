# Cloud-Gateway 网关服务

## 简介
`cloud-gatway9527` 是基于 **Spring Cloud Gateway** 构建的 API 网关。它是微服务架构的流量入口和安全的第一道防线。

## 核心功能

1.  **动态路由**:
    *   基于 Consul 服务发现，将请求动态路由到后端微服务。
    *   支持两种路由模式：**服务发现自动路由** 和 **静态配置路由**。
2.  **统一鉴权 (Resource Server)**:
    *   集成了 **Spring Security OAuth2 Resource Server**。
    *   作为资源服务器，它会拦截所有非白名单请求。
    *   通过配置的 `jwk-set-uri` (指向 `cloud-auth`)，拉取公钥并**校验 JWT Token 的签名和有效期**。
    *   **只负责验票**：如果 Token 无效，直接拒绝 (401)；如果 Token 有效，透传给下游。

## 路由配置说明

### 1. 服务发现自动路由

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
- 示例：`http://localhost:9527/cloud-auth/login` -> 转发到 `cloud-auth` 服务

### 2. 静态配置路由

| 路由ID | 路径匹配 | 目标服务 | 说明 |
| :--- | :--- | :--- | :--- |
| `auth_route` | `/auth/**` | `lb://cloud-auth` | 认证服务路由（负载均衡） |
| `pay_routh1` | `/pay/gateway/get/**` | `lb://cloud-payment-service` | 支付服务路由（负载均衡） |
| `pay_routh2` | `/pay/gateway/info/**` | `http://localhost:8001` | 支付服务直连路由 |

**路由优先级**:
- 静态配置的路由优先级高于服务发现自动路由
- 路径匹配遵循**最长匹配原则**

### 3. 路由断言说明

网关使用 Spring Cloud Gateway 的 `Path` 断言进行路径匹配：

| 断言类型 | 示例 | 说明 |
| :--- | :--- | :--- |
| `Path=/auth/**` | `/auth/login`, `/auth/oauth2/token` | 匹配以 `/auth` 开头的所有路径 |
| `Path=/pay/gateway/get/**` | `/pay/gateway/get/123` | 匹配特定路径模式 |

## 配置说明

*   **端口**: `9527`
*   **JWK URI**: `http://localhost:9000/oauth2/jwks` (必须能访问到 cloud-auth)
*   **白名单**: `/login`, `/auth/**`, `/actuator/**` 等路径配置为 `permitAll()`。
*   **配置文件**: 
    - `application.yaml`: 默认配置（生产环境）
    - `application-local.yaml`: 本地开发配置
    - `bootstrap.yaml`: Consul 服务发现配置

## 注意事项
*   网关**不进行**细粒度的权限校验 (如 "是否有 ai:chat 权限")。细粒度校验下放给具体的微服务处理。
*   网关必须能够网络连通 `cloud-auth` 以获取公钥。
*   使用 `lb://` 前缀启用负载均衡，需要配合服务发现使用。
