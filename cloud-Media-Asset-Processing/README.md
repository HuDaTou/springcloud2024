# 媒体资产处理服务

## 1. 模块概述

`cloud-Media-Asset-Processing` 是 `springcloud2024` 项目中的**独立媒体资产管理微服务**，专注于处理媒体文件的完整生命周期。它为文件上传、存储、检索和管理提供了集中式解决方案，是整个系统的文件处理核心。

该服务直接与 [MinIO](https://min.io/)（高性能、与 S3 兼容的对象存储服务器）深度集成，抽象了文件存储的复杂性，为其他服务提供了一套简单而安全的 API 端点。通过 Consul 服务发现注册到微服务体系，由 `cloud-gatway9527` 网关统一路由转发。

## 2. 核心功能

### 2.1 文件上传
- **分片上传**：支持大文件的高效、可续传的分片上传机制（断点续传）
- **预签名 URL**：生成安全的预签名上传/下载 URL，客户端可直接与 MinIO 交互
- **上传初始化**：在上传前校验文件大小和类型，确保系统稳定性和安全性
- **分片合并**：所有分片上传完成后，自动合并生成完整文件

### 2.2 文件管理
- **文件列表**：分页列出已上传的媒体资产及其元数据
- **文件详情**：获取指定媒体资产的详细信息（大小、类型、上传时间等）
- **文件删除**：删除指定的媒体文件及其元数据（同时清理 MinIO 和数据库）
- **临时访问**：生成有时效性的下载 URL（默认有效期可配置）

### 2.3 上传规则管理
- **规则配置**：管理文件上传规则（允许的文件类型、大小限制等）
- **规则校验**：上传前根据规则校验文件合法性

### 2.4 系统保障
- **异步清理**：定时清理过期的分片上传任务（`StaleUploadCleanupTask`）
- **安全访问**：与 `system-auth-starter` 集成，确保只有授权用户才能访问或修改媒体资产
- **元数据持久化**：所有媒体资产元数据存储在 PostgreSQL 数据库中

## 3. API 概览

### 3.1 分片上传接口

| 接口 | 方法 | 说明 |
| --- | --- | --- |
| `/media/initiate` | POST | 初始化分片上传任务，返回 `uploadId` 和预签名 URL |
| `/media/complete` | POST | 合并分片完成上传 |
| `/media/cancel` | POST | 取消上传并清理已上传的分片 |

### 3.2 媒体资产接口

| 接口 | 方法 | 说明 |
| --- | --- | --- |
| `/media/list` | GET | 分页查询媒体资产列表 |
| `/media/{id}` | GET | 获取单个媒体资产详情 |
| `/media/{id}` | PUT | 更新媒体资产信息 |
| `/media/{id}` | DELETE | 删除媒体资产 |
| `/media/url/{objectName}` | GET | 获取临时下载 URL |

### 3.3 上传规则接口

| 接口 | 方法 | 说明 |
| --- | --- | --- |
| `/rules/list` | GET | 分页查询上传规则列表 |
| `/rules/{id}` | GET | 获取单个规则详情 |
| `/rules` | POST | 创建上传规则 |
| `/rules/{id}` | PUT | 更新上传规则 |
| `/rules/{id}` | DELETE | 删除上传规则 |

## 4. 包结构

```
com.overthinker.cloud.media
├── Media.java                              # 启动类
├── controller/                             # REST 控制器
│   ├── MediaController                     # 分片上传核心接口
│   ├── MediaAssetController                # 媒体资产 CRUD
│   └── FileUploadRulesController           # 上传规则管理
├── service/                                # 业务接口
│   ├── MediaAssetService                   # 媒体资产服务
│   ├── FileUploadRulesService              # 上传规则服务
│   ├── UploadService                       # 上传服务
│   └── impl/                               # 服务实现类
├── mapper/                                 # MyBatis-Plus 数据访问层
│   ├── MediaAssetMapper
│   └── FileUploadRulesMapper
├── entity/
│   ├── PO/                                 # 持久化对象
│   │   ├── MediaAsset                      # 媒体资产实体
│   │   └── FileUploadRules                 # 上传规则实体
│   ├── VO/                                 # 视图对象
│   │   ├── MediaAssetVO
│   │   └── FileUploadRulesVO
│   └── DTO/                                # 数据传输对象
│       ├── InitiateMultipartUploadDTO
│       ├── PresignedUploadDTO
│       └── FileUploadRulesInfoDTO
├── config/                                 # 配置类
│   ├── MinioConfig                         # MinIO 客户端配置
│   ├── MinioProperties                     # MinIO 属性配置
│   ├── MediaProperties                     # 媒体服务属性
│   ├── MinioInitializationService          # MinIO 初始化（创建桶）
│   └── AsyncConfig                         # 异步任务配置
├── task/                                   # 定时任务
│   └── StaleUploadCleanupTask              # 清理过期上传任务
├── enums/                                  # 枚举类
│   └── MediaStatusEnum                     # 媒体状态枚举
├── validators/                             # 自定义校验器
│   └── FileFormatValidator                 # 文件格式校验
├── annotation/                             # 自定义注解
│   └── FileFormat                          # 文件格式注解
├── utils/                                  # 工具类
│   └── MediaUtils                          # 媒体工具类
└── constants/                              # 常量
    └── Const                               # 通用常量
```

### 4.1 持久化实体

| 实体类 | 表名 | 用途 |
| --- | --- | --- |
| `MediaAsset` | `media_asset` | 媒体资产元数据（文件名、大小、类型、存储路径等） |
| `FileUploadRules` | `file_upload_rules` | 上传规则（允许类型、大小限制、状态等） |

## 5. 关键依赖

| 依赖 | 版本 | 说明 |
| --- | --- | --- |
| `spring-boot-starter-web` | Spring Boot 3.x | Web 框架 |
| `io.minio:minio` | 最新 | MinIO 对象存储客户端 |
| `system-consul-starter` | 内部 | Consul 服务注册与发现 |
| `system-auth-starter` | 内部 | 统一认证/鉴权 |
| `system-redis-starter` | 内部 | Redis 缓存（分片上传状态） |
| `cloud-common-db` | 内部 | MyBatis-Plus + Druid 数据库配置 |
| `springdoc-openapi-starter-common` | 最新 | OpenAPI 文档 |

## 6. 配置

### 6.1 MinIO 配置

```yaml
minio:
  endpoint: http://localhost:9000
  access-key: minioadmin
  secret-key: minioadmin
  bucket-name: media
  presigned-expire-minutes: 60
```

### 6.2 数据库配置

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/example_db
    username: admin
    password: password
    driver-class-name: org.postgresql.Driver
```

### 6.3 媒体服务配置

```yaml
media:
  chunk-size: 5242880                    # 分片大小（默认 5MB）
  stale-upload-expire-hours: 24          # 过期上传清理时间
```

### 6.4 启动前准备

确保以下服务已运行且可访问：
- **MinIO Server**：对象存储服务
- **PostgreSQL**：数据库服务
- **Consul Server**：服务注册与配置中心（可选，开发环境可关闭）

## 7. API 文档

应用启动后，可通过以下地址访问 OpenAPI 3 文档：

```
http://localhost:8081/v3/api-docs
http://localhost:8081/swagger-ui/index.html
```

> 服务默认端口：`8081`（可通过 `server.port` 配置修改）

## 8. 启动与运行

### 8.1 本地启动

```bash
# 在 springcloud2024 根目录
mvn -pl cloud-Media-Asset-Processing -am clean package -DskipTests
java -jar cloud-Media-Asset-Processing/target/cloud-Media-Asset-Processing.jar
```

### 8.2 Docker 启动

```bash
docker build -t cloud-media-asset-processing .
docker run -p 8081:8081 cloud-media-asset-processing
```

## 9. 分片上传流程

```
┌──────────────┐     ┌───────────────┐     ┌───────────┐
│   客户端     │     │  MediaService │     │   MinIO   │
└──────┬───────┘     └───────┬───────┘     └─────┬─────┘
       │                      │                   │
       │ POST /media/initiate │                   │
       │─────────────────────>│                   │
       │                      │                   │
       │   { uploadId,       │                   │
       │    presignedUrls }   │                   │
       │<────────────────────│                   │
       │                      │                   │
       │                      │ PUT (预签名URL)   │
       │─────────────────────────────────────────>│
       │         [重复直到所有分片上传完成]        │
       │                      │                   │
       │ POST /media/complete │                   │
       │─────────────────────>│                   │
       │                      │  completeMultipart│
       │                      │──────────────────>│
       │                      │                   │
       │         { assetInfo }│                   │
       │<────────────────────│                   │
```

## 10. 注意事项

- **分片大小**：默认 5MB，可通过 `media.chunk-size` 配置调整
- **预签名 URL 有效期**：默认 60 分钟，可通过 `minio.presigned-expire-minutes` 配置
- **过期上传清理**：定时任务默认每小时执行一次，清理超过 24 小时的未完成上传
- **服务访问**：服务注册到 Consul 后，外部请求应通过网关，路径前缀为 `/cloud-Media-Asset-Processing`
- **权限控制**：所有接口均需通过 `system-auth-starter` 的认证校验，未认证请求将被拒绝
