# cloud-auth 模块

## 模块介绍

`cloud-auth` 是一个专门负责用户认证和授权的微服务模块。它为整个系统提供统一的用户身份验证、权限控制和会话管理功能，是系统安全的基石。

## 登录流程

本模块的登录流程基于 Spring Security 的标准认证机制，并结合 JWT 实现无状态认证。流程如下：

1.  **接收登录请求**: `AuthController` 的 `/login` 端点接收包含用户名和密码的 POST 请求。
2.  **认证处理**: 请求被转发给 `AuthenticationManager` (Spring Security 的认证管理器) 进行处理。
3.  **加载用户信息**: `AuthenticationManager` 会调用 `UserService` (实现了 `UserDetailsService` 接口) 的 `loadUserByUsername` 方法，根据用户名从数据库中查询用户信息。
4.  **密码校验**: `AuthenticationManager` 会使用 `PasswordEncoder` 比较请求中的密码和数据库中存储的加密密码是否匹配。
5.  **生成 Token**: 认证成功后，`AuthController` 会利用 `JwtUtils` 工具类，根据用户信息生成一个 JWT (JSON Web Token)。
6.  **返回 Token**: `AuthController` 将生成的 JWT 返回给客户端。

### 登录流程图

```mermaid
graph TD
    A[用户发起登录请求<br>(POST /api/v1/auth/login<br>携带用户名和密码)] --> B{AuthController};
    B --> C{AuthenticationManager};
    C --> D{UserService (loadUserByUsername)};
    D --> E[数据库];
    E --> D;
    D --> C;
    C --> F{密码校验};
    F -- 认证成功 --> G[生成JWT];
    G --> H[返回JWT给用户];
    F -- 认证失败 --> I[返回认证失败响应];

    subgraph "cloud-auth 模块"
        B
        C
        D
        F
        G
    end
```

## 第三方登录

本模块支持通过第三方平台（如 Gitee, GitHub, WeChat）进行登录。该功能基于 `JustAuth` 库实现。

### 第三方登录流程

1.  **跳转授权页面**: 客户端请求 `GET /oauth/render/{source}`，其中 `{source}` 是第三方平台的名称（如 `gitee`）。后端会重定向到该平台的授权页面。
2.  **用户授权**: 用户在第三方平台页面上同意授权。
3.  **回调处理**: 第三方平台会携带授权码回调到 `GET /oauth/callback/{source}`。
4.  **获取用户信息**: `SocialLoginService` 会使用授权码向第三方平台请求用户信息。
5.  **用户处理**: 
    *   如果该用户是第一次通过此第三方平台登录，系统会自动为其创建一个新用户。
    *   如果用户已存在，则直接登录。
6.  **生成并返回 JWT**: 系统为登录成功的用户生成 JWT，并返回给客户端。

### 配置

为了启用第三方登录，您需要在 `application.yml` 文件中添加相应的配置，并填入您在第三方平台申请的 `client-id` 和 `client-secret`。

```yaml
justauth:
  enabled: true
  gitee:
    client-id: YOUR_GITEE_CLIENT_ID
    client-secret: YOUR_GITEE_CLIENT_SECRET
    redirect-uri: http://localhost:8080/oauth/callback/gitee
  github:
    client-id: YOUR_GITHUB_CLIENT_ID
    client-secret: YOUR_GITHUB_CLIENT_SECRET
    redirect-uri: http://localhost:8080/oauth/callback/github
  wechat:
    client-id: YOUR_WECHAT_CLIENT_ID
    client-secret: YOUR_WECHAT_CLIENT_SECRET
    redirect-uri: http://localhost:8080/oauth/callback/wechat
```

## 角色与权限管理

本模块提供了完整的角色和权限管理功能，可以对系统中的所有用户、角色和权限进行精细化控制。

### 角色管理 API

*   **GET /roles**: 获取所有角色的列表。
*   **POST /roles**: 创建一个新角色。
*   **PUT /roles/{id}**: 更新指定 ID 的角色信息。
*   **DELETE /roles/{id}**: 删除指定 ID 的角色。

### 权限管理 API

*   **GET /permissions**: 获取所有权限的列表。
*   **POST /permissions**: 创建一个新权限。
*   **PUT /permissions/{id}**: 更新指定 ID 的权限信息。
*   **DELETE /permissions/{id}**: 删除指定 ID 的权限。
*   **POST /permissions/internal/register**: (内部接口) 注册权限列表，通常由其他服务在启动时调用。

### 角色与权限关联 API

*   **GET /roles/{roleId}/permissions**: 获取指定角色所拥有的所有权限的 ID 列表。
*   **PUT /roles/{roleId}/permissions**:为指定角色分配权限。请求体为一个包含权限 ID 的 JSON 数组。

## 未来优化计划 (TODO)

为了使 `cloud-auth` 模块更加完善和健壮，计划进行以下优化：

### 1. 功能完整性

-   [ ] **实现 `UserService`**: 填充 `UserServiceImpl` 中的空方法，完成用户注册、密码重置、信息更新等核心业务逻辑。
-   [ ] **实现用户与角色的关联**: 创建 `UserRoleController`，提供为用户分配和撤销角色的 API。

### 2. 代码质量和健壮性

-   [ ] **优化第三方登录**: 增加第三方账号与现有账号的绑定逻辑，避免创建重复用户。
-   [ ] **消除魔法值**: 创建枚举（如 `RegisterTypeEnum`）来替代代码中硬编码的类型值（如 1, 2, 3）。
-   [ ] **全局异常处理**: 创建一个 `GlobalExceptionHandler` 来统一处理业务异常，向客户端返回标准化的错误信息。

### 3. 安全性

-   [ ] **接口权限控制**: 使用 `@PreAuthorize` 注解为所有管理类接口（如角色、权限、用户管理）添加细粒度的权限校验。
