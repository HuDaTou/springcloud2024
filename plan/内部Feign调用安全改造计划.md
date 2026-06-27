# 内部 Feign 调用安全改造计划

## 背景

原设计流程：Feign 调用前需先请求 auth 服务获取内部 Token，携带 Token 调用目标服务，目标服务验证 JWT 中的 `ROLE_INTERNAL_SERVICE` 角色后放行。流程过于复杂。

新设计（简化）：Feign 拦截器在请求头直接添加 `X-Internal-Service: true` 和 `X-Service-Key: <密钥>`，服务端过滤器验证请求头后直接创建内部认证对象放行，无需与 auth 服务交互。

## 当前问题

1. **通用组件分散**：`InternalServiceAuthenticationFilter` 和 `InternalServiceAuthentication` 只在 `cloud-auth` 中，其他微服务无法复用
2. **代码重复**：`cloud-auth/SecurityConfig` 中的 `internalServiceBypass()` 与 `system-auth-starter/ResourceServerConfig` 中的逻辑重复
3. **密钥硬编码**：`InternalServiceAuthenticationFilter` 中服务密钥硬编码，未使用配置属性
4. **ResourceServerConfig 条件限制**：`@ConditionalOnMissingBean(name = "defaultSecurityFilterChain")` 导致 `cloud-auth` 无法加载 `ResourceServerConfig`，但其仍需要内部调用放行的通用能力

## 改造目标

- 通用组件统一放在 `system-auth-starter`，所有服务（包括 `cloud-auth`）复用
- `cloud-auth/SecurityConfig` 只保留登录、登出等特有逻辑，移除重复的放行逻辑
- `InternalServiceAuthenticationFilter` 使用 `InternalServiceProperties` 配置密钥

## 改造步骤

### 1. 移动 `InternalServiceAuthentication` 到 system-auth-starter

- **从**: `cloud-auth/.../filter/InternalServiceAuthentication.java`
- **到**: `cloud-system-starter/system-auth-starter/.../config/InternalServiceAuthentication.java`
- 包路径从 `com.overthinker.cloud.auth.filter` 改为 `com.overthinker.cloud.system.starter.auth.config`

### 2. 移动并改进 `InternalServiceAuthenticationFilter` 到 system-auth-starter

- **从**: `cloud-auth/.../filter/InternalServiceAuthenticationFilter.java`
- **到**: `cloud-system-starter/system-auth-starter/.../config/InternalServiceAuthenticationFilter.java`
- **改动**:
  - 包路径改为 `com.overthinker.cloud.system.starter.auth.config`
  - 移除 `@Component` 注解，改为在 AutoConfiguration 中注册
  - 移除硬编码的 `EXPECTED_SERVICE_KEY`，改用 `InternalServiceProperties` 注入
  - 使用 `InternalServiceProperties`（来自 `cloud-api`，`system-auth-starter` 已依赖）

### 3. 新建 `InternalServiceAuthAutoConfiguration` 自动配置

- **位置**: `cloud-system-starter/system-auth-starter/.../config/InternalServiceAuthAutoConfiguration.java`
- **职责**: 提供内部服务认证的通用组件，无论 `ResourceServerConfig` 是否加载
- **包含 Bean**:
  - `InternalServiceAuthenticationFilter` — 过滤器（注入 `InternalServiceProperties`）
  - `internalServiceBypassAuthorizationManager` — 共享的 URL 级别 `AuthorizationManager`
  - `methodSecurityExpressionHandler` — 共享的方法级别表达式处理器

### 4. 更新 `ResourceServerConfig`

- 移除私有的 `internalServiceBypass()` 方法
- 移除 `methodSecurityExpressionHandler()` Bean
- 注入 `InternalServiceAuthenticationFilter`，添加到 filter chain
- 注入共享的 `AuthorizationManager`（或保持内联 lambda）

### 5. 更新 `cloud-auth/SecurityConfig`

- 移除重复的 `internalServiceBypass()` 方法
- 移除重复的 `methodSecurityExpressionHandler()` Bean
- 移除直接依赖的 `InternalServiceAuthenticationFilter` 字段注入（改用共享 Bean）
- 在 filter chain 中添加共享的 `InternalServiceAuthenticationFilter`
- 保留：登录过滤器、登出处理、白名单等 auth 特有逻辑

### 6. 注册新 AutoConfiguration

- 在 `system-auth-starter/.../META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports` 中添加：
  ```
  com.overthinker.cloud.system.starter.auth.config.InternalServiceAuthAutoConfiguration
  ```

## 涉及文件清单

| 操作 | 文件 |
|------|------|
| 新增 | `plan/内部Feign调用安全改造计划.md` |
| 新增 | `system-auth-starter/.../config/InternalServiceAuthentication.java` |
| 新增 | `system-auth-starter/.../config/InternalServiceAuthenticationFilter.java` |
| 新增 | `system-auth-starter/.../config/InternalServiceAuthAutoConfiguration.java` |
| 修改 | `system-auth-starter/.../config/ResourceServerConfig.java` |
| 修改 | `system-auth-starter/.../resources/META-INF/spring/...AutoConfiguration.imports` |
| 修改 | `cloud-auth/.../config/SecurityConfig.java` |
| 删除 | `cloud-auth/.../filter/InternalServiceAuthentication.java` |
| 删除 | `cloud-auth/.../filter/InternalServiceAuthenticationFilter.java` |

## 架构示意图

```
┌─────────────────────────────────────────────────────────────┐
│  cloud-api                                                   │
│  ├─ FeignInternalServiceInterceptor  (添加请求头)             │
│  ├─ InternalServiceProperties       (配置 cloud.internal.*)  │
│  └─ FeignInternalServiceConfig      (自动配置拦截器)          │
└──────────────────────────┬──────────────────────────────────┘
                           │ depends on
┌──────────────────────────▼──────────────────────────────────┐
│  system-auth-starter (通用)                                   │
│  ├─ InternalServiceAuthentication          (认证对象)        │
│  ├─ InternalServiceAuthenticationFilter    (请求头验证过滤器) │
│  ├─ InternalServiceAuthAutoConfiguration   (提供通用Bean)    │
│  ├─ InternalServiceMethodSecurityExpressionHandler           │
│  ├─ InternalServiceSecurityExpressionRoot                   │
│  ├─ ResourceServerConfig  (条件加载，❌不用于cloud-auth)      │
│  └─ JwtAuthenticationConverterConfig                        │
└──────────────────────────┬──────────────────────────────────┘
                           │ depends on
┌──────────────────────────▼──────────────────────────────────┐
│  cloud-auth (auth服务特有)                                    │
│  └─ SecurityConfig                                          │
│     ├─ 注入 InternalServiceAuthenticationFilter (来自starter)│
│     ├─ 注入 internalServiceBypassAuthorizationManager         │
│     ├─ 登录过滤器 JsonUsernamePasswordAuthenticationFilter   │
│     ├─ 登出处理                                              │
│     └─ OAuth2 Resource Server 配置                           │
└─────────────────────────────────────────────────────────────┘
```
