# Cloud AI 服务模块

## 1. 模块概述

`cloud-AI` 模块是一个专门用于将人工智能功能集成到 `springcloud2024` 生态系统中的微服务。它利用 Spring AI 项目来连接和使用大型语言模型 (LLM) 的服务。

该模块使用反应式技术栈 (`spring-boot-starter-webflux`) 构建，使其在处理异步和长时间运行的 AI 相关任务时非常高效。它专门配置为与 [Ollama](https://ollama.com/) 平台集成，从而可以与本地托管的语言模型进行交互。

## 2. 核心功能

- **LLM 集成：** 提供一个简单的界面，用于向语言模型发送提示并接收响应。
- **反应式端点：** 为所有 AI 交互公开非阻塞 API 端点，确保高吞吐量和可伸缩性。
- **集中式 AI 逻辑：** 封装所有与 AI 相关的功能，为需要利用 AI 的其他微服务提供单一联系点。

## 3. 关键依赖

- **Spring AI Starter (Ollama):** `org.springframework.ai:spring-ai-starter-model-ollama`
- **Spring WebFlux:** `org.springframework.boot:spring-boot-starter-webflux`
- **Spring Data R2DBC:** `org.springframework.boot:spring-boot-starter-data-r2dbc` (用于反应式数据库访问)
- **Testcontainers:** `org.testcontainers:ollama` (用于集成测试)

## 4. 配置

要启用 `cloud-AI` 模块，您需要在 `application.yaml` 文件中配置与 Ollama 实例的连接。

```yaml
spring:
  ai:
    ollama:
      base-url: http://localhost:11434  # 替换为您的 Ollama 服务器地址
      model: "llama2"  # 指定要使用的默认模型
```

请确保 Ollama 服务器正在运行，并且可以从 `cloud-AI` 服务访问。