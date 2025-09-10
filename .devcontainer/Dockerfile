# 使用预装了 Java 17 的官方镜像
FROM mcr.microsoft.com/devcontainers/java:1-17

# 切换到 root 用户，确保安装权限
USER root

# 设置环境变量 NVS_HOME
ENV NVS_HOME="/root/.nvs"

# 运行一系列命令：克隆 nvs，安装 Node.js 和 gemini-cli
RUN git clone https://github.com/jasongin/nvs "$NVS_HOME" \
    && . "$NVS_HOME/nvs.sh" install \
    && nvs add 24 \
    && nvs use 24 \
    && nvs link 24 \
    && npm install -g @google/gemini-cli

# 创建 /root/arthas 目录并下载 arthas-boot.jar
# 使用独立的 RUN 指令以确保目录创建和下载操作的原子性
RUN mkdir -p /root/arthas \
    && curl -o /root/arthas/arthas-boot.jar https://arthas.aliyun.com/arthas-boot.jar

# 将 nvs 的 source 命令写入 shell 配置文件，确保每次启动容器时自动加载
RUN echo '. "$NVS_HOME/nvs.sh"' >> /root/.bashrc

# 设置工作目录
WORKDIR /root/workspace

# 定义默认命令
CMD ["/bin/bash"]