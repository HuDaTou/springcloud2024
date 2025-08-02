# 系统 Consul 启动器

## 1. 模块概述

`system-consul-starter` 是一个 Spring Boot 启动器，可简化 [HashiCorp Consul](https://www.consul.io/) 与 `springcloud2024` 项目的集成。它为服务发现和分布式配置管理提供自动配置。

通过在微服务中包含此启动器，该服务将自动向 Consul 代理注册自己，并从 Consul KV 存储中获取其配置，从而实现动态且有弹性的架构。

## 2. 核心功能

- **服务发现：** 自动向 Consul 注册服务，使其可被其他服务（包括 Spring Cloud Gateway）发现。
- **分布式配置：** 从 Consul KV 存储中获取应用程序配置，从而实现集中式和动态的属性管理。
- **健康检查：** 与 Spring Boot Actuator 集成，向 Consul 提供健康检查信息。

## 3. 关键依赖

- **Consul 发现客户端：** `org.springframework.cloud:spring-cloud-starter-consul-discovery`
- **Consul 配置客户端：** `org.springframework.cloud:spring-cloud-starter-consul-config`
- **Spring Cloud Bootstrap：** `org.springframework.cloud:spring-cloud-starter-bootstrap`（在启动时加载远程配置所需）

## 4. 如何使用

要在微服务中启用 Consul 集成，请执行以下两个步骤：

### 第 1 步：添加依赖

将此启动器添加到目标微服务的 `pom.xml` 中：

```xml
<dependency>
    <groupId>com.overthinker.cloud</groupId>
    <artifactId>system-consul-starter</artifactId>
    <version>${project.version}</version>
</dependency>
```

### 第 2 步：配置引导属性

在微服务的 `src/main/resources` 目录中创建一个 `bootstrap.yaml` 文件，并配置与 Consul 服务器的连接：

```yaml
spring:
  application:
    name: my-service  # 您的服务名称
  cloud:
    consul:
      host: localhost
      port: 8500
      config:
        enabled: true
        format: yaml
        data-key: config  # Consul KV 中存储配置的密钥
        prefix: /config
        profile-separator: '-'
      discovery:
        service-name: ${spring.application.name}
```

通过此配置，该服务将向 Consul 注册，并从 Consul KV 存储中的 `/config/my-service` 加载其配置。