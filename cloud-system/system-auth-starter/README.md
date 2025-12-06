# System-Auth-Starter 安全组件

## 简介
`system-auth-starter` 是一个通用的安全依赖包 (Starter)。**任何业务微服务 (如 cloud-ai, cloud-order) 只需要引入这个依赖，就能自动获得标准的安全能力。**

## 核心功能

### 1. 资源服务器 (Resource Server) 自动配置
*   **自动配置**: `ResourceServerConfig`。
*   **功能**: 
    *   引入 `spring-boot-starter-oauth2-resource-server`。
    *   配置 Security 拦截链，默认拦截所有请求。
    *   配置 JWT 解析器，自动从 Token 的 `authorities` 字段中提取权限，并转换为 Spring Security 的 `GrantedAuthority`。
    *   启用 `@EnableMethodSecurity`，支持在 Controller 上使用 `@PreAuthorize` 注解。

### 2. 权限自动扫描与上报
*   **组件**: `PermissionScanner` 和 `PermissionSender`。
*   **原理**:
    *   在微服务启动时，自动扫描所有 Controller 方法。
    *   提取 `@PreAuthorize("hasAuthority('xxx')")` 中的权限标识 (`xxx`)。
    *   提取 `@Operation(summary="xxx")` 中的接口描述。
    *   将扫描结果封装为 `PermissionDTO` 列表。
    *   通过 **RabbitMQ** 异步发送到 `auth.permission.exchange`。
*   **目的**: 实现“代码即权限”，无需人工维护数据库中的权限表。

## 如何使用

1.  **引入依赖**:
    在微服务的 `pom.xml` 中添加：
    ```xml
    <dependency>
        <groupId>com.overthinker.cloud.system</groupId>
        <artifactId>system-auth-starter</artifactId>
    </dependency>
    ```

2.  **编写代码**:
    在 Controller 接口上添加权限注解：
    ```java
    @GetMapping("/chat")
    @PreAuthorize("hasAuthority('ai:chat')") // 权限标识
    @Operation(summary = "AI对话接口")        // 接口描述
    public String chat() { ... }
    ```

3.  **启动服务**:
    服务启动后，该接口的权限信息会自动注册到 Cloud-Auth 的数据库中。
