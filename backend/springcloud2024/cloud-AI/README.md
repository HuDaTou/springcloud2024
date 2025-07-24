# Cloud AI 模块

本模块专注于将人工智能（AI）功能集成到项目中。它利用 Spring AI 库来连接各种 AI 模型，并提供函数调用（Function Calling）、检索增强生成（RAG）等服务。

## 主要功能

### 1. 天气查询 (示例案例)

这是一个基础的函数调用演示，允许AI查询模拟的天气数据。

-   **API 端点:** `POST /ai/function-call`
-   **实现细节:**
    -   `FunctionCallController.java`: 提供 API 端点。
    -   `config/FunctionConfiguration.java`: 定义 `weatherFunction`。

### 2. 股票价格查询 (示例案例)

这是一个更独立的函数调用案例，允许AI查询模拟的股票价格。

-   **API 端点:** `POST /ai/stock-price`
-   **实现细节:**
    -   `StockPriceController.java`: 提供专用的 API 端点。
    -   `config/StockPriceFunctionConfiguration.java`: 定义 `stockPriceFunction`。

### 3. 自然语言操作数据库 (高级案例)

这是一个高级案例，演示了如何使用AI通过自然语言指令来操作数据库（例如新增用户）。它完全基于非阻塞的响应式技术栈实现。

-   **API 端点:** `POST /ai/manage-user`
-   **技术栈:** Spring Data R2DBC, PostgreSQL
-   **实现细节:**
    -   `UserManagementController.java`: 提供 API 端点。
    -   `config/UserManagementFunctionConfig.java`: 定义 `addUserFunction`。
    -   `domain/User.java`: 数据实体。
    -   `repository/UserRepository.java`: 响应式数据库操作接口。

### 技术细节

-   **Spring AI 版本:** `1.1.0`
-   **AI 模型:** 配置为通过 Ollama 使用本地模型 (例如 `llama3.1:8b`)。
-   **配置文件:** 请参阅 `src/main/resources/application.yaml` 来配置 Ollama 的基础 URL 和模型名称。

### 如何测试

接口现在是**完全响应式**的，会以 **Server-Sent Events (SSE)** 的形式流式返回数据。使用 `curl` 测试时，请务必加上 `-N` 参数来禁用缓冲，以便实时观察到流式输出。

1.  确保您的 Ollama 服务正在运行，并且所需的模型已经下载 (例如 `ollama pull llama3.1:8b`)。
2.  确保 PostgreSQL 数据库已配置，并已创建 `users` 表。
3.  启动 `cloud-AI` 应用程序 (运行在 `8788` 端口)。
4.  使用 `curl` 等工具发送请求：

    **测试天气查询 (流式):**
    ```bash
    curl -N -X POST http://localhost:8788/ai/function-call \
    -H "Content-Type: text/plain" \
    -d "波士顿天气怎么样？"
    ```

    **测试股票价格查询 (流式):**
    ```bash
    curl -N -X POST http://localhost:8788/ai/stock-price \
    -H "Content-Type: text/plain" \
    -d "英伟达的股价是多少？"
    ```

    **测试自然语言新增用户 (流式):**
    ```bash
    curl -N -X POST http://localhost:8788/ai/manage-user \
    -H "Content-Type: text/plain" \
    -d "帮我创建一个新用户，用户名是 zhangsan，邮箱是 zhangsan@example.com"
    ```

---
*本文档将随着模块新功能的增加而同步更新。*

