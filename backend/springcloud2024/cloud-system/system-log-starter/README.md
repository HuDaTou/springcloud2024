# 系统日志启动器

## 1. 模块概述

`system-log-starter` 是一个 Spring Boot 启动器，为 `springcloud2024` 项目中的所有微服务提供统一且预配置的日志记录解决方案。它将默认的 Logback 日志记录替换为 [Log4j2](https.logging.apache.org/log4j/2.x/)，从而实现更高级、性能更强的日志记录功能。

通过包含此启动器，服务将自动继承标准化的日志记录配置，确保在整个应用程序中使用一致的日志格式、结构化日志记录（例如 JSON）和可配置的日志级别。

## 2. 核心功能

- **Log4j2 集成：** 无缝地将默认日志记录框架替换为 Log4j2。
- **集中化配置：** 提供一个可自定义的默认 `log4j2-spring.xml` 配置文件。
- **结构化日志记录：** 预先配置为以 JSON 等结构化格式输出日志，这对于日志聚合和分析工具（例如 ELK Stack、Splunk）非常理想。
- **性能：** 利用 Log4j2 异步日志记录功能的性能优势。

## 3. 关键依赖

- **Log4j2 Starter:** `org.springframework.boot:spring-boot-starter-log4j2`

## 4. 如何使用

要在微服务中启用集中式日志记录配置，请按照以下步骤操作：

### 第 1 步：排除默认日志记录

在微服务的 `pom.xml` 文件中，从 `spring-boot-starter` 或任何其他包含它的启动器中排除默认的 `spring-boot-starter-logging`。

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
    <exclusions>
        <exclusion>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-logging</artifactId>
        </exclusion>
    </exclusions>
</dependency>
```

### 第 2 步：添加日志启动器

将 `system-log-starter` 添加为依赖项：

```xml
<dependency>
    <groupId>com.overthinker.cloud</groupId>
    <artifactId>system-log-starter</artifactId>
    <version>${project.version}</version>
</dependency>
```

现在，该服务将自动使用此启动器提供的 Log4j2 配置。您可以通过在服务的 `src/main/resources` 目录中覆盖 `log4j2-spring.xml` 文件来进一步自定义日志记录行为。