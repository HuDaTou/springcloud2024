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

## 任务列表

- [ ] **联调前后端的登录功能**
  - 实现前后端分离的登录认证流程
  - 确保 OAuth2 授权码模式正常运作
  - 验证 Token 的正确获取和使用

- [ ] **修改实体类转化方式为 MapStruct**
  - 替换现有的 BeanUtils 或手动转换方式
  - 定义 PO/DTO/VO 之间的映射关系
  - 优化对象转换性能

- [ ] **实现接口权限自动注册自动化鉴权方案**
  - 设计权限自动扫描与上报机制
  - 实现 MQ 消息队列异步通信
  - 处理幂等性和去重问题
  - 实现全量覆盖机制

- [ ] **集成 SkyWalking 链路追踪**
  - 配置 SkyWalking Agent
  - 实现分布式链路追踪
  - 关联权限注册链路

## 接口权限自动注册方案

### 一、方案概述

本方案实现**接口权限自动注册**，通过自动化手段替代传统手动录入权限的繁琐工作，大幅提升开发效率。

#### 核心流程

```
各业务微服务启动
    ↓
自动扫描所有 @PreAuthorize("hasAuthority('permission_code')") 注解
    ↓
提取权限标识、接口路径、接口标题、备注信息
    ↓
本地缓存对比去重（仅推送变更）
    ↓
通过 MQ 异步发送到 Auth 服务
    ↓
Auth 服务接收并处理：
  - 存在则更新
  - 不存在则新增
  - 数据库中存在但本次未推送的权限自动禁用
```

### 二、方案优点

| 优点 | 说明 |
|------|------|
| **完全自动化** | 开发写完接口加注解，重启服务自动入库，无需手动维护 |
| **微服务解耦** | MQ 异步投递，业务服务不依赖 Auth 服务可用性 |
| **零维护成本** | 不用每个人去后台手动建权限，减少人工错误 |
| **精准性高** | 以代码注解为准，权限码零错误 |
| **全量覆盖** | 自动禁用已删除接口的权限，避免垃圾权限和越权漏洞 |

### 三、技术实现细节

#### 1. 服务配置

每个微服务需要在 `application.yml` 中配置唯一标识：

```yaml
spring:
  application:
    name: cloud-ai

system:
  auth:
    module-name: cloud-ai          # 模块唯一标识
    version: 1.0.0                  # 版本号
    scan-enabled: true               # 是否开启权限扫描（生产环境可关闭）
    mq-topic: permission-sync        # MQ 主题
```

#### 2. 注解扫描机制

使用 Spring 的 `ComponentScan` 和 `反射` 机制扫描所有 Controller：

```java
public class PermissionScanner {
    
    @Resource
    private ApplicationContext applicationContext;
    
    public List<PermissionDTO> scanPermissions() {
        Map<String, Object> controllers = applicationContext.getBeansWithAnnotation(RestController.class);
        List<PermissionDTO> permissions = new ArrayList<>();
        
        for (Object controller : controllers.values()) {
            Method[] methods = controller.getClass().getDeclaredMethods();
            for (Method method : methods) {
                PreAuthorize preAuthorize = method.getAnnotation(PreAuthorize.class);
                if (preAuthorize != null) {
                    // 提取权限码
                    String authority = extractAuthority(preAuthorize.value());
                    // 提取接口信息
                    permissions.add(buildPermissionDTO(authority, method, controller));
                }
            }
        }
        return permissions;
    }
}
```

#### 3. MQ 消息结构

```java
@Data
public class PermissionSyncMessage {
    private String moduleName;           // 模块名称
    private String serviceVersion;       // 服务版本
    private List<PermissionInfo> permissions;
    private Long timestamp;
    private String traceId;              // SkyWalking 链路追踪 ID
}

@Data
public class PermissionInfo {
    private String authority;            // 权限标识 (如: sys:user:list)
    private String apiPath;              // 接口路径
    private String title;                // 接口标题
    private String description;          // 接口描述
    private String httpMethod;           // HTTP 方法
    private Integer status;              // 状态 (1: 启用, 0: 禁用)
}
```

#### 4. 本地缓存与去重

```java
public class PermissionCache {
    
    private volatile Map<String, PermissionInfo> localCache = new ConcurrentHashMap<>();
    
    public List<PermissionInfo> getChangedPermissions(List<PermissionInfo> scanned) {
        List<PermissionInfo> changed = new ArrayList<>();
        for (PermissionInfo permission : scanned) {
            PermissionInfo cached = localCache.get(permission.getAuthority());
            if (cached == null || !cached.equals(permission)) {
                changed.add(permission);
            }
        }
        // 更新缓存
        localCache.clear();
        scanned.forEach(p -> localCache.put(p.getAuthority(), p));
        return changed;
    }
}
```

#### 5. Auth 服务处理逻辑

```java
@RabbitListener(queues = "permission-sync-queue")
public class PermissionSyncConsumer {
    
    @Resource
    private PermissionService permissionService;
    
    public void handlePermissionSync(PermissionSyncMessage message) {
        // 1. 链路追踪
        String traceId = message.getTraceId();
        
        // 2. 幂等处理（使用 moduleName + authority 作为唯一键）
        String idempotentKey = message.getModuleName() + ":" + message.getPermissions().size();
        
        // 3. 全量覆盖处理
        permissionService.syncPermissions(message.getModuleName(), message.getPermissions());
        
        // 4. 自动禁用旧权限
        permissionService.disableStalePermissions(message.getModuleName(), 
            message.getPermissions().stream()
                .map(PermissionInfo::getAuthority)
                .collect(Collectors.toSet()));
    }
}
```

#### 6. 数据库处理策略

```java
@Override
public void syncPermissions(String moduleName, List<PermissionInfo> newPermissions) {
    List<SysPermission> existingPermissions = permissionMapper.selectByModuleName(moduleName);
    Map<String, SysPermission> existingMap = existingPermissions.stream()
        .collect(Collectors.toMap(SysPermission::getAuthority, Function.identity()));
    
    Set<String> newAuthoritySet = new HashSet<>();
    
    for (PermissionInfo info : newPermissions) {
        newAuthoritySet.add(info.getAuthority());
        SysPermission existing = existingMap.get(info.getAuthority());
        
        if (existing != null) {
            // 更新
            BeanUtils.copyProperties(info, existing);
            existing.setStatus(1);
            existing.setUpdateTime(LocalDateTime.now());
            permissionMapper.updateById(existing);
        } else {
            // 新增
            SysPermission newPermission = new SysPermission();
            BeanUtils.copyProperties(info, newPermission);
            newPermission.setModuleName(moduleName);
            newPermission.setStatus(1);
            permissionMapper.insert(newPermission);
        }
    }
    
    // 禁用旧权限（数据库中存在但本次未推送的）
    for (SysPermission existing : existingPermissions) {
        if (!newAuthoritySet.contains(existing.getAuthority())) {
            existing.setStatus(0);
            existing.setUpdateTime(LocalDateTime.now());
            permissionMapper.updateById(existing);
        }
    }
}
```

### 四、解决的关键问题

#### 1. 幂等性保证

- **问题**：MQ 重复消费导致数据错乱
- **解决**：
  - 使用 `moduleName + authority` 作为唯一键
  - 消费前检查是否已处理
  - 数据库层面使用 `ON CONFLICT DO UPDATE`

#### 2. 防重复推送

- **问题**：多实例部署时重复推送
- **解决**：
  - 本地缓存已扫描的权限信息
  - 仅推送变更的权限
  - 使用分布式锁控制（可选）

#### 3. 旧权限自动禁用

- **问题**：接口删除后权限残留
- **解决**：采用**全量覆盖机制**，本次未推送的权限自动标记为禁用

#### 4. 版本控制

- **问题**：不知道权限属于哪个服务
- **解决**：
  - 每个服务配置 `module-name`
  - 权限记录关联 `module_name` 字段
  - 支持按服务维度查询和过滤

### 五、与传统方案对比

| 维度 | 手动后台录入 | 自动化注册方案 |
|------|-------------|----------------|
| 开发效率 | 低，每条手动加 | 极高，注解写完自动入库 |
| 准确性 | 容易录错权限码 | 注解为准，零错误 |
| 维护成本 | 高 | 几乎为 0 |
| 微服务适配 | 极差 | 完美适配 |
| 安全风险 | 一般 | 可控，规范后更安全 |
| 旧接口处理 | 需手动删除 | 自动禁用 |

### 六、生产环境注意事项

#### 1. 开关控制

```yaml
system:
  auth:
    scan-enabled: ${SCAN_ENABLED:false}  # 生产环境默认关闭
```

#### 2. 灰度发布

- 新服务上线时先在测试环境验证
- 确认无误后再开启自动注册
- 建议配合蓝绿部署使用

#### 3. 监控告警

- 监控 MQ 消息消费延迟
- 监控权限同步失败的场景
- 设置异常告警阈值

## SkyWalking 链路追踪方案

### 一、方案概述

本项目集成 **SkyWalking** 实现分布式链路追踪，支持：

- 全链路追踪（从网关到微服务）
- 性能监控
- 错误追踪
- 权限注册链路追踪

### 二、架构设计

```
┌─────────────────────────────────────────────────────────────┐
│                        SkyWalking UI                         │
│                    (http://localhost:8080)                   │
└─────────────────────────────────────────────────────────────┘
                              │
                              │ HTTP/gRPC
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                    SkyWalking OAP Server                     │
│                   (localhost:11800 gRPC)                    │
└─────────────────────────────────────────────────────────────┘
                              ▲
                              │ Agent
    ┌─────────────────────────┼─────────────────────────┐
    │                         │                         │
    ▼                         ▼                         ▼
┌─────────┐            ┌──────────┐             ┌──────────┐
│ Gateway │            │ Cloud-AI │             │ Cloud-Auth│
│  :9527  │            │   :8080  │             │   :9000  │
└─────────┘            └──────────┘             └──────────┘
```

### 三、配置说明

#### 1. Agent 配置

在每个微服务的 `agent.config` 或 JVM 参数中配置：

```bash
java -javaagent:skywalking-agent.jar \
     -Dskywalking.agent.service_name=cloud-auth \
     -Dskywalking.collector.backend_service=localhost:11800 \
     -jar cloud-auth.jar
```

#### 2. Maven 配置（可选）

在 `pom.xml` 中添加依赖（可选，用于代码层面的链路追踪）：

```xml
<dependency>
    <groupId>org.apache.skywalking</groupId>
    <artifactId>apm-toolkit-trace</artifactId>
    <version>${skywalking.version}</version>
</dependency>
```

### 四、使用示例

#### 1. 基础链路追踪

SkyWalking Agent 会自动追踪：

- HTTP 请求
- 数据库访问
- 外部服务调用
- MQ 消息发送/消费

#### 2. 自定义链路追踪

```java
import org.apache.skywalking.apm.toolkit.trace.TraceContext;

@Service
public class PermissionScannerService {
    
    public void scanAndReport() {
        // 获取当前 Trace ID
        String traceId = TraceContext.traceId();
        
        // 添加自定义标签
        TraceContext.tag("module-name", "cloud-ai");
        TraceContext.tag("permission-count", String.valueOf(permissions.size()));
        
        // 添加链路追踪信息到 MQ 消息
        PermissionSyncMessage message = new PermissionSyncMessage();
        message.setTraceId(traceId);
        
        // 发送 MQ 消息
        mqProducer.send(message);
    }
}
```

#### 3. MQ 链路追踪

```java
@Component
public class PermissionMQProducer {
    
    @Resource
    private RabbitTemplate rabbitTemplate;
    
    public void sendPermissionSync(PermissionSyncMessage message) {
        // 注入当前链路的 Context
        InjectorDTO injector = TracingContextInjector.injector();
        Map<String, String> context = injector.inject();
        
        // 将链路上下文放入消息头
        rabbitTemplate.convertAndSend(
            "permission-sync-exchange",
            "permission.sync",
            message,
            msg -> {
                msg.getMessageProperties().setHeaders(context);
                return msg;
            }
        );
    }
}

@Component
public class PermissionMQConsumer {
    
    @RabbitListener(queues = "permission-sync-queue")
    public void handleMessage(PermissionSyncMessage message, 
                              @Header(AmqpHeaders.DELIVERY_TAG) long tag,
                              MessageHeaders headers) {
        // 提取链路上下文并恢复追踪
        Map<String, String> context = (Map<String, String>) headers.get("sw-context");
        TracingContextExtractor extractor = TracingContextExtractor.extractor(context);
        TracingContextUtils.restore(extractor);
        
        try {
            // 处理业务逻辑
            permissionService.syncPermissions(message);
        } finally {
            // 结束当前 span
            TracingContextUtils.finishSpan();
        }
    }
}
```

### 五、关键特性

#### 1. 权限注册链路追踪

```java
@Slf4j
@Service
public class PermissionScannerService {
    
    public void scanAndReport() {
        String traceId = TraceContext.traceId();
        log.info("开始扫描权限, traceId: {}", traceId);
        
        try {
            List<PermissionInfo> permissions = scanner.scan();
            log.info("扫描到 {} 个权限, traceId: {}", permissions.size(), traceId);
            
            // 发送到 MQ
            messageProducer.send(permissions, traceId);
            log.info("权限已发送到 MQ, traceId: {}", traceId);
            
        } catch (Exception e) {
            log.error("权限扫描失败, traceId: {}, error: {}", traceId, e.getMessage());
            TraceContext.error("permission-scan-failed", e);
            throw e;
        }
    }
}
```

#### 2. 监控指标

SkyWalking 会自动采集以下指标：

| 指标 | 说明 |
|------|------|
| `trace_count` | 链路总数 |
| `trace_success_rate` | 成功率 |
| `response_time` | 响应时间 |
| `jvm_memory` | JVM 内存 |
| `jvm_gc` | GC 次数和时间 |

#### 3. 告警配置

```yaml
# alarm-settings.yml
rules:
  # 权限同步失败告警
  permission-sync-failure:
    metrics-name: permission_sync_failure_count
    op: ">"
    threshold: 5
    period: 10
    count: 3
    message: "权限同步失败超过阈值"
  
  # 链路追踪延迟告警
  trace-latency:
    metrics-name: trace_response_time
    op: ">"
    threshold: 3000
    period: 5
    count: 2
    message: "链路响应时间超过 3 秒"
```

### 六、运维指南

#### 1. 查看链路

1. 访问 SkyWalking UI (http://localhost:8080)
2. 选择 **拓扑图** 查看服务关系
3. 选择 **追踪** 查看具体链路
4. 使用 Trace ID 查询特定请求

#### 2. 性能分析

- 使用 ** flame graph ** 查看调用耗时分布
- 使用 ** thread dump ** 分析线程问题
- 使用 ** heap dump ** 分析内存问题

#### 3. 常见问题

| 问题 | 解决方案 |
|------|---------|
| Agent 未启动 | 检查 `-javaagent` 参数是否正确配置 |
| 无法连接 OAP | 检查 `backend_service` 地址是否正确 |
| 链路断裂 | 检查防火墙和端口是否开放 |
| 数据丢失 | 增加 OAP Server 的存储容量 |

## 待办事项

- [ ] 将所有 starter 移动到 cloud-common 里面
- [ ] 在 cloud-system 中，设计新的模块用于后管接口
- [ ] 联调前后端的登录功能
- [ ] 修改实体类转化方式为 MapStruct
- [ ] 实现接口权限自动注册自动化鉴权方案
- [ ] 集成 SkyWalking 链路追踪
