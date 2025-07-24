# 系统 Redis Starter (system-redis-starter)

本微服务启动器 (starter) 为项目中的所有服务提供了一个统一的、自动配置的、开箱即用的 Redis 集成方案。

## 核心功能

1.  **自动配置**: 任何微服务只需在 `pom.xml` 中添加此 starter 作为依赖，即可自动获得与 Redis 的连接能力，无需任何手动配置（默认连接 `localhost:6379`）。

2.  **优化的 `RedisTemplate`**: 本 starter 预先配置了一个 `RedisTemplate` 的 Bean。它解决了 Spring Boot 默认 `RedisTemplate` 的主要痛点：
    -   **Key 序列化**: 使用 `StringRedisSerializer`，确保您在 Redis 客户端中看到的 key 是可读的字符串，而不是乱码。
    -   **Value 序列化**: 使用 `GenericJackson2JsonRedisSerializer`，将存入的 Java 对象自动序列化为易于阅读和调试的 JSON 格式。

3.  **便捷工具类 `MyRedisCache`**: 本 starter 包含了一个强大的工具类 `MyRedisCache`，它封装了对 `RedisTemplate` 的常用操作，提供了大量语义化的便捷方法。

## 使用方法

在任何需要使用 Redis 的微服务中（例如 `cloud-web` 或 `cloud-auth`），请执行以下两个步骤：

### 1. 添加 Maven 依赖

在您的微服务的 `pom.xml` 文件中，添加对此 starter 的依赖：

```xml
<dependency>
    <groupId>com.overthinker.cloud</groupId>
    <artifactId>system-redis-starter</artifactId>
    <version>${project.version}</version>
</dependency>
```

### 2. 注入并使用 `MyRedisCache`

在您的 Service 或 Component 类中，直接通过 `@Resource` 或 `@Autowired` 注入 `MyRedisCache` 即可使用。

**代码示例：**

```java
@Service
public class YourService {

    @Resource
    private MyRedisCache redisCache;

    public void someMethod() {
        // 缓存一个字符串，有效期为5分钟
        redisCache.setCacheObject("myKey", "myValue", 5, TimeUnit.MINUTES);

        // 获取缓存的对象
        String value = redisCache.getCacheObject("myKey");

        // ... 其他操作
        User user = new User();
        user.setId(1L);
        user.setUsername("testUser");
        redisCache.setCacheObject("user:1", user);
    }
}
```

通过使用此 starter，您可以极大地简化 Redis 在各个微服务中的集成工作，并确保整个项目使用一套统一、高效、可读的缓存标准。

---
*本文档最后更新于 2025-07-25。*
