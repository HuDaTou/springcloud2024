# 使用官方的OpenJDK 17镜像作为基础镜像
FROM registry.cn-chengdu.aliyuncs.com/base-images-all/openjdk:17

# 设置工作目录
WORKDIR /app

# 复制项目的jar文件到容器中
COPY cloud-web-1.0-SNAPSHOT.jar /app/app.jar

# 暴露应用运行的端口
EXPOSE 8000

# 运行Spring Boot应用
ENTRYPOINT ["java", "-jar", "/app/app.jar"]