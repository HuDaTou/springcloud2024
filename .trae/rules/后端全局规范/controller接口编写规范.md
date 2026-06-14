---
alwaysApply: false
globs: **/controller/*Controller.java
---
# Spring Cloud Java 后端项目开发规范 - Controller 接口编写规范
## 一、强制要求（AI必须严格遵守，违反即错误）
1. **类结构与注解**
   <!-- - 必须使用 `@RestController` + `@RequestMapping("/api/v{version}/[模块名]")` 统一前缀和版本号 -->
      - 必须使用 `@RestController` + `@RequestMapping("/api/[模块名]")` 统一前缀

   - 必须添加 `@Tag(name = "[模块名]管理", description = "[模块名]相关接口")` Swagger文档注解
   - 必须使用 `@RequiredArgsConstructor` 注入依赖，**绝对禁止使用 `@Autowired`**
   - 所有依赖必须声明为 `private final`

2. **接口语义与HTTP方法**
   - 查询单个：`@GetMapping("/{id}")`
   - 查询列表/分页：`@GetMapping`
   - 新增：`@PostMapping`
   - 修改全量：`@PutMapping("/{id}")`
   - 修改部分：`@PatchMapping("/{id}")`
   - 删除：`@DeleteMapping("/{id}")`
   - 批量操作：`@PostMapping("/batch/[操作名]")`

3. **参数处理**
   - 所有请求体必须添加 `@RequestBody @Valid` 开启参数校验
   - 所有路径参数必须添加 `@PathVariable` 和 `@Parameter(description = "[参数说明]")`
   - 所有查询参数必须添加 `@RequestParam` 或 `@ModelAttribute`，禁止无注解接收参数
   - 分页参数统一使用项目通用 `PageParams` 类，禁止单独声明 pageNum/pageSize

4. **返回值与异常**
   - 所有接口必须返回统一包装类 `ResultData<T>`，禁止直接返回业务对象
   - 禁止在Controller层捕获业务异常，异常统一由全局异常处理器处理
   - 禁止返回 `null`，空集合返回 `Collections.emptyList()`，空对象返回 `ResultData.success(null)`

5. **Swagger文档**
   - 所有接口必须添加 `@Operation(summary = "[接口功能]", description = "[详细说明，可选]")`
   - 所有参数必须添加 `@Parameter(description = "[参数说明]")`
   - 禁止使用 `@ApiIgnore` 隐藏生产环境接口

## 二、推荐做法（AI优先采用）
1. 批量删除接口使用 `@PostMapping("/batch/delete")` 接收ID列表，避免GET请求超长问题
2. 复杂查询接口使用 `@ModelAttribute` 接收查询条件DTO，避免参数过多
3. 所有接口添加 `@PreAuthorize` 权限控制注解，如 `@PreAuthorize("hasAuthority('system:user:list')")`
4. 导入导出接口统一使用 `POST` 方法，避免中文参数乱码问题

## 三、禁止行为（AI绝对不能生成）
1. 禁止在Controller层编写任何业务逻辑，只能做参数接收、Service调用和结果返回
2. 禁止使用 `System.out.println()` 打印日志，必须使用 `@Slf4j` 注解和SLF4J日志
3. 禁止硬编码任何业务常量、配置信息或敏感数据
4. 禁止直接拼接SQL语句，禁止在Controller层操作数据库
5. 禁止返回前端不需要的字段，所有返回数据必须通过VO对象包装

## 四、标准代码示例
```java
@RestController
@RequestMapping("/api/users")
@Tag(name = "用户管理", description = "用户相关接口")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping
    @Operation(summary = "分页查询用户列表", description = "根据条件分页查询用户信息")
    @PreAuthorize("hasAuthority('system:user:list')")
    public ResultData<PageResult<UserVO>> listUsers(
        @Parameter(description = "分页参数") @ModelAttribute PageParams pageParams,
        @Parameter(description = "查询条件") @ModelAttribute UserQueryDTO queryDTO
    ) {
        PageResult<UserVO> result = userService.pageUsers(pageParams, queryDTO);
        return ResultData.success(result);
    }

    @GetMapping("/{id}")
    @Operation(summary = "查询用户详情")
    @PreAuthorize("hasAuthority('system:user:query')")
    public ResultData<UserVO> getUser(
        @Parameter(description = "用户ID", required = true) @PathVariable @NotNull Long id
    ) {
        UserVO user = userService.getVOById(id);
        return ResultData.success(user);
    }

    @PostMapping
    @Operation(summary = "创建用户")
    @PreAuthorize("hasAuthority('system:user:add')")
    public ResultData<Void> createUser(
        @Parameter(description = "用户信息", required = true) @RequestBody @Valid UserCreateDTO dto
    ) {
        userService.create(dto);
        log.info("创建用户成功，用户名：{}", dto.getUsername());
        return ResultData.success();
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新用户信息")
    @PreAuthorize("hasAuthority('system:user:edit')")
    public ResultData<Void> updateUser(
        @Parameter(description = "用户ID", required = true) @PathVariable @NotNull Long id,
        @Parameter(description = "用户信息", required = true) @RequestBody @Valid UserUpdateDTO dto
    ) {
        userService.update(id, dto);
        log.info("更新用户成功，用户ID：{}", id);
        return ResultData.success();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除用户")
    @PreAuthorize("hasAuthority('system:user:delete')")
    public ResultData<Void> deleteUser(
        @Parameter(description = "用户ID", required = true) @PathVariable @NotNull Long id
    ) {
        userService.delete(id);
        log.info("删除用户成功，用户ID：{}", id);
        return ResultData.success();
    }

    @PostMapping("/batch/delete")
    @Operation(summary = "批量删除用户")
    @PreAuthorize("hasAuthority('system:user:delete')")
    public ResultData<Void> batchDeleteUsers(
        @Parameter(description = "用户ID列表", required = true) @RequestBody @NotEmpty List<Long> ids
    ) {
        userService.batchDelete(ids);
        log.info("批量删除用户成功，数量：{}", ids.size());
        return ResultData.success();
    }
}