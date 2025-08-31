# 开发容器 (Dev Container) 配置说明

本目录下的文件用于配置一个一致、可复现的开发环境，该环境在 Docker 容器中运行。这确保了所有开发者都使用相同的工具和配置，避免了 "在我机器上可以运行" 的问题。

## 文件说明

### `devcontainer.json`

这个是 Dev Container 的核心配置文件，它告诉 VS Code 或其他兼容 IDE 如何构建和管理开发环境。

-   `"name": "SpringCloud2024 Dev Container"`
    -   **作用**: 为开发容器指定一个易于识别的显示名称。

-   `"dockerComposeFile": "../docker-compose.yaml"`
    -   **作用**: 指定用于定义环境的 Docker Compose 文件路径。`../` 表示 `docker-compose.yaml` 文件位于当前目录（`.devcontainer`）的上一级目录，也就是项目的根目录。

-   `"service": "app"`
    -   **作用**: 指定在 `docker-compose.yaml` 文件中，我们选择哪一个服务 (`service`) 作为开发环境的主体。在这里，我们使用名为 `app` 的服务。IDE 将会 "附加" 到这个服务的容器中。

-   `"workspaceFolder": "/workspace"`
    -   **作用**: 指定当 IDE 连接到容器时，默认打开的工作区目录。这个路径应与 `docker-compose.yaml` 中挂载项目代码的卷（volume）路径一致。

-   `"remoteUser": "root"`
    -   **作用**: 指定在容器内部执行命令时使用的用户名。根据您的要求，这里已配置为 `root` 用户，以提供完整的系统权限。

-   `"customizations"`
    -   **作用**: 允许进行特定于 IDE 的配置。例如，为 VS Code 自动安装推荐的扩展，或为 JetBrains IDE 配置特定设置。

-   `"postCreateCommand": "mvn clean install -DskipTests"`
    -   **作用**: 定义一个在容器成功创建后自动执行的命令。这里用它来自动运行 `mvn clean install`，以便下载所有依赖并完成项目首次构建，节省了手动操作的时间。

### `Dockerfile`

-   **作用**: 这个文件是 `app` 服务的构建蓝图。它定义了开发环境的基础镜像（包含操作系统、Java 版本等）、安装所有必需的开发工具（如 Maven、Git 等），并创建 `remoteUser` 指定的用户。**（注意：此文件当前缺失，需要创建才能使开发容器正常工作）**

### 与 `docker-compose.yaml` 的关系

-   `devcontainer.json` 负责“指挥”，告诉 IDE 使用哪个 `docker-compose.yaml` 文件和其中的哪个 `service`。
-   `docker-compose.yaml` 负责“执行”，它实际定义了 `app` 服务（包括如何构建它或使用哪个镜像）、所有依赖的中间件服务（如数据库、Redis 等）、它们之间的网络以及数据持久化的卷。

简单来说，`devcontainer.json` 是开发容器的入口和配置，而 `docker-compose.yaml` 和 `Dockerfile` 则是构建这个环境的基石。
