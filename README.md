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
*   Java 21+
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

## 代码编写规范

### 1. Java 代码规范

#### 1.1 基本约定
- **编码格式**：使用 UTF-8 编码
- **缩进**：使用 4 个空格，禁止使用 Tab
- **行宽**：每行不超过 120 个字符
- **空行**：方法之间、类的成员之间用空行分隔
- **导入顺序**：先 `java.*`，再 `javax.*`，然后第三方库，最后项目内部包，每组之间空一行

#### 1.2 类与接口
- 类名使用 **大驼峰命名法 (PascalCase)**
- **Service 接口**：直接使用业务名称（如 `UserService`），**不以 `I` 开头**
- **Service 实现类**：接口名 + `Impl`（如 `UserServiceImpl`）
- 抽象类以 `Abstract` 开头（如 `AbstractService`）
- 异常类以 `Exception` 结尾（如 `AuthException`）
- 枚举类以 `Enum` 结尾（如 `ReturnCodeEnum`）

#### 1.3 方法与变量
- 方法名使用 **小驼峰命名法 (camelCase)**，采用动宾结构（如 `getUserById`）
- 变量名使用 **小驼峰命名法 (camelCase)**，避免单字符变量（循环索引除外）
- 常量名使用 **全大写 + 下划线分隔**（如 `JWT_EXPIRE_TIME`），定义在常量类中
- 方法参数名使用 **小驼峰命名法**，且具有描述性

#### 1.4 控制结构
- `if-else`、`for`、`while`、`do-while` 语句必须使用大括号，即使只有一行代码
- `if` 条件表达式必须换行时，操作符放在换行处
- 三元运算符仅用于简单表达式，避免嵌套
- 使用 `Objects.isNull()` 和 `Objects.nonNull()` 判断对象是否为空

### 2. 命名规范

| 类型 | 命名规则 | 示例 |
| :--- | :--- | :--- |
| 包名 | 全小写，使用域名反转 | `com.overthinker.cloud.auth` |
| Service 接口 | PascalCase | `UserService`, `AuthService` |
| Service 实现类 | PascalCase + Impl | `UserServiceImpl`, `AuthServiceImpl` |
| Controller | PascalCase + Controller | `UserController`, `AuthController` |
| Mapper | PascalCase + Mapper | `UserMapper`, `RoleMapper` |
| DTO | PascalCase + DTO | `UserRegisterDTO`, `UserUpdateDTO` |
| VO | PascalCase + VO | `UserAccountVO`, `UserDetailsVO` |
| PO | PascalCase + PO | `User`, `SysPermission` |
| 常量类 | PascalCase + Const | `AuthRedisConst`, `SecurityConst` |
| 工具类 | PascalCase + Utils | `SecurityUtils`, `SpringContextUtils` |
| 枚举 | PascalCase + Enum | `ReturnCodeEnum`, `MyModel` |
| 异常 | PascalCase + Exception | `TokenExpiredException` |

### 3. 目录结构规范

```
module/
├── src/main/java/com/overthinker/cloud/module/
│   ├── controller/        # REST API 控制层
│   ├── service/           # 业务接口
│   │   └── impl/          # 业务实现
│   ├── mapper/            # MyBatis Mapper 接口
│   ├── entity/            # 实体类
│   │   ├── PO/            # 数据库实体 (Persistent Object)
│   │   ├── DTO/           # 数据传输对象 (Data Transfer Object)
│   │   └── VO/            # 视图对象 (View Object)
│   ├── config/            # 配置类
│   ├── constants/         # 常量定义
│   ├── utils/             # 工具类
│   ├── listener/          # 事件监听器
│   └── Application.java   # 启动类
└── src/main/resources/
    ├── application.yml    # 应用配置
    └── mapper/            # MyBatis XML 文件
```

### 4. 注释规范

#### 4.1 Javadoc 注释
- 类、接口、公共方法必须有 Javadoc 注释
- 注释使用中文，清晰描述功能、参数、返回值和异常
- 使用 `@author`、`@since` 标注作者和版本信息

```java
/**
 * 用户服务接口
 *
 * @author overH
 * @since 2023-10-10
 */
public interface UserService extends IService<User> {
    /**
     * 根据用户ID查询用户账户信息
     *
     * @param id 用户ID
     * @return 用户账户视图对象
     */
    UserAccountVO findAccountById(Long id);
}
```

#### 4.2 代码注释
- 复杂业务逻辑必须有注释说明
- 单行注释使用 `//`，多行注释使用 `/* */`
- 注释应说明**为什么**这么做，而不是重复代码

### 5. Spring 相关规范

#### 5.1 依赖注入
- **优先使用 `@Resource` 注解**进行字段注入（项目约定）
- 复杂依赖或需要循环依赖时使用构造函数注入
- Service 实现类使用 `@Service("beanName")` 指定 Bean 名称

#### 5.2 事务管理
- 使用 `@Transactional` 注解声明事务
- 事务方法应是 public 修饰符
- 明确指定事务传播行为和回滚规则

#### 5.3 注解使用顺序
```java
@Slf4j                    // 日志
@Service("userService")   // 组件注解
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    // ...
}
```

### 6. 数据层规范

#### 6.1 MyBatis 规范
- Mapper 接口继承 `BaseMapper<T>` 或 `IService<T>`
- 使用 `LambdaQueryWrapper` 进行类型安全的查询构造
- SQL 语句使用 `#{}` 占位符，禁止拼接字符串

#### 6.2 通用响应结果
- 使用 `ResultData<T>` 作为统一返回格式
- 成功响应：`ResultData.success()` 或 `ResultData.success(data)`
- 失败响应：`ResultData.failure()` 或 `ResultData.failure(msg)`

```java
// 成功响应
return ResultData.success(user);

// 失败响应
return ResultData.failure("用户不存在");
```

#### 6.3 返回码枚举
- 使用 `ReturnCodeEnum` 管理统一错误码
- 错误码范围：
  - `2xx`：成功或业务提示
  - `4xx`：客户端错误
  - `5xx`：服务端错误
  - `10xx`：业务自定义错误

### 7. 安全规范

#### 7.1 敏感信息
- 禁止硬编码敏感信息（密码、密钥、配置等）
- 敏感信息必须通过配置文件或环境变量注入
- 日志中禁止打印密码、Token 等敏感数据

#### 7.2 输入验证
- 所有外部输入必须进行参数校验
- 使用 `@Valid` 和 `@Validated` 注解进行 Bean 校验
- SQL 查询使用预编译语句，防止 SQL 注入

#### 7.3 权限控制
- 使用 `@PreAuthorize("hasAuthority('permission_code')")` 进行方法级权限控制
- 权限码格式：`模块:功能`（如 `ai:chat`, `user:list`）

### 8. Git 提交规范

#### 8.1 提交格式
```
<type>(<scope>): <description>

[optional body]

[optional footer]
```

#### 8.2 类型说明
| 类型 | 说明 |
| :--- | :--- |
| `feat` | 新增功能 |
| `fix` | 修复 Bug |
| `docs` | 文档更新 |
| `style` | 代码格式调整（不影响逻辑） |
| `refactor` | 代码重构 |
| `test` | 测试代码 |
| `chore` | 构建/工具配置 |

#### 8.3 示例
```
feat(auth): 添加 OAuth2 授权码模式支持

- 实现 AuthorizationCodeTokenGranter
- 添加授权码存储逻辑
- 更新 SecurityConfig 配置

Closes #123
```

### 9. 日志规范

- 使用 `@Slf4j` 注解注入 Logger
- 日志级别：
  - `DEBUG`：详细的调试信息
  - `INFO`：关键业务流程日志
  - `WARN`：警告信息，需要关注但不影响运行
  - `ERROR`：错误信息，影响功能运行
- 禁止使用 `System.out.println()`

### 10. API 设计规范

#### 10.1 HTTP 方法
| 方法 | 用途 |
| :--- | :--- |
| `GET` | 查询资源 |
| `POST` | 创建资源 |
| `PUT` | 更新资源（全量） |
| `PATCH` | 更新资源（部分） |
| `DELETE` | 删除资源 |

#### 10.2 URI 命名
- 使用小写字母和连字符（如 `/auth/reset-password`）
- 资源用复数形式（如 `/users`）
- 避免动词，使用名词（如 `/users` 而非 `/getUsers`）

#### 10.3 响应格式
```json
{
    "code": 200,
    "msg": "success",
    "data": {},
    "timestamp": 1620000000000
}
```

#### 10.4 错误码
| 错误码 | 含义 |
| :--- | :--- |
| `200` | 请求成功 |
| `400` | 请求参数错误 |
| `401` | 未授权（Token 无效或过期） |
| `403` | 无权限访问 |
| `404` | 资源不存在 |
| `500` | 服务器内部错误 |

### 11. 异步编程规范

#### 11.1 Reactor 响应式编程
- 响应式方法返回 `Mono<T>` 或 `Flux<T>`
- 使用 `reactor.core.publisher` 包下的类
- 方法命名使用 `Mono` 或 `Flux` 相关后缀（如 `query` 而非 `queryMono`）

#### 11.2 异步任务
- 使用 `@Async` 注解标记异步方法
- 异步方法返回 `CompletableFuture<T>`

### 12. 接口文档规范

使用 Swagger/OpenAPI 注解：
- `@Tag`：Controller 级别描述
- `@Operation`：方法级别描述
- `@Parameter`：参数描述

```java
@Tag(name = "用户登录", description = "用户账户登录")
@RestController
@RequestMapping("/auth")
public class AuthController {
    @Operation(summary = "用户注册", description = "新用户通过邮箱验证码注册账号")
    @PostMapping("/register")
    public ResultData<Void> register(@RequestBody @Valid UserRegisterDTO userRegisterDTO) {
        // ...
    }
}
```

## 待办事项

- [ ] 将所有 starter 移动到 cloud-common 里面
- [ ] 在 cloud-system 中，设计新的模块用于后管接口
