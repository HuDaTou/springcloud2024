# 系统认证启动器

## 1. 模块概述

`system-auth-starter` 是一个 Spring Boot 启动器，旨在为 `springcloud2024` 项目中的其他微服务提供集中化、可重用的身份验证和授权功能。

其主要职责是在任何将其作为依赖项的服务中自动扫描与权限相关的注解。然后，它与中央 `cloud-auth` 服务通信以注册这些权限，确保系统的访问控制列表始终保持最新。

## 2. 核心功能

- **自动权限发现：** 在应用程序启动时，扫描控制器方法上的 `@Operation` 和其他与安全相关的注解。
- **权限注册：** 使用 Feign 客户端将发现的权限发送到 `cloud-auth` 服务进行存储和管理。
- **简化的安全配置：** 提供一个预配置的 `SecurityFilterChain`，其他服务可以导入该配置以保护其端点。
- **解耦的认证逻辑：** 抽象了权限处理的细节，使服务开发人员能够专注于业务逻辑。

## 3. 关键依赖

- **Spring Cloud OpenFeign:** `org.springframework.cloud:spring-cloud-starter-openfeign`
- **Spring Boot Security:** `org.springframework.boot:spring-boot-starter-security`
- **SpringDoc OpenAPI:** `org.springdoc:springdoc-openapi-starter-common`
- **Cloud API:** `com.overthinker.cloud:cloud-api`（包含认证服务的 Feign 客户端接口）

## 4. 如何使用

要在另一个微服务（例如 `cloud-web`）中启用自动权限发现和注册，只需将其 `pom.xml` 文件中的此启动器添加为依赖项即可：

```xml
<dependency>
    <groupId>com.overthinker.cloud</groupId>
    <artifactId>system-auth-starter</artifactId>
    <version>${project.version}</version>
</dependency>
```

应用程序启动后，启动器将自动处理其余部分。请确保 `cloud-auth` 服务正在运行，并且可以通过服务发现机制（例如 Nacos）进行访问。