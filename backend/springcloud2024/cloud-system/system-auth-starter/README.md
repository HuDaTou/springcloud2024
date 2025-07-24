# 系统认证 Starter (system-auth-starter)

本启动器 (starter) 提供了一套强大的**自动化权限发现与注册**机制。当任何微服务引入此 starter 后，它会在应用启动时自动扫描其所有 REST 接口，提取元数据，并将其作为权限注册到统一的 `cloud-auth` 认证中心。

## 核心特性

-   **零配置**: 只需添加依赖即可，无需额外配置。
-   **自动发现**: 在应用启动时，starter 会自动扫描所有的 `@RestController` Bean。
-   **元数据提取**: 从标准的 API 注解 (`@Tag`, `@Operation`, `@GetMapping` 等) 中提取权限的详细信息。
-   **集中化注册**: 所有发现的权限都会通过 REST API 调用发送到 `cloud-auth` 服务，确保您的权限表始终与代码保持同步。

## 工作原理

本 starter 会根据以下信息，构建一条唯一的权限记录：

1.  **权限分类**: 从类级别的 `@Tag(name = "文章管理")` 注解的 `name` 属性中提取。

2.  **权限名称**: 从方法级别的 `@Operation(summary = "按标题搜索文章")` 注解的 `summary` 属性中提取。

3.  **HTTP 方法**: 根据映射注解自动判断 (例如, `@GetMapping` 变为 `GET`)。

4.  **完整路径**: 通过拼接以下三部分构成：
    -   **服务路径前缀**: 从 `spring.application.name` 派生 (例如, `/cloud-web`)。
    -   **控制器基础路径**: 从类上的 `@RequestMapping` 注解获取。
    -   **方法路径**: 从方法上的 `@GetMapping` 等注解获取。
    -   *示例*: `/cloud-web/article/search/init/title`

## 开发者约定

为了确保您的接口能被正确地发现和注册，您**必须**遵守以下约定：

-   **控制器层面**: 每个 `@RestController` 类都必须有一个 `@Tag` 注解来定义其权限分类。
-   **方法层面**: 每个您希望注册为权限的公开接口方法 (`@GetMapping`, `@PostMapping` 等) **必须**有一个带有描述性 `summary` 的 `@Operation` 注解。

不符合这些标准的接口将被扫描器忽略。

## 配置

本 starter 被设计为零配置。但是，您也可以通过在 `application.yml` 或 `bootstrap.yml` 中设置以下属性，来覆盖默认的 `cloud-auth` 服务注册地址：

```yaml
cloud-auth:
  registration:
    url: http://你的认证服务地址/internal/api/v1/permissions/register
```

如果未指定，它将默认为 `http://cloud-auth/internal/api/v1/permissions/register`，这适用于已启用服务发现的环境。

---
*本文档最后更新于 2025-07-25。*