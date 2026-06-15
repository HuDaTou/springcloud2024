# Cloud Web 应用模块

## 1. 模块概述

`cloud-web` 是 `springcloud2024` 项目的**核心业务后端服务**，专注于博客/资讯类业务功能。它提供文章、视频、评论、留言、相册、友链、轮播图、点赞、收藏、树洞、菜单、网站信息、服务器监控等完整的 RESTful API，并作为面向用户交互的主要入口点。

该模块是一个综合性的 Spring Boot 应用，集成了项目内部的各种服务和启动器。它处理用户请求、执行业务逻辑、与数据库交互，并通过 Consul 服务发现注册到微服务体系中，由 `cloud-gatway9527` 网关统一路由转发。

> **关于角色与权限**：原 `Role`、`RoleMenu`、`ChatGpt` 等实体及对应业务已迁移至 `cloud-auth` 服务，本模块不再承担 RBAC 鉴权职责。菜单数据由 `cloud-auth` 提供。

## 2. 核心功能

### 2.1 内容管理
- **文章管理**：文章 CRUD、分类、标签、推荐、热门、随机、搜索、时间线、详情、阅读量统计
- **视频管理**：视频 CRUD、分类、标签、播放权限、播放量统计
- **轮播图管理**：首页轮播图 CRUD、排序
- **相册管理**：相册/照片 CRUD、文件信息（MinIO 存储）
- **友链管理**：友链申请、审核、上下线
- **网站信息管理**：站长信息、站点配置、备案信息、开站时间

### 2.2 互动功能
- **评论系统**：文章/视频评论、回复、楼中楼、邮件通知
- **点赞**：文章/视频点赞
- **收藏**：文章/视频收藏
- **树洞**：匿名树洞
- **留言板**：站点留言、邮件通知

### 2.3 系统管理
- **菜单管理**（数据已迁移 `cloud-auth`）
- **系统日志**：操作日志、登录日志
- **服务器监控**：基于 OSHI 获取 CPU、内存、JVM、磁盘、文件系统信息

### 2.4 业务支撑
- **RESTful API**：面向前端的业务接口
- **业务逻辑**：使用 MyBatis-Plus + Druid 实现高效可靠的数据库操作
- **分布式任务调度**：集成 XXL-Job
- **第三方集成**：JustAuth 第三方登录、邮件发送（评论/留言通知）、OkHttp 流式请求
- **对象存储**：MinIO（文件/图片上传）
- **缓存与消息**：Redis 缓存、RabbitMQ 异步消息
- **API 文档**：springdoc-openapi（OpenAPI 3）

## 3. 关键依赖

主要依赖（详见 `pom.xml`）：

- **Spring Boot Starter**：web、validation、thymeleaf、mail、amqp、quartz、test
- **数据库**：`mybatis-plus-spring-boot3-starter`、`druid-spring-boot-starter`、`p6spy-spring-boot-starter`
- **服务注册与发现**：`spring-cloud-starter-consul-discovery`
- **配置中心**：`spring-cloud-starter-consul-config`（`@RefreshScope` 动态刷新）
- **系统启动器**：
  - `system-redis-starter`：Redis 缓存
  - `system-auth-starter`：统一认证/鉴权（已弱化，本服务无 RBAC）
- **第三方库**：
  - `JustAuth 1.16.6`：第三方登录（QQ、GitHub、Gitee 等）
  - `okhttp3`：HTTP 客户端
  - `oshi-core 6.4.0` + `jna 5.12.1`：系统硬件信息采集
  - `UserAgentUtils 1.21`：浏览器/操作系统解析
  - `xxl-job-core`：分布式任务调度客户端
  - `minio`：MinIO 对象存储
  - `fastjson2`：JSON 序列化
- **公共模块**：
  - `cloud-common-web`：通用 Web 配置
  - `cloud-common-db`：通用数据库配置
  - `cloud-api`：公共 API/Feign DTO

## 4. 包结构

```
com.overthinker.cloud.web
├── cloudWebApplication.java      # 启动类
├── controller/                   # REST 控制器
│   ├── ArticleController         # 文章
│   ├── VideoController           # 视频
│   ├── CategoryController        # 分类
│   ├── TagController             # 标签
│   ├── CommentController         # 评论
│   ├── LeaveWordController       # 留言
│   ├── TreeHoleController        # 树洞
│   ├── LinkController            # 友链
│   ├── PhotoController           # 相册
│   ├── BannersController         # 轮播图
│   ├── FavoriteController        # 收藏
│   ├── LikeController            # 点赞
│   ├── WebsiteInfoController     # 站点信息
│   ├── ServerController          # 服务器监控
│   ├── SseController             # SSE 推送
│   └── TestController            # 测试
├── service/                      # 业务接口
│   └── impl/                     # 业务实现
├── handler/                      # 全局处理器
│   ├── GlobalExceptionControllerHandler   # 统一异常
│   ├── MyMetaObjectHandler                 # MyBatis-Plus 字段填充
│   └── RabbitListenerErrorHandler          # MQ 消费异常
└── entity/
    ├── PO/   # 持久化对象（数据库实体）
    ├── VO/   # 视图对象（接口返回）
    ├── DTO/  # 数据传输对象（接口入参）
    ├── enums/
    ├── email/
    └── server/
```

### 4.1 当前持久化实体（`entity/PO`）

| 实体类 | 表名 | 用途 |
| --- | --- | --- |
| `Article` | `t_article` | 文章 |
| `ArticleTag` | `t_article_tag` | 文章-标签关联 |
| `Video` | `t_video` | 视频 |
| `VideoTag` | `t_video_tag` | 视频-标签关联 |
| `Category` | `t_category` | 分类 |
| `Tag` | `t_tag` | 标签 |
| `Banners` | `t_banners` | 轮播图 |
| `Comment` | `t_comment` | 评论 |
| `Favorite` | `t_favorite` | 收藏 |
| `Like` | `t_like` | 点赞 |
| `LeaveWord` | `t_leave_word` | 留言 |
| `Link` | `t_link` | 友链 |
| `Photo` | `t_photo` | 相册/照片 |
| `TreeHole` | `t_tree_hole` | 树洞 |
| `Log` | `sys_log` | 操作日志 |
| `LoginLog` | `sys_login_log` | 登录日志 |
| `WebsiteInfo` | `sys_website_info` | 站点信息 |

> **注意**：`Server`、`Menu` 等类不在此模块维护。`Server` 为 OSHI 监控 VO；`Menu` 数据已迁移至 `cloud-auth`。

## 5. 配置

作为核心业务模块，`cloud-web` 需要配置多个外部服务。配置统一通过 Consul 或 `application.yaml` 提供：

- **数据源**：MySQL/PostgreSQL 连接信息（账号、密码、JdbcUrl、驱动类）
- **Redis**：连接信息、序列化方式
- **RabbitMQ**：连接信息、交换机/队列声明
- **Consul**：服务注册、配置中心地址
- **XXL-Job Admin**：调度中心地址、访问令牌
- **MinIO**：Endpoint、AccessKey、SecretKey、Bucket
- **邮件 SMTP**：评论/留言通知邮箱
- **第三方登录**：JustAuth 各平台 ClientId / ClientSecret

具体属性键请参考 `application.yaml` 与 Nacos/Consul 上的 `cloud-web.yaml`。

## 6. 数据库脚本

完整的 PostgreSQL 建表脚本位于仓库根目录的 `sql/blog.sql`，与本模块 `entity/PO` 一一对应。

主要特性：
- 所有主键使用 `BIGINT` 雪花 ID（应用层 MyBatis-Plus `IdType.ASSIGN_ID` 生成）
- 公共字段：`create_time`、`update_time`、`is_deleted`（逻辑删除）
- 关键字段已建索引（详见 `sql/blog.sql`）
- 原 `t_chat_gpt`、`sys_role`、`sys_role_menu` 等表已迁移至 `cloud-auth`

## 7. API 文档

应用启动后，可通过以下地址访问 OpenAPI 3 文档（springdoc-openapi）：

```
http://localhost:8080/v3/api-docs
http://localhost:8080/swagger-ui/index.html
```

> 历史版本曾集成 Knife4j；当前使用 springdoc-openapi-starter-webmvc-ui，路径以应用实际配置为准。

## 8. 启动与运行

### 8.1 本地启动

```bash
# 在 springcloud2024 根目录
mvn -pl cloud-web -am clean package -DskipTests
java -jar cloud-web/target/cloud-web.jar
```

### 8.2 远程调试

```bash
java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005 \
     -jar cloud-web/target/cloud-web.jar
```

### 8.3 Docker 启动

`cloud-web` 根目录已提供 `Dockerfile`，可执行：

```bash
docker build -t cloud-web .
docker run -p 8080:8080 cloud-web
```

## 9. 注意事项

- 本模块的雪花 ID 走 Jackson 全局 `Long → String` 序列化，避免前端 JS 精度丢失。
- 邮件通知（评论、留言）使用 RabbitMQ 异步发送，失败由 `RabbitListenerErrorHandler` 统一处理。
- 菜单、角色、权限相关接口已迁出，前端菜单与权限请统一调用 `cloud-auth` 服务（`/cloud-auth/menu`、`/cloud-auth/role`）。
- `cloud-web` 注册到 Consul 后，外部请求应通过 `cloud-gatway9527` 网关，路径前缀为 `/cloud-web`。
