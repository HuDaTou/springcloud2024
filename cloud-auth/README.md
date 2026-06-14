# Cloud-Auth 认证授权中心

## 目录

- [1. 模块简介](#1-模块简介)
- [2. 技术架构](#2-技术架构)
- [3. 核心功能流程](#3-核心功能流程)
- [4. API 接口文档](#4-api-接口文档)
- [5. Service 层方法说明](#5-service-层方法说明)
- [6. 数据库表结构](#6-数据库表结构)
- [7. 配置说明](#7-配置说明)
- [8. 启动指南](#8-启动指南)

---

## 1. 模块简介

**cloud-auth** 是基于 **Spring Authorization Server** 构建的统一认证中心，核心职责：

1. **OAuth2.1 授权服务器**：提供标准 OAuth2 端点（Token 颁发、JWKS 公钥发布等）
2. **用户认证**：用户名密码登录、第三方登录（Gitee/GitHub）
3. **Token 管理**：JWE 加密的 Access Token + Refresh Token
4. **权限管理**：RBAC 权限模型，支持权限自动注册
5. **黑名单管理**：IP/用户封禁功能

**端口**：`9123`

---

## 2. 技术架构

```
┌─────────────────────────────────────────────────────────┐
│                    客户端应用                            │
│  (Web/App/其他微服务)                                    │
└────────────────────┬────────────────────────────────────┘
                     │ HTTP + Bearer Token
                     ▼
┌─────────────────────────────────────────────────────────┐
│                 Spring Authorization Server              │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐   │
│  │ JweTokenGen │  │ TokenCustom │  │ JWKS Endpoint│   │
│  │ (Token生成)  │  │ (Token增强)  │  │ (公钥发布)   │   │
│  └─────────────┘  └─────────────┘  └─────────────┘   │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐   │
│  │ BCrypt      │  │ OAuth2      │  │ RabbitMQ    │   │
│  │ (密码加密)   │  │ (授权流程)   │  │ (邮件发送)   │   │
│  └─────────────┘  └─────────────┘  └─────────────┘   │
└────────────────────┬────────────────────────────────────┘
                     │
         ┌───────────┼───────────┐
         ▼           ▼           ▼
    ┌─────────┐ ┌─────────┐ ┌─────────┐
    │PostgreSQL│ │  Redis  │ │ RabbitMQ│
    │(用户数据) │ │(会话/缓存)│ │(邮件队列)│
    └─────────┘ └─────────┘ └─────────┘
```

---

## 3. 核心功能流程

### 3.1 用户登录流程（Spring Security 表单登录）

```
用户提交表单 (username + password)
         │
         ▼
┌────────────────────────────────────┐
│  1. Spring Security 表单登录        │
│     POST /auth/login                │
└─────────────────┬──────────────────┘
                  │
                  ▼
┌────────────────────────────────────┐
│  2. UserDetailsService 验证        │
│     - loadUserByUsername 查询用户  │
│     - BCrypt 验证密码              │
│     - 检查黑名单/禁用状态          │
└─────────────────┬──────────────────┘
                  │
                  ▼
┌────────────────────────────────────┐
│  3. SecurityConfig 生成 Token      │
│     - 创建 LoginUser 加载权限      │
│     - 调用 JwtEncoder 生成 Token   │
│     - JWE 加密保护                 │
└─────────────────┬──────────────────┘
                  │
                  ▼
┌────────────────────────────────────┐
│  4. 返回登录成功响应                │
│     {                              │
│       "code": 200,                 │
│       "msg": "登录成功",            │
│       "token": "xxx",              │
│       "expire": "2024-xx-xx..."    │
│     }                              │
└────────────────────────────────────┘
```

**配置类职责**：
- `JweAuthorizationServerConfig`：负责 **JWT 密钥管理**、签名算法、JWE 加密、JWKS 公钥发布
- `SecurityConfig`：负责 **表单登录流程**、端点放行、CSRF 禁用等

**Token 生成依赖关系**：
```
SecurityConfig.generateToken()
        │
        ▼ 注入
┌────────────────────────────────────┐
│  JweAuthorizationServerConfig     │
│  - JwtEncoder (JWE 加密)          │
│  - RSA KeyPair (密钥对)           │
│  - JWKSet (公钥发布)              │
└────────────────────────────────────┘
```

### 3.2 OAuth2 Token 获取流程（授权码模式）

```
用户已登录 → 客户端请求授权
         │
         ▼
┌────────────────────────────────────┐
│  1. 授权确认页                      │
│     GET /oauth2/authorize          │
└─────────────────┬──────────────────┘
                  │
                  ▼
┌────────────────────────────────────┐
│  2. 用户确认授权                    │
└─────────────────┬──────────────────┘
                  │
                  ▼
┌────────────────────────────────────┐
│  3. 颁发授权码 (Redirect)          │
└─────────────────┬──────────────────┘
                  │
                  ▼
┌────────────────────────────────────┐
│  4. 客户端用授权码换 Token          │
│     POST /oauth2/token              │
└─────────────────┬──────────────────┘
                  │
                  ▼
┌────────────────────────────────────┐
│  5. JwtGenerator + TokenCustomizer │
│     - 生成 JWT Token               │
│     - 注入 user_id/authorities     │
│     - JWE 加密                     │
└─────────────────┬──────────────────┘
                  │
                  ▼
┌────────────────────────────────────┐
│  6. 返回 Access Token + Refresh Token│
└────────────────────────────────────┘
```

### 3.3 权限自动注册流程

```
业务微服务启动
         │
         ▼
┌────────────────────────────────────┐
│  1. 扫描 @SaCheckPermission 注解   │
└─────────────────┬──────────────────┘
                  │
                  ▼
┌────────────────────────────────────┐
│  2. 提取权限信息                    │
│     - 权限码 (如 sys:user:list)   │
│     - 接口路径                      │
│     - 标题/备注                     │
└─────────────────┬──────────────────┘
                  │
                  ▼
┌────────────────────────────────────┐
│  3. 本地缓存去重                    │
└─────────────────┬──────────────────┘
                  │
                  ▼
┌────────────────────────────────────┐
│  4. 通过 MQ 异步发送到 Auth         │
│     Queue: auth.permission.register │
└─────────────────┬──────────────────┘
                  │
                  ▼
┌────────────────────────────────────┐
│  5. Auth 接收并处理                 │
│     - 新增权限                      │
│     - 更新权限                      │
│     - 禁用旧权限 (全量覆盖)         │
└────────────────────────────────────┘
```

---

## 4. API 接口文档

### 4.1 用户管理 (UserController)

**基础路径**：`/user`

| 方法 | 路径 | 说明 | 权限 |
|------|------|------|------|
| GET | `/auth/info` | 获取当前用户信息 | 登录用户 |
| POST | `/auth/update` | 修改用户信息 | 登录用户 |
| POST | `/auth/upload/avatar` | 上传头像 | 登录用户 |
| POST | `/auth/update/email` | 修改绑定邮箱 | 登录用户 |
| POST | `/auth/third/update/email` | 第三方登录绑定邮箱 | 登录用户 |
| GET | `/list` | 获取用户列表 | 管理员 |
| POST | `/search` | 搜索用户 | 管理员 |
| POST | `/update/status` | 更新用户状态 | 管理员 |
| GET | `/details/{id}` | 获取用户详情 | 管理员 |
| DELETE | `/delete` | 删除用户 | 管理员 |

**内部 API**：

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/internal/api/user/count` | 获取用户总数 |
| GET | `/internal/api/user/username` | 根据ID获取用户名 |
| GET | `/internal/api/user/email` | 根据ID获取邮箱 |
| GET | `/internal/api/user/info` | 根据ID获取用户信息 |
| GET | `/internal/api/user/search` | 根据用户名搜索用户ID |

---

### 4.2 角色管理 (RoleController)

**基础路径**：`/roles`

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/` | 获取所有角色 |
| POST | `/` | 创建角色 |
| PUT | `/{id}` | 更新角色 |
| DELETE | `/{id}` | 删除角色 |

---

### 4.3 权限管理 (PermissionController)

**基础路径**：`/permissions`

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/` | 获取所有权限 |
| POST | `/` | 创建权限 |
| PUT | `/{id}` | 更新权限 |
| DELETE | `/{id}` | 删除权限 |

---

### 4.4 角色权限关联 (RolePermissionController)

**基础路径**：`/roles/{roleId}/permissions`

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/` | 获取角色拥有的权限ID列表 |
| PUT | `/` | 分配权限给角色 |

---

### 4.5 黑名单管理 (BlackListController)

**基础路径**：`/blackList`

| 方法 | 路径 | 说明 | 权限 |
|------|------|------|------|
| POST | `/add` | 添加黑名单 | blog:black:add |
| PUT | `/update` | 修改黑名单 | blog:black:update |
| POST | `/getBlackListing` | 查询黑名单 | blog:black:select |
| DELETE | `/delete` | 删除黑名单 | blog:black:delete |

**内部 API**：

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/internal/api/blacklist/check` | 检查IP/用户是否在黑名单 |
| POST | `/internal/api/blacklist/add` | 内部添加黑名单 |
| DELETE | `/internal/api/blacklist/expire` | 内部解除黑名单 |

---

### 4.6 邮箱验证码 (EmailCodeController)

**基础路径**：`/Email`

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | `/getCode` | 发送邮箱验证码 |

---

### 4.7 认证接口

#### 4.7.1 Spring Security 表单登录

| 端点 | 方法 | 说明 |
|------|------|------|
| `/auth/login` | POST | 用户名密码登录 |
| `/auth/logout` | POST | 退出登录 |

**POST /auth/login 请求参数**：

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| `username` | String | 是 | 用户名或邮箱 |
| `password` | String | 是 | 密码 |

**POST /auth/login 响应示例**：

```json
{
  "code": 200,
  "msg": "登录成功",
  "token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9...",
  "expire": "2024-01-01T12:00:00Z"
}
```

#### 4.7.2 OAuth2 认证端点（授权码模式）

| 端点 | 方法 | 说明 |
|------|------|------|
| `/oauth2/jwks` | GET | 获取公钥 (JWKS) |
| `/oauth2/token` | POST | 获取 Token（授权码模式） |
| `/oauth2/authorize` | GET | 授权确认页 |

---

## 5. Service 层方法说明

### 5.1 UserService

```java
// 继承自 IService<User> 和 UserDetailsService

// 用户相关
UserDetails loadUserByUsername(String usernameOrEmail)
    // 根据用户名或邮箱加载用户（用于登录验证）
    // 返回: LoginUser (包含用户信息和权限)

User findAccountByNameOrEmail(String userName, String email)
    // 根据用户名或邮箱查询用户
    // 返回: User 实体

UserAccountVO findAccountById(Long id)
    // 根据ID查询用户账户信息
    // 返回: 用户账户VO (包含权限和角色列表)

void userLoginStatus(Long id, Integer type)
    // 更新用户登录状态
    // 参数: type - 登录方式 (0:邮箱, 1:Gitee, 2:Github)

// 注册与密码
ResultData<Void> userRegister(UserRegisterDTO dto)
    // 用户注册
    // 流程: 验证码校验 → 密码BCrypt加密 → 插入数据库 → 分配默认角色

ResultData<Void> userResetConfirm(UserResetConfirmDTO dto)
    // 密码重置第一步：确认邮箱验证码

ResultData<Void> userResetPassword(UserResetPasswordDTO dto)
    // 密码重置第二步：设置新密码

ResultData<Void> updateEmailAndVerify(UpdateEmailDTO dto)
    // 修改邮箱（需验证原密码）

// 用户管理
List<UserListVO> getUserOrSearch(UserSearchDTO dto)
    // 获取用户列表或搜索

ResultData<Void> updateStatus(Long id, Integer status)
    // 更新用户状态（启用/禁用）

UserDetailsVO findUserDetailsById(Long id)
    // 获取用户详细信息

ResultData<Void> deleteUser(List<Long> ids)
    // 删除用户（同时删除用户-角色关联）

ResultData<Void> updateUser(UserUpdateDTO dto)
    // 更新用户信息

ResultData<String> uploadAvatar(MultipartFile file)
    // 上传头像

// 权限相关
List<String> getUserAuthorities(Long userId)
    // 获取用户权限列表
    // 流程: 查询用户角色 → 查询角色权限 → 返回权限码列表

List<String> getUserRoleNames(Long userId)
    // 获取用户角色名称列表
```

---

### 5.2 AuthService

```java
// 认证服务接口

ResultData<Void> register(UserRegisterDTO dto)
    // 用户注册（完整流程）
    // 流程: 验证码校验 → 检查重复 → 创建用户 → 分配角色 → 删除验证码

ResultData<Void> resetConfirm(UserResetConfirmDTO dto)
    // 密码重置确认

ResultData<Void> resetPassword(UserResetPasswordDTO dto)
    // 密码重置

UserDetails loginOrRegisterThirdParty(String type, String accessToken)
    // 第三方登录或注册
    // 参数: type - "gitee" 或 "github"
    // 返回: 登录用户信息
```

---

### 5.3 BlackListService

```java
// 黑名单服务接口

ResultData<Void> addBlackList(AddBlackListDTO dto)
    // 添加黑名单

List<BlackListVO> getBlackList(SearchBlackListDTO dto)
    // 查询黑名单列表

ResultData<Void> updateBlackList(UpdateBlackListDTO dto)
    // 修改黑名单

ResultData<Void> deleteBlackList(List<Long> ids)
    // 删除黑名单

BlackListCheckResponse checkBlacklist(String ip, Long userId)
    // 检查IP或用户是否在黑名单

void expireBlacklist(String ip, Long userId)
    // 手动解除黑名单

void addBlacklistInternal(AddBlackListRequest request)
    // 内部接口添加黑名单
```

---

### 5.4 PermissionService

```java
// 权限服务接口 (继承 IService<SysPermission>)

// 可添加自定义方法，如：
// SysPermission findByCode(String permissionCode)
// List<SysPermission> findByCategory(String category)
```

---

### 5.5 RoleService

```java
// 角色服务接口

SysRole getDfalultRole()
    // 获取默认角色 (ROLE_USER)

Void setDfalultRole()
    // 设置默认角色
```

---

### 5.6 RolePermissionService

```java
// 角色权限关联服务接口

void assignPermissionsToRole(Long roleId, List<Long> permissionIds)
    // 分配权限给角色
    // 流程: 删除旧关联 → 批量插入新关联

List<Long> getPermissionIdsByRoleId(Long roleId)
    // 获取角色拥有的权限ID列表
```

---

### 5.7 EmailService

```java
// 邮件服务接口

void getEmailCode(String email, String type)
    // 发送邮箱验证码
    // 参数: type - 邮件模板类型
```

---

## 6. 数据库表结构

### 6.1 sys_user（用户表）

| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | BIGINT | 用户ID（雪花算法） |
| nickname | VARCHAR(50) | 用户昵称 |
| username | VARCHAR(50) | 用户名（唯一） |
| password | VARCHAR(100) | 密码（BCrypt加密） |
| gender | TINYINT | 性别（0:未定义, 1:男, 2:女） |
| avatar | VARCHAR(255) | 头像URL |
| intro | VARCHAR(100) | 个人简介 |
| email | VARCHAR(50) | 邮箱 |
| register_type | TINYINT | 注册方式（0:邮箱, 1:Gitee, 2:Github） |
| register_ip | VARCHAR(100) | 注册IP |
| register_address | VARCHAR(50) | 注册地址 |
| login_type | TINYINT | 最近登录方式 |
| login_ip | VARCHAR(100) | 最近登录IP |
| login_address | VARCHAR(50) | 最近登录地址 |
| login_time | DATETIME | 最近登录时间 |
| is_disable | TINYINT | 是否禁用（0:否, 1:是） |
| create_time | DATETIME | 创建时间 |
| update_time | DATETIME | 更新时间 |
| is_deleted | TINYINT | 是否删除（0:否, 1:是） |

---

### 6.2 sys_role（角色表）

| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | BIGINT | 角色ID |
| role_name | VARCHAR(100) | 角色名称 |
| role_key | VARCHAR(10) | 角色标识（如：ADMIN, USER） |
| status | TINYINT | 状态（0:正常, 1:停用） |
| order_num | BIGINT | 排序号 |
| remark | VARCHAR(255) | 备注 |
| create_time | DATETIME | 创建时间 |
| update_time | DATETIME | 更新时间 |
| is_deleted | TINYINT | 是否删除 |

---

### 6.3 sys_permission（权限表）

| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | BIGINT | 权限ID（自增） |
| permission_desc | VARCHAR(50) | 权限描述 |
| permission_key | VARCHAR(255) | 权限字符（如：sys:user:list） |
| menu_id | BIGINT | 关联菜单ID |
| create_time | DATETIME | 创建时间 |
| update_time | DATETIME | 更新时间 |
| is_deleted | TINYINT | 是否删除 |

---

### 6.4 sys_user_role（用户角色关联表）

| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | INT | 主键 |
| user_id | INT | 用户ID |
| role_id | VARCHAR(20) | 角色ID |

---

### 6.5 sys_role_permission（角色权限关联表）

| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | BIGINT | 主键 |
| role_id | BIGINT | 角色ID |
| permission_id | BIGINT | 权限ID |

---

### 6.6 sys_menu（菜单表）

| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | BIGINT | 菜单ID |
| title | VARCHAR(50) | 菜单标题 |
| icon | VARCHAR(50) | 图标 |
| path | VARCHAR(255) | 路由路径 |
| component | VARCHAR(255) | 组件路径 |
| parent_id | BIGINT | 父菜单ID |
| order_num | INT | 排序 |
| is_disable | TINYINT | 是否禁用 |
| create_time | DATETIME | 创建时间 |
| update_time | DATETIME | 更新时间 |
| is_deleted | TINYINT | 是否删除 |

---

### 6.7 t_black_list（黑名单表）

| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | BIGINT | 主键 |
| user_id | BIGINT | 用户ID（可为空） |
| reason | VARCHAR | 封禁原因 |
| banned_time | DATETIME | 封禁时间 |
| expires_time | DATETIME | 到期时间（null=永久） |
| type | INT | 类型（1:用户, 2:路人/IP） |
| ip_info | JSON | IP信息（JSON格式） |

---

### 6.8 sys_login_log（登录日志表）

| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | BIGINT | 主键 |
| user_name | VARCHAR(50) | 用户名 |
| ip | VARCHAR(50) | 登录IP |
| address | VARCHAR(50) | 登录地址 |
| browser | VARCHAR(50) | 浏览器 |
| os | VARCHAR(50) | 操作系统 |
| type | TINYINT | 登录类型（0:前台, 1:后台, 2:非法） |
| state | TINYINT | 状态（0:成功, 1:失败） |
| message | TEXT | 登录信息 |
| create_time | DATETIME | 创建时间 |

---

### 6.9 sys_log（操作日志表）

| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | BIGINT | 主键 |
| module | VARCHAR(50) | 模块名称 |
| operation | VARCHAR(50) | 操作类型 |
| user_name | VARCHAR(50) | 操作人员 |
| ip | VARCHAR(100) | IP地址 |
| method | VARCHAR(255) | 操作方法 |
| req_parameter | TEXT | 请求参数 |
| state | TINYINT | 状态（0:成功, 1:失败, 2:异常） |
| exception | TEXT | 异常信息 |
| time | BIGINT | 消耗时间(ms) |
| create_time | DATETIME | 创建时间 |

---

## 7. 配置说明

### 7.1 核心配置类

| 配置类 | 职责 |
|--------|------|
| JweAuthorizationServerConfig | OAuth2授权服务器配置（Token生成、JWE加密、JWKS） |
| SecurityConfig | Web安全配置（表单登录、CSRF、端点放行） |
| AuthorizationServerConfig | 备用授权服务器配置 |
| JweKeyConfig | JWE密钥配置（RSA密钥对生成） |
| BlackListInitConfig | 黑名单初始化配置 |

---

### 7.2 Token 配置

```yaml
# Token 设置
access_token:
  format: SELF_CONTAINED  # JWT格式
  time_to_live: 30m       # Access Token有效期
refresh_token:
  time_to_live: 7d       # Refresh Token有效期
  reuse: false           # 不复用
signature:
  algorithm: RS256       # 签名算法
encryption:
  algorithm: RSA-OAEP-256  # 加密算法
  method: A256GCM           # 内容加密
```

---

### 7.3 OAuth2 客户端配置

| 配置项 | 值 |
|--------|-----|
| client_id | oidc-client |
| client_secret | {noop}secret |
| grant_types | authorization_code, refresh_token, client_credentials, password |
| scopes | openid, profile, read, write |
| authentication_methods | CLIENT_SECRET_BASIC, NONE |
| require_authorization_consent | false |

---

## 8. 启动指南

### 8.1 前置条件

| 组件 | 默认地址 | 说明 |
|------|---------|------|
| PostgreSQL | localhost:5432 | 存储用户、角色、权限数据 |
| Redis | localhost:6379 | 缓存、Session管理 |
| RabbitMQ | localhost:5672 | 邮件发送队列 |
| Consul | localhost:8500 | 服务注册与发现 |

### 8.2 启动顺序

```bash
# 1. 启动基础设施
docker-compose up -d postgres redis rabbitmq consul

# 2. 导入数据库
psql -h localhost -U postgres -d cloud_data < sql/cloud_data.sql

# 3. 启动 Auth 服务
cd cloud-auth
mvn spring-boot:run

# 4. 验证启动
curl http://localhost:9123/oauth2/jwks
```

### 8.3 测试账号

| 用户名 | 密码 | 角色 |
|--------|------|------|
| ADMIN | 123456 | 超级管理员 |
| test | test123 | 普通用户 |

**BCrypt加密密码示例**：
```
123456 → $2a$10$VyFtQ3T943p3NY5R0IxzIONjdqABmuCSGiHe5uV8d1ujLGYuS2KZe
```

---

## 附录

### A. 权限码命名规范

```
模块:资源:操作

示例：
- sys:user:list      # 用户列表
- sys:user:add       # 添加用户
- sys:user:update    # 修改用户
- sys:user:delete    # 删除用户
- blog:article:list   # 文章列表
- blog:article:publish # 发布文章
```

### B. Redis Key 规范

| Key 前缀 | 说明 | 示例 |
|----------|------|------|
| auth:email:code:* | 邮箱验证码 | auth:email:code:user@example.com |
| auth:blacklist:* | 黑名单缓存 | auth:blacklist:ip:192.168.1.1 |

### C. RabbitMQ 队列

| 队列名 | 说明 |
|--------|------|
| auth.email.send | 邮件发送队列 |
| auth.permission.register | 权限自动注册队列 |

---

**文档版本**：1.0  
**最后更新**：2024-XX-XX  
**维护者**：overH
