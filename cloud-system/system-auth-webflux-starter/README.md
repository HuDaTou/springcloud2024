# system-auth-webflux-starter

WebFlux 版本的认证授权 starter，用于自动权限发现和注册。

## 功能特性

- JWT Token 校验与解析
- 方法级权限控制 (@PreAuthorize)
- 自动扫描 Controller 接口权限
- 通过 RabbitMQ 异步上报权限信息

## 依赖引入

```xml
<dependency>
    <groupId>com.overthinker.cloud.system</groupId>
    <artifactId>system-auth-webflux-starter</artifactId>
</dependency>
```

## 使用说明

1. 在 WebFlux 服务中引入此 starter
2. 确保配置了 OAuth2 Resource Server 的 JWT 验证
3. 在 Controller 方法上使用 `@PreAuthorize("hasAuthority('permission_code')")` 注解
4. 权限会在应用启动时自动扫描并注册到认证中心

## 配置项

```yaml
permission:
  scanner:
    enabled: true  # 是否启用权限扫描，默认启用
```

## 权限扫描规则

- 扫描所有标注了 `@RestController` 的类
- 从 `@PreAuthorize` 注解中提取权限标识
- 支持 OpenAPI `@Tag` 和 `@Operation` 注解提取描述信息

## 与 system-auth-starter 的区别

| 特性 | system-auth-starter | system-auth-webflux-starter |
|------|---------------------|-----------------------------|
| 基础框架 | Spring MVC | Spring WebFlux |
| 安全配置 | WebSecurityConfigurerAdapter | ServerHttpSecurity |
| 方法安全 | @EnableMethodSecurity | @EnableReactiveMethodSecurity |
| 工具类 | SecurityContextHolder | ReactiveSecurityContextHolder |