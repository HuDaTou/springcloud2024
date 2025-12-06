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
1.  访问 `http://localhost:9527/auth/login` (通过网关路由到 Auth) 或直接访问 `http://localhost:9000/login`。
2.  使用账号 `admin / 123456` (需在数据库 `sys_user` 表中预置) 登录。
3.  进行 OAuth2 授权流程，获取 Access Token。
4.  携带 Token 访问 `http://localhost:9527/ai/chat`。

## 数据库脚本
请参考 `/sql` 目录下的 SQL 脚本初始化数据库，特别是 `sys_user`, `sys_role`, `sys_permission` 等表。