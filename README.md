# Spring Cloud 微服务安全架构 (Spring Cloud Security 2024)

本项目基于 Spring Cloud 2024 和 Spring Boot 3.x 构建，采用标准的 OAuth2.1 协议实现微服务架构下的统一认证与鉴权。

## 核心架构

采用 **"网关统一验票 + 微服务独立鉴权"** 的混合模式，兼顾了安全性和开发效率。

*   **Cloud-Auth (认证中心)**: 
    *   基于 `spring-authorization-server`。
    *   负责用户认证 (Login)、授权 (Consent) 和 颁发 Token (JWT)。
    *   Token 中包含用户的身份信息 (User ID) 和权限列表 (Permissions)。
*   **Cloud-Gateway (网关)**: 
    *   基于 `spring-cloud-gateway` + `spring-security-oauth2-resource-server`。
    *   负责拦截所有请求，校验 Token 的签名有效性 (验票)。
    *   只有持有有效 Token 的请求才能通过网关，路由到下游微服务。
*   **Microservices (业务微服务)**: 
    *   引入 `system-auth-starter`。
    *   负责**细粒度鉴权**。使用 `@PreAuthorize("hasAuthority('ai:chat')")` 控制接口访问权限。
    *   负责**权限上报**。启动时自动扫描接口权限并上报给 Cloud-Auth。

## 登录与鉴权流程 (以 OAuth2 授权码模式为例)

本系统采用 OAuth2.1 授权码模式 (`Authorization Code Grant`) 进行用户登录和 Token 获取，并结合 JWT 和资源服务器进行鉴权。

### 1. 用户认证与获取授权码 (Authorization Code)
1.  **用户请求**：用户在浏览器或客户端访问受保护资源，或直接访问网关登录页 (`http://localhost:9527/auth/login`)。
2.  **网关路由**：`cloud-gatway9527` 收到 `/auth/login` 请求，将其路由到 `cloud-auth` 服务。
3.  **Auth 登录页**：`cloud-auth`（基于 `Spring Security` 的 `formLogin` 配置）返回默认的 HTML 登录页面。**此登录页由 Spring Security 自动生成，无需手动编写 Controller。**
4.  **用户登录**：用户在登录页输入用户名 (`user` 或数据库中的用户邮箱) 和密码，提交 (`POST /login`)。
5.  **身份验证**：`cloud-auth` 的 `UserDetailsServiceImpl` 通过查询数据库 (`sys_user` 表) 验证用户身份。
6.  **授权请求**：认证成功后，如果客户端 (例如前端应用或另一个服务) 之前发起了 `/oauth2/authorize` 授权请求，`cloud-auth` 会提示用户确认授权给该客户端。
7.  **颁发授权码**：用户同意授权后，`cloud-auth` 会向客户端的 `redirect_uri` 重定向，并在 URL 参数中包含一个 `authorization_code` (授权码)。

### 2. 客户端获取 Token (Access Token & Refresh Token)
1.  **Token 请求**：客户端拿着上一步获得的 `authorization_code` 和自身的 `client_id`, `client_secret` 等信息，向 `cloud-auth` 的 `/oauth2/token` 端点发起 `POST` 请求。
2.  **Token 验证与颁发**：`cloud-auth` 验证 `authorization_code` 和客户端凭证。验证成功后，颁发一个 **JWT 格式的 Access Token** 和一个 **Refresh Token** 给客户端。
    *   **Token 增强**：Access Token 中包含了用户的 `user_id`, `username` 以及从数据库查询到的 **权限列表 (`authorities`)**。

### 3. 访问受保护资源 (Resource Access)
1.  **携带 Token**：客户端在后续访问任何受保护的业务微服务时，都必须在 HTTP 请求头中带上 `Authorization: Bearer <Access Token>`。
2.  **网关验签**：请求首先到达 `cloud-gatway9527`。网关配置为 `Resource Server`，它会拦截请求：
    *   提取 `Authorization` 头中的 Access Token。
    *   使用 `cloud-auth` 的公钥 (从 `http://localhost:9000/oauth2/jwks` 获取) **本地验证** Token 的签名和有效期。
    *   如果 Token **无效** (签名错误、已过期)，网关直接拒绝请求 (401 Unauthorized)。
    *   如果 Token **有效**，网关将请求路由到下游的业务微服务 (如 `cloud-AI`)。
3.  **微服务鉴权**：请求到达业务微服务 (`cloud-AI`)。
    *   微服务也配置为 `Resource Server` (通过引入 `system-auth-starter` 实现)。
    *   它再次解析 Token，并使用 `SecurityUtils` 获取用户信息和权限。
    *   微服务中的接口通过 `@PreAuthorize("hasAuthority('ai:chat')")` 等注解，**根据 Token 中的权限列表进行细粒度权限校验**。
    *   如果用户拥有所需权限，接口执行业务逻辑；否则，返回 403 Forbidden。

## 模块说明

| 模块 | 路径 | 描述 |
| :--- | :--- | :--- |
| **cloud-auth** | `/cloud-auth` | **认证授权中心**。核心服务，必须优先启动。 |
| **cloud-gatway9527** | `/cloud-gatway9527` | **API 网关**。流量入口，负责路由和 Token 验签。 |
| **system-auth-starter** | `/cloud-system/system-auth-starter` | **安全公共组件**。封装了资源服务器配置和权限扫描逻辑，供微服务引用。 |
| **cloud-ai** | `/cloud-AI` | **AI 业务服务** (示例)。演示如何接入安全体系。 |

## 快速开始

### 1. 环境准备
*   Java 17+
*   Maven 3.8+
*   Consul (默认 `localhost:8500`)
*   RabbitMQ (默认 `localhost:5672`)
*   PostgreSQL (默认 `localhost:5432`)
*   Redis (默认 `localhost:6379`)

### 2. 启动顺序
1.  启动基础设施 (Consul, RabbitMQ, PostgreSQL, Redis)。
2.  启动 **Cloud-Auth** (端口 9000)。等待启动完成。
3.  启动 **Cloud-Gateway** (端口 9527)。
4.  启动 **Cloud-AI** (或其他业务服务)。

### 3. 验证流程
1.  访问 `http://localhost:9527/auth/login` (通过网关路由到 Auth)。
2.  使用账号 `admin / 123456` (需在数据库 `sys_user` 表中预置，密码需 BCrypt 加密) 登录。
3.  如果这是授权码模式的首次授权，会看到一个授权同意页面。
4.  完成 OAuth2 授权流程，客户端 (通常是前端应用) 将获取到 **Access Token**。
5.  携带此 Access Token (在请求头 `Authorization: Bearer <AccessToken>` 中) 访问 `http://localhost:9527/ai/talk` (假设 `talk` 是 `cloud-AI` 中的受保护接口)。
    *   如果成功访问，说明流程正确。
    *   如果 Gateway 返回 401，请检查 Token 是否过期或无效。
    *   如果微服务返回 403，请检查 Token 中的权限是否足够。

## 数据库脚本
请参考 `/sql` 目录下的 SQL 脚本初始化数据库，特别是 `sys_user`, `sys_role`, `sys_permission` 等表。
**注意**：`sys_user` 表中的密码需要使用 BCrypt 加密 (例如，`admin` 对应的 BCrypt 密码是 `$2a$10$VyFtQ3T943p3NY5R0IxzIONjdqABmuCSGiHe5uV8d1ujLGYuS2KZe`)。
