# Spring Cloud 2024 微服务项目

## 1. 项目概述

`springcloud2024` 是一个基于最新的 Spring Cloud、Spring Boot 和 Spring Cloud Alibaba 规范的综合性微服务项目。它可作为现代分布式应用程序的强大后端架构，集成了各种基本服务和中间件。

该项目旨在成为一个学习和生产就绪的样板，展示了微服务开发的最佳实践，包括服务发现、配置管理、网关路由、身份验证和可观察性。

## 2. 技术栈

- **核心框架：** Spring Boot 3.x、Spring Cloud 2024.0.0
- **服务治理：** Spring Cloud Alibaba（Nacos 用于服务发现和配置）
- **API 网关：** Spring Cloud Gateway
- **身份验证：** Spring Security、JWT
- **数据库：** MySQL、PostgreSQL、MyBatis-Plus
- **缓存：** Redis
- **消息队列：** RabbitMQ
- **人工智能：** Spring AI
- **规则引擎：** Drools
- **分布式作业调度：** XXL-Job
- **对象存储：** Minio
- **可观察性：** Micrometer、Zipkin
- **API 文档：** SpringDoc、Knife4j

## 3. 模块分解

该项目由以下模块组成：

| 模块 | 描述 |
| ---------------------------------- | ------------------------------------------------------------------------------------------------------------- |
| `cloud-api` | 定义要在服务之间共享的通用 API 接口和 DTO。 |
| `cloud-auth` | 负责用户登录和令牌颁发的集中式身份验证服务。 |
| `cloud-common` | 包含共享类、枚举和帮助函数的通用实用程序模块。 |
| `cloud-getway9527` | 将所有传入请求路由到相应微服务的中央 API 网关。 |
| `cloud-AI` | 一个用于使用 Spring AI 集成人工智能功能的实验模块。 |
| `cloud-drools` | 一个用于集成 Drools 规则引擎以动态管理和执行业务规则的模块。 |
| `cloud-Media-Asset-Processing` | 专门用于处理媒体资产处理（例如视频转码或图像大小调整）的服务。 |
| `cloud-web` | 包含核心应用程序逻辑和面向 Web 的控制器的主要业务模块。 |
| `cloud-system` | 各种系统级启动器组件的父模块。 |
| `system-auth-starter` | 一个用于向其他服务提供可重用身份验证和授权逻辑的启动器。 |
| `system-consul-starter` | 一个用于将 Consul 集成为服务发现客户端的启动器。 |
| `system-log-starter` | 一个用于集中式和结构化日志记录的启动器。 |
| `system-RabbitMQ-starter` | 一个用于轻松与 RabbitMQ 集成的启动器。 |
| `system-redis-starter` | 一个用于简化 Redis 集成和配置的启动器。 |
| `spring-study` | 一个用于试验和学习新 Spring 功能的沙箱模块。 |

## 4. 环境设置

要运行此项目，您需要在系统上安装以下软件：

- **Java 17+**
- **Maven 3.6+**
- **Docker 和 Docker Compose**（用于运行 Nacos、Redis、RabbitMQ 等中间件）
- **IDE**（推荐使用 IntelliJ IDEA 或 VS Code）

在启动应用程序之前，请确保所有必需的中间件服务（Nacos、Redis、RabbitMQ、MySQL）都已运行。您通常可以使用 Docker Compose 启动它们。

## 5. 如何运行

1. **克隆存储库：**
    ```bash
    git clone <your-repository-url>
    cd springcloud2024
    ```

2. **启动中间件：**
    使用 Docker 或您首选的方法启动所有必要的服务（Nacos、Redis 等）。确保可以从本地计算机访问它们。

3. **配置应用程序：**
    更新每个模块中的 `bootstrap.yaml` 和 `application.yaml` 文件，以匹配您的环境设置（例如，数据库凭据、Nacos 地址）。

4. **构建项目：**
    ```bash
    mvn clean install
    ```

5. **运行服务：**
    按以下顺序启动微服务：
    - `cloud-getway9527`
    - `cloud-auth`
    - `cloud-web`
    - 任何其他所需的服务。

您可以从 IDE 运行每个服务，也可以对已编译的 JAR 文件使用 `java -jar` 命令。