# OpenAPI (Swagger) 常用注解完全指南

## 目录

- [1. 概述](#1-概述)
- [2. 核心概念](#2-核心概念)
- [3. 类级别注解](#3-类级别注解)
- [4. 方法级别注解](#4-方法级别注解)
- [5. 参数级别注解](#5-参数级别注解)
- [6. 字段级别注解](#6-字段级别注解)
- [7. 完整示例](#7-完整示例)
- [8. 最佳实践](#8-最佳实践)

---

## 1. 概述

### 1.1 Swagger 与 OpenAPI 的关系

| 版本 | 说明 |
|------|------|
| **Swagger 2.0** | 旧版本，使用 `io.swagger` 注解 |
| **OpenAPI 3.0** | 标准规范，Swagger 规范重命名而来 |
| **SpringDoc OpenAPI** | 当前推荐使用，基于 OpenAPI 3.0 规范 |

### 1.2 依赖配置

本项目使用 `springdoc-openapi-starter-webmvc-ui`，版本：`2.8.9`

**Maven 依赖：**
```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.8.9</version>
</dependency>
```

**访问地址：**
- Swagger UI: `http://localhost:端口号/swagger-ui.html`
- OpenAPI JSON: `http://localhost:端口号/v3/api-docs`

---

## 2. 核心概念

### 2.1 注解分类

```
OpenAPI 注解
├── API 级别（顶层配置）
├── Controller 级别（类注解）
├── Operation 级别（方法注解）
├── Parameter 级别（参数注解）
├── Schema 级别（字段注解）
└── Security 级别（安全配置）
```

### 2.2 常用包路径

```java
import io.swagger.v3.oas.annotations.Operation;      // 操作说明
import io.swagger.v3.oas.annotations.tags.Tag;       // 控制器分组
import io.swagger.v3.oas.annotations.Parameter;     // 参数说明
import io.swagger.v3.oas.annotations.Parameters;     // 参数数组
import io.swagger.v3.oas.annotations.media.Schema;   // 字段定义
import io.swagger.v3.oas.annotations.media.Content;  // 媒体类型
import io.swagger.v3.oas.annotations.media.ExampleObject;  // 示例值
import io.swagger.v3.oas.annotations.responses.ApiResponse; // 响应定义
import io.swagger.v3.oas.annotations.responses.ApiResponses; // 响应数组
import io.swagger.v3.oas.annotations.security.SecurityRequirement;  // 安全需求
import io.swagger.v3.oas.annotations.security.SecurityScheme;      // 安全方案
import io.swagger.v3.oas.annotations.enums.ParameterIn;           // 参数位置
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;   // 安全类型
import io.swagger.v3.oas.annotations.OpenAPIDefinition;          // 顶层定义
import io.swagger.v3.oas.annotations.info.Contact;               // 联系信息
import io.swagger.v3.oas.annotations.info.License;                // 许可证
import io.swagger.v3.oas.annotations.info.Info;                   // API 信息
```

---

## 3. 类级别注解

### 3.1 @Tag - 控制器分组

**作用：** 对 Controller 进行分组，用于 API 文档的分类展示

**位置：** Controller 类上

**示例：**
```java
@Tag(name = "用户管理", description = "用户相关接口", tags = {"user", "users"})
@RestController
@RequestMapping("/api/users")
public class UserController {
    // ...
}
```

**属性说明：**

| 属性 | 类型 | 说明 | 示例 |
|------|------|------|------|
| `name` | String | 分组名称（必填） | "用户管理" |
| `description` | String | 详细描述 | "提供用户CRUD操作" |
| `tags` | String[] | 额外标签 | {"user", "users"} |

### 3.2 @Hidden - 隐藏接口

**作用：** 隐藏指定接口，不在文档中显示

**位置：** Controller 类或方法上

**示例：**
```java
@Hidden
@GetMapping("/internal/status")
public String getInternalStatus() {
    return "OK";
}
```

### 3.3 @RequestBody 相关的 Schema

**示例：**
```java
@Operation(summary = "创建用户")
@PostMapping
public ResultData<Void> createUser(
    @RequestBody(
        description = "用户信息",
        required = true,
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = UserCreateDTO.class),
            examples = @ExampleObject(
                name = "示例",
                summary = "创建管理员",
                value = "{\"username\": \"admin\", \"password\": \"123456\", \"email\": \"admin@example.com\"}"
            )
        )
    ) 
    @Valid @RequestBody UserCreateDTO dto
) {
    // ...
}
```

---

## 4. 方法级别注解

### 4.1 @Operation - 操作说明

**作用：** 描述接口的功能、用途、参数、返回值等

**位置：** Controller 方法上

**基本示例：**
```java
@Operation(summary = "获取用户详情", description = "根据用户ID获取用户详细信息")
@GetMapping("/{id}")
public ResultData<UserVO> getUser(@PathVariable Long id) {
    // ...
}
```

**完整示例：**
```java
@Operation(
    summary = "创建用户",
    description = "创建新用户，包含基本信息校验",
    deprecated = false,  // 是否废弃
    tags = {"user"},
    method = "POST"
)
```

### 4.2 @Parameters - 参数数组

**作用：** 描述多个参数

**示例：**
```java
@Operation(summary = "条件查询用户")
@GetMapping
@Parameters({
    @Parameter(name = "username", description = "用户名", in = ParameterIn.QUERY),
    @Parameter(name = "status", description = "用户状态", in = ParameterIn.QUERY),
    @Parameter(name = "pageNum", description = "页码", in = ParameterIn.QUERY, required = true),
    @Parameter(name = "pageSize", description = "每页数量", in = ParameterIn.QUERY)
})
public ResultData<PageResult<UserVO>> searchUsers(
    @RequestParam(required = false) String username,
    @RequestParam(required = false) Integer status,
    @RequestParam(defaultValue = "1") Integer pageNum,
    @RequestParam(defaultValue = "10") Integer pageSize
) {
    // ...
}
```

### 4.3 @ApiResponses - 响应数组

**作用：** 定义接口可能的响应

**示例：**
```java
@Operation(summary = "获取用户详情")
@GetMapping("/{id}")
@ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "查询成功",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = UserVO.class)
        )
    ),
    @ApiResponse(
        responseCode = "404",
        description = "用户不存在",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(example = "{\"code\": 404, \"msg\": \"用户不存在\"}")
        )
    ),
    @ApiResponse(
        responseCode = "500",
        description = "服务器内部错误",
        content = @Content()
    )
})
public ResultData<UserVO> getUser(@PathVariable Long id) {
    // ...
}
```

---

## 5. 参数级别注解

### 5.1 @Parameter - 参数说明

**作用：** 描述请求参数

**位置：** 方法参数上

**基本示例：**
```java
@Operation(summary = "获取用户详情")
@GetMapping("/{id}")
public ResultData<UserVO> getUser(
    @Parameter(description = "用户ID", required = true, example = "1234567890123456789")
    @PathVariable Long id
) {
    // ...
}
```

**详细示例：**
```java
@Parameter(
    name = "username",
    description = "用户名，长度3-20位",
    required = true,
    example = "admin",
    in = ParameterIn.QUERY,
    schema = @Schema(
        type = "string",
        minLength = 3,
        maxLength = 20,
        pattern = "^[a-zA-Z][a-zA-Z0-9_]*$"
    ),
    deprecated = false,
    allowEmptyValue = false
)
```

### 5.2 @ParameterIn - 参数位置

```java
public enum ParameterIn {
    DEFAULT,  // 默认
    QUERY,    // ?username=xxx
    PATH,     // /{id}
    HEADER,   // Header: Authorization
    COOKIE    // Cookie
}
```

### 5.3 参数注解结合使用

```java
@Operation(summary = "更新用户")
@PutMapping("/{id}")
public ResultData<Void> updateUser(
    @Parameter(description = "用户ID", in = ParameterIn.PATH, example = "1234567890123456789")
    @PathVariable Long id,
    
    @Parameter(description = "用户名", in = ParameterIn.QUERY, example = "newadmin")
    @RequestParam(required = false) String username,
    
    @Parameter(description = "请求ID", in = ParameterIn.HEADER, example = "uuid-xxx")
    @RequestHeader(value = "X-Request-Id", required = false) String requestId,
    
    @RequestBody UserUpdateDTO dto
) {
    // ...
}
```

---

## 6. 字段级别注解

### 6.1 @Schema - 字段定义

**作用：** 描述 DTO/VO/Entity 等数据模型的字段

**位置：** 字段上

**基本示例：**
```java
@Data
public class UserVO {
    @Schema(description = "用户ID", example = "1234567890123456789")
    private Long id;

    @Schema(description = "用户名", example = "admin")
    private String username;

    @Schema(description = "邮箱", example = "admin@example.com")
    private String email;
}
```

**详细示例：**
```java
@Schema(
    description = "用户ID",
    example = "1234567890123456789",
    type = "integer",           // 类型：string, integer, number, boolean, array, object
    format = "int64",          // 格式：int64, date-time, email, uri 等
    minimum = "1",             // 最小值
    maximum = "9223372036854775807",  // 最大值
    minLength = 1,             // 最小长度
    maxLength = 255,           // 最大长度
    pattern = "^[a-zA-Z].*",   // 正则表达式
    requiredMode = Schema.RequiredMode.REQUIRED,  // 是否必填
    nullable = false,          // 是否可为空
    defaultValue = "0",        // 默认值
    title = "用户ID",           // 标题
    deprecated = false,        // 是否废弃
    hidden = false             // 是否隐藏
)
private Long id;
```

### 6.2 @Schema 的 requiredMode

```java
public enum RequiredMode {
    REQUIRED,    // 必填
    NOT_REQUIRED,// 非必填
    AUTO         // 自动判断（根据 @NotNull 等注解）
}
```

### 6.3 复杂字段示例

```java
@Data
public class UserCreateDTO {
    @Schema(
        description = "用户名",
        example = "admin",
        requiredMode = Schema.RequiredMode.REQUIRED,
        minLength = 3,
        maxLength = 20
    )
    @NotBlank(message = "用户名不能为空")
    private String username;

    @Schema(
        description = "密码",
        example = "******",
        requiredMode = Schema.RequiredMode.REQUIRED,
        minLength = 6,
        maxLength = 20
    )
    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 20, message = "密码长度6-20位")
    private String password;

    @Schema(
        description = "邮箱",
        example = "admin@example.com",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    @Email(message = "邮箱格式不正确")
    private String email;

    @Schema(
        description = "手机号",
        example = "13800138000",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        pattern = "^1[3-9]\\d{9}$"
    )
    private String phone;

    @Schema(
        description = "用户状态",
        example = "1",
        allowableValues = {"0", "1"},
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private Integer status;
}
```

### 6.4 嵌套对象

```java
@Data
public class OrderVO {
    @Schema(description = "订单ID")
    private Long orderId;

    @Schema(description = "订单金额")
    private BigDecimal amount;

    @Schema(description = "用户信息")
    private UserVO user;  // 嵌套对象，自动引用

    @Schema(description = "订单项列表")
    private List<OrderItemVO> items;  // 嵌套列表
}
```

---

## 7. 完整示例

### 7.1 Controller 完整示例

```java
package com.overthinker.cloud.auth.controller;

import com.overthinker.cloud.auth.dto.UserCreateDTO;
import com.overthinker.cloud.auth.dto.UserUpdateDTO;
import com.overthinker.cloud.auth.service.UserService;
import com.overthinker.cloud.auth.vo.UserVO;
import com.overthinker.cloud.common.core.resp.ResultData;
import com.overthinker.cloud.common.core.util.PageResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 用户管理控制器
 *
 * @author overthinker
 */
@Tag(name = "用户管理", description = "提供用户相关的增删改查接口")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 分页查询用户列表
     */
    @Operation(
        summary = "分页查询用户",
        description = "支持按用户名、状态筛选，支持分页"
    )
    @GetMapping
    @Parameters({
        @Parameter(name = "username", description = "用户名（模糊匹配）", in = ParameterIn.QUERY),
        @Parameter(name = "status", description = "用户状态", in = ParameterIn.QUERY, example = "1"),
        @Parameter(name = "pageNum", description = "页码", in = ParameterIn.QUERY, required = true, example = "1"),
        @Parameter(name = "pageSize", description = "每页数量", in = ParameterIn.QUERY, example = "10")
    })
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "查询成功",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = PageResult.class)
            )
        )
    })
    public ResultData<PageResult<UserVO>> pageUsers(
        @RequestParam(required = false) String username,
        @RequestParam(required = false) Integer status,
        @RequestParam(defaultValue = "1") Integer pageNum,
        @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        PageResult<UserVO> result = userService.pageUsers(username, status, pageNum, pageSize);
        return ResultData.success(result);
    }

    /**
     * 获取用户详情
     */
    @Operation(summary = "获取用户详情", description = "根据用户ID获取用户详细信息")
    @GetMapping("/{id}")
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "查询成功",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = UserVO.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "用户不存在",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "NotFound",
                    summary = "用户不存在",
                    value = "{\"code\": 404, \"msg\": \"用户不存在\"}"
                )
            )
        )
    })
    public ResultData<UserVO> getUser(
        @Parameter(description = "用户ID", required = true, example = "1234567890123456789")
        @PathVariable Long id
    ) {
        UserVO user = userService.getUserById(id);
        return ResultData.success(user);
    }

    /**
     * 创建用户
     */
    @Operation(
        summary = "创建用户",
        description = "创建新用户，包含用户名、密码、邮箱等基本信息"
    )
    @PostMapping
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "创建成功"),
        @ApiResponse(responseCode = "400", description = "参数错误或用户名已存在")
    })
    public ResultData<Void> createUser(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "用户信息",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = UserCreateDTO.class),
                examples = @ExampleObject(
                    name = "创建用户",
                    summary = "创建管理员用户",
                    value = "{\"username\": \"admin\", \"password\": \"123456\", \"email\": \"admin@example.com\"}"
                )
            )
        )
        @Valid @RequestBody UserCreateDTO dto
    ) {
        userService.createUser(dto);
        return ResultData.success();
    }

    /**
     * 更新用户
     */
    @Operation(summary = "更新用户", description = "根据用户ID更新用户信息")
    @PutMapping("/{id}")
    public ResultData<Void> updateUser(
        @Parameter(description = "用户ID", required = true, example = "1234567890123456789")
        @PathVariable Long id,
        
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "用户信息",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = UserUpdateDTO.class)
            )
        )
        @Valid @RequestBody UserUpdateDTO dto
    ) {
        userService.updateUser(id, dto);
        return ResultData.success();
    }

    /**
     * 删除用户
     */
    @Operation(summary = "删除用户", description = "根据用户ID删除用户（软删除）")
    @DeleteMapping("/{id}")
    public ResultData<Void> deleteUser(
        @Parameter(description = "用户ID", required = true, example = "1234567890123456789")
        @PathVariable Long id
    ) {
        userService.deleteUser(id);
        return ResultData.success();
    }
}
```

### 7.2 DTO/VO 完整示例

```java
package com.overthinker.cloud.auth.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户视图对象
 */
@Data
@Schema(description = "用户视图对象")
public class UserVO {

    @Schema(description = "用户ID", example = "1234567890123456789")
    private Long id;

    @Schema(description = "用户名", example = "admin")
    private String username;

    @Schema(description = "昵称", example = "管理员")
    private String nickname;

    @Schema(description = "邮箱", example = "admin@example.com")
    private String email;

    @Schema(description = "手机号", example = "138****8000")
    private String phone;

    @Schema(description = "头像URL", example = "https://example.com/avatar.jpg")
    private String avatar;

    @Schema(description = "用户状态：0-禁用 1-启用", example = "1", allowableValues = {"0", "1"})
    private Integer status;

    @Schema(description = "用户角色", example = "admin")
    private String roleName;

    @Schema(description = "创建时间", example = "2024-01-15 10:30:00")
    private LocalDateTime createTime;

    @Schema(description = "最后登录时间")
    private LocalDateTime lastLoginTime;
}
```

---

## 8. 最佳实践

### 8.1 命名规范

| 建议 | 示例 |
|------|------|
| ✅ 使用中文 `description` | `@Schema(description = "用户ID")` |
| ✅ 提供 `example` 示例 | `@Schema(example = "123456")` |
| ✅ 标注必填字段 | `requiredMode = Schema.RequiredMode.REQUIRED` |
| ✅ 统一响应码说明 | `@ApiResponse(responseCode = "404", description = "资源不存在")` |
| ❌ 避免过长描述 | 保持简洁，控制在 50 字以内 |
| ❌ 避免模糊说明 | 不要使用 "用户信息" 这种模糊描述 |

### 8.2 示例值规范

```java
// ✅ 好的示例
@Schema(description = "用户ID", example = "1234567890123456789")
private Long id;

// ✅ 脱敏的示例
@Schema(description = "手机号", example = "138****8000")
private String phone;

// ❌ 避免真实敏感信息
@Schema(description = "密码", example = "myRealPassword123")  // 错误！
```

### 8.3 响应码规范

```java
@ApiResponses({
    @ApiResponse(responseCode = "200", description = "操作成功"),
    @ApiResponse(responseCode = "400", description = "请求参数错误"),
    @ApiResponse(responseCode = "401", description = "未授权访问"),
    @ApiResponse(responseCode = "403", description = "权限不足"),
    @ApiResponse(responseCode = "404", description = "资源不存在"),
    @ApiResponse(responseCode = "409", description = "资源冲突（如用户名已存在）"),
    @ApiResponse(responseCode = "500", description = "服务器内部错误")
})
```

### 8.4 分组展示技巧

```java
// 同一业务的不同接口放在同一 Tag 下
@Tag(name = "认证管理", description = "登录、注册、token管理等")
public class AuthController { }

// 不同业务使用不同 Tag
@Tag(name = "用户管理", description = "用户CRUD")
public class UserController { }

@Tag(name = "订单管理", description = "订单相关操作")
public class OrderController { }
```

### 8.5 隐藏内部接口

```java
@Hidden
@GetMapping("/health")
public String health() {
    return "OK";
}
```

### 8.6 版本控制

```java
@Tag(name = "用户管理 v1", description = "用户相关接口 v1版本")
@RestController
@RequestMapping("/v1/users")
public class UserControllerV1 { }

@Tag(name = "用户管理 v2", description = "用户相关接口 v2版本（新增字段）")
@RestController
@RequestMapping("/v2/users")
public class UserControllerV2 { }
```

---

## 附录

### A. 常用注解速查表

| 注解 | 位置 | 说明 |
|------|------|------|
| `@Tag` | Controller 类 | API 分组 |
| `@Operation` | 方法 | API 操作说明 |
| `@Parameter` | 参数 | 参数说明 |
| `@Parameters` | 方法 | 多参数说明 |
| `@ApiResponse` | 方法 | 单个响应说明 |
| `@ApiResponses` | 方法 | 多响应说明 |
| `@Schema` | 字段 | 数据模型字段 |
| `@Hidden` | 类/方法 | 隐藏接口 |
| `@SecurityRequirement` | 类/方法 | 安全需求 |

### B. Schema type 常用类型

| type | 说明 | 示例 |
|------|------|------|
| `string` | 字符串 | "hello" |
| `integer` | 整数 | 123 |
| `number` | 数字 | 123.45 |
| `boolean` | 布尔 | true/false |
| `array` | 数组 | [1, 2, 3] |
| `object` | 对象 | {key: value} |

### C. Schema format 常用格式

| format | 说明 |
|--------|------|
| `int32` | 32位整数 |
| `int64` | 64位整数 |
| `float` | 单精度浮点 |
| `double` | 双精度浮点 |
| `date` | 日期（yyyy-MM-dd） |
| `date-time` | 日期时间 |
| `email` | 邮箱 |
| `uri` | URI |
| `uuid` | UUID |
| `password` | 密码（加密显示） |

---

## 参考资料

- [OpenAPI 3.0 官方规范](https://spec.openapis.org/oas/v3.0.3)
- [SpringDoc OpenAPI 文档](https://springdoc.org/)
- [Swagger Annotations](https://github.com/swagger-api/swagger-core/wiki/Swagger-2.X---Annotations)

---

**文档版本：** v1.0  
**最后更新：** 2024年  
**维护者：** overthinker
