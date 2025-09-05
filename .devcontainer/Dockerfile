# 使用预装了 Java 17 的官方镜像
FROM mcr.microsoft.com/devcontainers/java:1-17

# 切换到 root 用户，以便在 /root 目录下执行安装操作
USER root

# 将工作目录设置为 /root/workspace，以符合您的要求
WORKDIR /root/workspace

# 将 nvs 克隆到 root 用户的家目录 (~/ 或 /root/) 下，
# 验证 nvs 安装，然后安装 Node.js 24 LTS 版本，并将其设置为默认。
# 最后，安装 @google/gemini-cli
# 所有命令合并到单个 RUN 层，以提高构建效率。
RUN git clone https://github.com/jasongin/nvs /root/.nvs && \
    . "/root/.nvs/nvs.sh" && \
    nvs version || (echo "nvs version check failed, attempting re-installation..." && rm -rf /root/.nvs && git clone https://github.com/jasongin/nvs /root/.nvs && . "/root/.nvs/nvs.sh") && \
    nvs add 24 && \
    nvs use 24 && \
    npm install -g @google/gemini-cli
