# System Redis 启动器

## 1. 模块概述

`system-redis-starter` 是一个 Spring Boot 启动器，为 `springcloud2024` 项目提供预配置且易于使用的 [Redis](https://redis.io/) 集成。它简化了缓存、会话管理和其他基于 Redis 的功能。

该启动器使用适当的序列化器（例如，键使用 `StringRedisSerializer`，值使用 `GenericJackson2JsonRedisSerializer`）自动配置 `RedisTemplate`，从而允许在不进行手动序列化的情况下存储和检索 Java 对象。

## 2. 核心功能

- **自动配置的 `RedisTemplate`：** 提供一个即用型的 `RedisTemplate` bean，用于直接与 Redis 交互。
- **JSON 序列化：** 配置序列化器以人类可读的 JSON 格式存储对象数据，提高了互操作性和可调试性。
- **连接池：** 利用 `commons-pool2` 为 Lettuce Redis 客户端启用高效的连接池。
- **缓存工具：** 通常包含一个 `CacheService` 或类似的工具类，该类包装 `RedisTemplate` 以提供方便、高级的缓存操作。

## 3. 关键依赖

- **Spring Data Redis Starter:** `org.springframework.boot:spring-boot-starter-data-redis`
- **Apache Commons Pool2:** `org.apache.commons:commons-pool2`
- **Cloud Common:** `com.overthinker.cloud:cloud-common`

## 4. 如何使用

要在微服务中启用 Redis，请按照以下步骤操作：

### 第 1 步：添加依赖

将此启动器添加到目标微服务的 `pom.xml` 中：

```xml
<dependency>
    <groupId>com.overthinker.cloud</groupId>
    <artifactId>system-redis-starter</artifactId>
    <version>${project.version}</version>
</dependency>
```

### 第 2 步：配置连接

在服务的 `application.yaml` 文件中，提供 Redis 服务器的连接详细信息：

```yaml
spring:
  data:
    redis:
      host: localhost
      port: 6379
      password:  # 可选：您的 Redis 密码
      database: 0
      lettuce:
        pool:
          max-active: 8
          max-idle: 8
          min-idle: 0
```

### 第 3 步：使用 RedisTemplate

将 `RedisTemplate` 或自定义的 `CacheService` 注入到您的组件中以与 Redis 交互。

```java
@Autowired
private RedisTemplate<String, Object> redisTemplate;

public void cacheUser(User user) {
    redisTemplate.opsForValue().set("user:" + user.getId(), user, 1, TimeUnit.HOURS);
}

public User getUser(String userId) {
    return (User) redisTemplate.opsForValue().get("user:" + userId);
}
```