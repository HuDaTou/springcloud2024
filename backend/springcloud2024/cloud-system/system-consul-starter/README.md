# 系统 Consul Starter (system-consul-starter)

本模块为项目中的所有微服务提供了一套标准化的 Consul 客户端配置，用于实现服务发现和分布式配置管理。

## 核心特性：自动配置服务名

本 starter 遵循“约定优于配置”的原则。

**它会自动为任何引入它的微服务配置服务名称。** 服务名将被自动设置为该微服务模块在其 `pom.xml` 中定义的 `artifactId`。

例如，如果 `cloud-web` 模块（其 `artifactId` 为 'cloud-web'）引入了此 starter，它将自动以 `cloud-web` 的名称注册到 Consul。

**除非有特殊需求需要覆盖，否则您无需在微服务的配置文件中指定 `spring.application.name`。**

## 多环境配置

本 starter 同时支持多环境配置，以便在本地开发和部署环境之间无缝切换。

### 配置文件

-   **`bootstrap.yml`**: 这是基础配置文件。它为 Consul 主机地址使用了一个占位符 (`${consul.host}`), 以便在部署时通过运行时参数来提供。
-   **`bootstrap-local.yml`**: 此文件包含了对 `local` (本地) 环境的配置覆盖，它将 Consul 主机地址设置为 `localhost`，便于开箱即用的本地开发。

### 使用方法

#### 1. 本地开发

在您的 IDE 的运行配置中，激活 `local` 这个 Spring profile。项目的根 `pom.xml` 已将其设置为默认激活，所以它通常会自动生效。

#### 2. 打包部署 (生产环境, K8s等)

当您运行打包后的 JAR 文件时，请不要激活 `local` profile。相反，您应该通过命令行参数来提供 Consul 的主机地址。

**示例:**

```bash
java -jar your-microservice.jar --spring.profiles.active=prod --consul.host=consul-server-address
```

---
*本文档最后更新于 2025-07-25，以反映自动服务名配置策略。*
