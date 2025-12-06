# Cloud-Auth 认证授权中心

## 简介
`cloud-auth` 是基于 **Spring Authorization Server** 构建的统一认证中心。它是整个微服务安全架构的心脏。

## 核心功能

1.  **OAuth2.1 授权服务器**:
    *   支持授权码模式 (Authorization Code)、客户端模式 (Client Credentials)、刷新 Token (Refresh Token) 等标准流程。
    *   提供 `/oauth2/token`, `/oauth2/authorize`, `/oauth2/jwks` 等标准端点。
2.  **用户认证**:
    *   连接 PostgreSQL 数据库 (`sys_user` 表)，校验用户名和密码。
    *   使用 BCrypt 加密存储密码。
3.  **Token 增强 (Token Customizer)**:
    *   在颁发的 JWT Token 中，自动注入用户的 **User ID** 和 **权限列表 (Authorities)**。
    *   下游微服务可以直接从 Token 中获取这些信息，无需再次查询数据库。
4.  **权限管理与入库**:
    *   监听 RabbitMQ 队列 (`auth.permission.register`)。
    *   接收各微服务启动时自动上报的接口权限信息，并持久化到 `sys_permission` 表。

## 配置说明

*   **端口**: `9000`
*   **数据库**: PostgreSQL
*   **注册中心**: Consul
*   **配置类**:
    *   `AuthorizationServerConfig`: 配置 OAuth2 协议、客户端 (Client)、JWK 密钥。
    *   `SecurityConfig`: 配置 Web 安全、表单登录、UserDetailsService。

## 启动前准备
1.  确保 PostgreSQL 数据库已运行，并导入了相关表结构。
2.  确保 RabbitMQ 已运行。
3.  确保 Consul 已运行。