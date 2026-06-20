# Spring Security 权限设置指南

## 一、权限表达式概述

Spring Security 提供了丰富的 SpEL（Spring Expression Language）权限表达式，用于在控制器或配置中控制接口访问权限。

### 1.1 内置权限表达式

| 表达式 | 含义 | 使用场景 |
|--------|------|----------|
| `isAuthenticated()` | 用户已登录（非匿名） | 需登录才能访问的接口 |
| `isAnonymous()` | 用户未登录（匿名访客） | 仅允许匿名访问的接口 |
| `isFullyAuthenticated()` | 用户已登录且非"记住我"登录 | 需要完整登录认证的接口 |
| `isRememberMe()` | 用户通过"记住我"登录 | 判断是否为记住我登录 |
| `hasRole('xxx')` | 拥有指定角色（自动加 ROLE_ 前缀） | 基于角色的权限控制 |
| `hasAuthority('xxx')` | 拥有指定权限码 | 基于权限码的细粒度控制 |
| `hasAnyRole('xxx', 'yyy')` | 拥有任一角色 | 多角色权限控制 |
| `hasAnyAuthority('xxx', 'yyy')` | 拥有任一权限码 | 多权限码控制 |
| `permitAll()` | 允许所有用户访问 | 公开接口 |
| `denyAll()` | 拒绝所有用户访问 | 禁用接口 |
| `principal` | 当前登录用户对象 | 获取用户信息 |
| `authentication` | 当前认证对象 | 获取认证详情 |

### 1.2 表达式组合

```java
// 已登录 AND 拥有权限
@PreAuthorize("isAuthenticated() AND hasAuthority('auth:user:list')")

// 已登录 OR 拥有权限
@PreAuthorize("isAuthenticated() OR hasAuthority('auth:public')")

// 拥有多个权限
@PreAuthorize("hasAuthority('auth:user:list') AND hasAuthority('auth:user:edit')")
```

---

## 二、权限注解使用

### 2.1 常用注解

| 注解 | 作用位置 | 说明 |
|------|----------|------|
| `@PreAuthorize` | 方法执行前 | 前置权限检查（推荐） |
| `@PostAuthorize` | 方法执行后 | 后置权限检查（可访问返回结果） |
| `@Secured` | 方法上 | 旧版注解，仅支持角色 |
| `@RolesAllowed` | 方法上 | JSR-250 标准注解 |

### 2.2 使用示例

```java
@RestController
@RequestMapping("/user")
public class UserController {

    // 仅需登录即可访问
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/auth/info")
    public ResultData<UserVO> getInfo() {
        // ...
    }

    // 需要特定权限码才能访问
    @PreAuthorize("hasAuthority('auth:user:list')")
    @GetMapping("/list")
    public ResultData<List<UserVO>> list() {
        // ...
    }

    // 需要多个权限中的任意一个
    @PreAuthorize("hasAnyAuthority('auth:user:list', 'auth:user:query')")
    @GetMapping("/{id}")
    public ResultData<UserVO> getById(@PathVariable Long id) {
        // ...
    }

    // 后置权限检查（可访问返回结果）
    @PostAuthorize("returnObject.data.userId == principal.id")
    @GetMapping("/profile")
    public ResultData<UserVO> getProfile() {
        // ...
    }
}
```

---

## 三、项目权限码设计

### 3.1 权限码命名规范

本项目采用 **模块:资源:操作** 的命名格式：

```
格式：{模块}:{资源}:{操作}

示例：
- auth:user:list      # 用户模块 - 用户资源 - 查询列表
- auth:user:query     # 用户模块 - 用户资源 - 查询详情
- auth:user:add       # 用户模块 - 用户资源 - 新增
- auth:user:edit      # 用户模块 - 用户资源 - 编辑
- auth:user:delete    # 用户模块 - 用户资源 - 删除
- auth:role:list      # 角色模块 - 角色资源 - 查询列表
- auth:menu:list      # 菜单模块 - 菜单资源 - 查询列表
- blog:article:add    # 博客模块 - 文章资源 - 新增
```

### 3.2 操作类型对照表

| 操作类型 | 权限码后缀 | 说明 |
|----------|------------|------|
| 查询列表 | `list` | 获取列表数据 |
| 查询详情 | `query` | 获取单个详情 |
| 新增 | `add` | 创建新记录 |
| 编辑 | `edit` | 更新记录 |
| 删除 | `delete` | 删除记录 |
| 导出 | `export` | 导出数据 |
| 导入 | `import` | 导入数据 |

### 3.3 权限码存储

权限码存储在数据库 `sys_permission` 表中：

```sql
CREATE TABLE sys_permission (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    permisson_code VARCHAR(100) NOT NULL COMMENT '权限编码',
    name VARCHAR(100) COMMENT '权限名称',
    path VARCHAR(200) COMMENT '接口路径',
    http_method VARCHAR(10) COMMENT 'HTTP方法',
    category VARCHAR(50) COMMENT '权限分类',
    service_name VARCHAR(50) COMMENT '服务名称'
);
```

---

## 四、权限加载流程

### 4.1 登录时权限加载

用户登录时，系统会从数据库加载该用户的所有权限码：

```
登录流程：
1. 用户登录 → 认证成功
2. 查询用户角色 → sys_role
3. 查询角色权限 → sys_role_permission
4. 查询权限详情 → sys_permission
5. 权限码加载到 Authentication 对象
6. 存入 SecurityContext
```

### 4.2 权限校验流程

```
接口访问流程：
1. 用户请求接口
2. Spring Security 拦截
3. 解析 @PreAuthorize 注解
4. 执行 SpEL 表达式
5. 校验用户权限码是否匹配
6. 匹配成功 → 执行接口
7. 匹配失败 → 返回 403
```

---

## 五、权限配置示例

### 5.1 SecurityConfig 配置

```java
@Configuration
@EnableMethodSecurity(prePostEnabled = true)  // 启用 @PreAuthorize
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // 公开接口（无需登录）
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/register", "/auth/login").permitAll()
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                // 其他接口需要登录
                .anyRequest().authenticated()
            )
            // 禁用 CSRF
            .csrf(csrf -> csrf.disable())
            // 启用 JWT 认证
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
```

### 5.2 控制器权限配置

```java
@RestController
@RequestMapping("/user")
@Tag(name = "用户管理")
public class UserController {

    // ==================== 认证用户接口（仅需登录） ====================

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/auth/info")
    public ResultData<UserVO> getInfo() { }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/auth/update")
    public ResultData<Void> update() { }

    // ==================== 管理员接口（需要权限码） ====================

    @PreAuthorize("hasAuthority('auth:user:list')")
    @PostMapping("/list")
    public ResultData<List<UserVO>> list() { }

    @PreAuthorize("hasAuthority('auth:user:add')")
    @PostMapping
    public ResultData<Void> add() { }

    @PreAuthorize("hasAuthority('auth:user:edit')")
    @PutMapping("/{id}")
    public ResultData<Void> edit(@PathVariable Long id) { }

    @PreAuthorize("hasAuthority('auth:user:delete')")
    @DeleteMapping("/{id}")
    public ResultData<Void> delete(@PathVariable Long id) { }

    // ==================== 内部服务接口 ====================

    @PreAuthorize("hasAuthority('auth:user:query')")
    @GetMapping("/info")
    public ResultData<UserVO> getById(@RequestParam Long userId) { }
}
```

---

## 六、常见问题

### 6.1 权限表达式写法错误

```java
// ❌ 错误：isAuthenticated 是方法，需要加括号
@PreAuthorize("isAuthenticated")

// ✅ 正确：SpEL 方法调用需要括号
@PreAuthorize("isAuthenticated()")
```

### 6.2 权限码大小写问题

```java
// ❌ 错误：权限码大小写不一致
@PreAuthorize("hasAuthority('AUTH:USER:LIST')")

// ✅ 正确：与数据库中的权限码保持一致
@PreAuthorize("hasAuthority('auth:user:list')")
```

### 6.3 hasRole 与 hasAuthority 区别

```java
// hasRole 会自动加 ROLE_ 前缀
@PreAuthorize("hasRole('ADMIN')")  // 实际检查 ROLE_ADMIN

// hasAuthority 不加前缀，直接匹配
@PreAuthorize("hasAuthority('auth:user:list')")  // 直接检查 auth:user:list
```

**推荐使用 `hasAuthority`**，因为本项目权限码不带 ROLE_ 前缀。

### 6.4 权限继承问题

如果需要实现权限继承（如超级管理员拥有所有权限），可在 `UserDetails` 实现中处理：

```java
public class CustomUserDetails implements UserDetails {

    private Set<String> permissions;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 如果是超级管理员，返回所有权限
        if (isSuperAdmin()) {
            return getAllPermissions();
        }
        // 否则返回用户拥有的权限
        return permissions.stream()
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());
    }
}
```

---

## 七、最佳实践

### 7.1 权限分层设计

```
接口分层：
├── 公开接口（permitAll）     → 注册、登录、Swagger
├── 认证接口（isAuthenticated） → 当前用户信息、修改个人信息
├── 管理接口（hasAuthority）   → 用户管理、角色管理、权限管理
└── 内部接口（hasAuthority）   → 服务间调用
```

### 7.2 权限码命名一致性

- 控制器注解中的权限码必须与数据库中的 `permisson_code` 一致
- 建议在服务启动时自动上报权限到数据库（通过 MQ 或启动事件）

### 7.3 权限粒度控制

| 粒度 | 权限码示例 | 适用场景 |
|------|------------|----------|
| 模块级 | `auth:*:*` | 模块管理员 |
| 资源级 | `auth:user:*` | 资源管理员 |
| 操作级 | `auth:user:list` | 精细控制 |

---

## 八、参考资料

- [Spring Security 官方文档](https://docs.spring.io/spring-security/reference/)
- [Spring Security 权限表达式](https://docs.spring.io/spring-security/reference/servlet/authorization/expressions.html)
- [项目权限表设计](../src/main/java/com/overthinker/cloud/auth/entity/PO/SysPermission.java)