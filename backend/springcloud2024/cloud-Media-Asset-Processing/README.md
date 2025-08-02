# 媒体资产处理服务

## 1. 模块概述

`cloud-Media-Asset-Processing` 模块是一个专门的微服务，旨在处理 `springcloud2024` 项目中媒体资产的完整生命周期。它为上传、存储、检索和管理文件提供了集中式解决方案。

该服务直接与 [MinIO](https://min.io/)（一个高性能、与 S3 兼容的对象存储服务器）集成。它抽象了文件存储的复杂性，为其他服务提供了一套简单而安全的 API 端点。

## 2. 核心功能

- **分片上传：** 支持大文件的高效、可续传的分片上传机制。
- **文件下载与访问：** 提供文件的临时访问URL和直接下载功能。
- **对象存储集成：** 从 MinIO 存储桶中无缝存储和检索文件。
- **资产管理：** 管理所有媒体资产的元数据，并将信息存储在 PostgreSQL 数据库中。
- **安全访问：** 与 `system-auth-starter` 集成，以确保只有授权用户才能访问或修改媒体资产。
- **上传前校验：** 在上传初始化阶段对文件大小和类型进行校验，确保系统稳定性和安全性。

## 3. API 概览

该服务提供以下主要的 RESTful API 端点：

- `POST /media/initiate`: 初始化一个分片上传任务。客户端需提供文件名、总分片数、文件大小和类型。服务器返回一个唯一的 `uploadId` 和所有分片的预签名上传URL。
- `POST /media/complete`: 在所有分片上传完成后，通知服务器合并分片以完成整个文件的上传。
- `GET /media/list`: 分页列出已上传的媒体资产。
- `DELETE /media/delete/{objectName}`: 删除指定的媒体文件及其元数据。
- `GET /media/url/{objectName}`: 获取指定文件的一个临时的、有时效性的下载URL。

## 4. 关键依赖

- **MinIO 客户端：** `io.minio:minio`
- **Spring Web：** `org.springframework.boot:spring-boot-starter-web`
- **MyBatis-Plus：** `com.baomidou:mybatis-plus-spring-boot3-starter`
- **PostgreSQL 驱动程序：** `org.postgresql:postgresql`
- **系统启动器：** `system-consul-starter`、`system-auth-starter`、`system-redis-starter`

## 5. 配置

要运行此模块，您必须在 `application.yml` 文件中配置与 MinIO 和 PostgreSQL 数据库的连接。

### MinIO 配置

```yaml
minio:
  endpoint: http://localhost:9000
  accessKey: minioadmin
  secretKey: minioadmin
  bucketName: media
```

### 数据库配置

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/media_db
    username: user
    password: password
    driver-class-name: org.postgresql.Driver
```

在启动服务之前，请确保 MinIO 服务器和 PostgreSQL 数据库正在运行且可访问。