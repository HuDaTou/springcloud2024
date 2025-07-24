# 媒体资产处理服务 (Cloud Media Asset Processing Service)

本微服务负责管理所有媒体资产，并使用 MinIO 提供一个健壮、高性能的文件上传解决方案。

## 核心功能：基于预签名URL的分片上传

本服务实现了一种专为大文件设计的先进上传策略。服务器不直接接收文件内容，而是扮演一个协调者的角色，协调客户端（如浏览器）将文件直接上传到 MinIO 对象存储中。

### 上传流程

1.  **初始化**: 客户端（例如Web前端）首先调用本服务的一个接口来发起上传请求。
2.  **协调**: 本服务与 MinIO 通信，创建一个新的分片上传任务，并获取一个唯一的 `uploadId`。
3.  **URL生成**: 随后，服务会为文件的每一个分片生成一个唯一的、预签名的URL。这些URL为客户端提供了临时的、安全的许可，允许其将特定的文件分片直接上传到 MinIO。
4.  **返回信息**: 服务将 `uploadId` 和预签名URL列表返回给客户端。
5.  **客户端上传**: 客户端浏览器使用这个列表，通过向预签名URL发送PUT请求，将每个文件块直接上传到 MinIO。
6.  **完成上传**: 所有分片上传完毕后，客户端需要调用另一个接口（`/media/complete`），并提供 `uploadId`，以通知服务器完成文件合并。

这种方法显著降低了应用服务器的负载，节省了带宽和内存，并充分利用了 MinIO 集群的可伸缩性。

## API 接口

### `POST /media/initiate`

初始化一个分片上传任务。

**查询参数:**

-   `filename` (字符串, 必需): 要上传的文件的名称 (例如, `我的大视频.mp4`)。
-   `totalParts` (整数, 必需): 文件被客户端分割的总块数。

**成功响应 (200 OK):**

一个包含客户端执行上传所需信息的JSON对象。

```json
{
  "uploadId": "从minio获取的唯一上传ID",
  "presignedUrls": {
    "1": "http://minio地址/桶/对象?partNumber=1&uploadId=...&X-Amz-Signature=...",
    "2": "http://minio地址/桶/对象?partNumber=2&uploadId=...&X-Amz-Signature=...",
    "3": "..."
  },
  "filename": "我的大视频.mp4",
  "totalParts": 3
}
```

## 配置

要运行此服务，您需要在 `application.yml` 中配置 MinIO 的连接详细信息：

```yaml
minio:
  endpoint: http://你的minio地址:9000
  accessKey: 你的minio-access-key
  secretKey: 你的minio-secret-key
  bucketName: 你的目标存储桶名称
```

---
*本文档最后更新于 2025-07-25。*