# 使用预装了 Java 17 的官方镜像
FROM mcr.microsoft.com/devcontainers/java:1-17

# 切换到 root 用户，确保安装权限
USER root

# 设置环境变量 NVS_HOME
ENV NVS_HOME="/root/.nvs"

# 克隆 nvs 仓库并运行 nvs 安装脚本
# 使用 && 将多条命令合并为一条 RUN 指令，减少镜像层数
RUN git clone https://github.com/jasongin/nvs "$NVS_HOME" \
    && . "$NVS_HOME/nvs.sh" install \
    # 添加并安装 Node.js 24 LTS 版本
    && nvs add 24 \
    # 切换到 Node.js 24 版本
    && nvs use 24 \
    # 将 Node.js 24 版本设置为默认，这样即使重新启动容器，它也会是默认版本
    && nvs link 24 \
    # 使用 npm 安装 @google/gemini-cli
    && npm install -g @google/gemini-cli

# 将 nvs 的 source 命令写入 shell 配置文件，确保每次启动容器时自动加载
RUN echo '. "$NVS_HOME/nvs.sh"' >> /root/.bashrc

# 设置工作目录
WORKDIR /root/workspace

# 定义默认命令
CMD ["/bin/bash"]