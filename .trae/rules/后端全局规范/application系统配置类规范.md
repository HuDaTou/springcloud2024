---
alwaysApply: false
globs: 
  - "**/config/*Config.java"
  - "!**/test/**/*"
---
# 配置类规范

## 配置文件组织

```
resources/
├── application.yml              # 主配置
├── application-dev.yml          # 开发环境
├── application-prod.yml         # 生产环境
└── mapper/                      # MyBatis XML
```

## 配置类规范

```java
@Data
@Configuration
@ConfigurationProperties(prefix = "my-project.minio")
public class MinioProperties {
    private String endpoint;
    private String accessKey;
    private String secretKey;
    private String bucketName;
}

@Configuration
@RequiredArgsConstructor
public class MinioConfig {
    private final MinioProperties properties;

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
            .endpoint(properties.getEndpoint())
            .credentials(properties.getAccessKey(), properties.getSecretKey())
            .build();
    }
}
```

## 敏感信息管理

❌ **禁止**：在配置文件中硬编码敏感信息

✅ **必须**：敏感信息通过环境变量管理

```bash
# .env 文件
DB_PASSWORD=xxxx
MINIO_ACCESS_KEY=xxxx
MINIO_SECRET_KEY=xxxx
JWT_SECRET=xxxx
```

```yaml
# application.yml
spring:
  datasource:
    password: ${DB_PASSWORD}
```
