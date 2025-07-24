# 系统日志 Starter (system-log-starter)

本启动器 (starter) 基于 SLF4J 和 Log4j2，为项目中的所有微服务提供了一个集中的、自动配置的日志解决方案。

## 核心功能

1.  **自动化配置**: 任何微服务只需在 `pom.xml` 中添加此 starter 作为依赖，就会自动完成以下配置：
    -   排除 Spring Boot 默认的 `spring-boot-starter-logging` (它使用 Logback)。
    -   引入 `spring-boot-starter-log4j2` 作为新的日志框架。

2.  **集中化配置**: 本 starter 内置了一个默认的 `log4j2-spring.xml` 配置文件。任何使用此 starter 的服务都将自动采纳这套日志配置，无需在自己的模块中放置任何日志配置文件。

## 默认日志行为

内置的 `log4j2-spring.xml` 已被配置为同时适用于开发和生产环境：

-   **控制台输出**: 日志会输出到控制台，便于在本地开发时进行调试。
-   **文件输出**: 日志会被写入到一个滚动文件中。
    -   **日志文件名**: 日志文件会根据服务名自动命名 (例如 `logs/cloud-web.log`)。这是通过在配置中引用 `${spring:spring.application.name}` 属性来实现的。
    -   **日志滚动策略**: 当日志文件达到 10MB 或每天开始时，会自动进行滚动（归档）。
    -   **日志路径**: 默认情况下，日志文件存储在应用程序根目录下的 `logs` 文件夹中。您可以通过在服务的 `application.yml` 中设置 `logging.file.path` 属性来更改此路径。

## 如何覆盖默认配置

如果默认的日志行为不满足某个微服务的特定需求，您可以轻松地覆盖它。

只需在您的微服务的 `src/main/resources` 目录下放置一个您自己的 `log4j2-spring.xml` 文件。根据 Spring Boot 的类加载优先级，这个本地的配置文件将会优先于本 starter 中提供的配置文件被加载。

例如，`cloud-web` 模块当前就保留了自己的 `log4j2-spring.xml`，这正是覆盖机制的一个实际例子。

---
*本文档最后更新于 2025-07-25。*