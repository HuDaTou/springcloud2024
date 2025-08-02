# Cloud Web 应用模块

## 1. 模块概述

`cloud-web` 模块是 `springcloud2024` 项目的核心后端服务。它包含了核心业务逻辑、RESTful API 端点，并作为面向用户交互的主要入口点。

该模块是一个综合性的 Spring Boot 应用，集成了项目内部的各种服务和启动器，以提供功能齐全的 Web 应用。它处理用户请求、执行业务逻辑、与数据库交互，并与其他微服务通信。

## 2. 核心功能

- **RESTful API：** 提供广泛的 API，用于管理用户、处理订单及其他核心业务功能。
- **业务逻辑：** 包含应用的主要业务逻辑。
- **数据库交互：** 使用 MyBatis-Plus 和 Druid 实现高效可靠的数据库操作。
- **第三方集成：** 支持第三方登录（JustAuth）、邮件发送等。
- **分布式任务调度：** 集成 XXL-Job，用于管理定时任务。
- **全系统集成：** 利用所有系统启动器，实现日志记录、认证、缓存、消息传递和审计功能。

## 3. 关键依赖

该模块集成了多种依赖，包括：

- **Spring Web & WebFlux：** 用于构建 RESTful API。
- **数据库：** `mybatis-plus-spring-boot3-starter`、`druid-spring-boot-starter`、`mysql-connector-java`。
- **系统启动器：**
  - `system-redis-starter`
  - `system-rabbitmq-starter`
  - `system-audit-log-starter`
  - `system-auth-starter`
  - `system-log-starter`
- **第三方库：** `JustAuth`、`okhttp`、`oshi-core`、`xxl-job-core`、`minio`。

## 4. 配置

作为核心模块，`cloud-web` 需要配置多个外部服务。请确保在您的 `application.yaml` 文件中正确配置以下内容：

- **数据库连接**
- **Redis 连接**
- **RabbitMQ 连接**
- **Nacos/Consul 服务发现**
- **XXL-Job Admin 地址**
- **MinIO 连接详情**

具体的属性键，请参考配置文件。

## 5. API 文档

应用启动后，您可以通过以下 URL 访问由 Knife4j 生成的 API 文档：

```
http://localhost:8080/doc.html
```

如果端口号已更改，请将 `8080` 替换为实际的端口号。