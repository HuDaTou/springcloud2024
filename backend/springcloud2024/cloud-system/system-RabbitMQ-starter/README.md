# 系统 RabbitMQ Starter (system-rabbitmq-starter)

本微服务启动器 (starter) 为项目中的所有服务提供了一个统一的、自动配置的、开箱即用的 RabbitMQ 集成方案。

## 核心功能

1.  **自动配置**: 任何微服务只需在 `pom.xml` 中添加此 starter 作为依赖，即可自动获得与 RabbitMQ 的连接能力，并应用一套优良的默认配置。

2.  **JSON序列化**: 本 starter 的核心价值在于，它会自动配置一个 `Jackson2JsonMessageConverter` 作为全局的消息转换器。这解决了 Spring AMQP 默认使用 Java 序列化的两大痛点：
    -   **可读性**: 存入队列中的消息将是易于阅读和调试的 JSON 字符串，而不是二进制乱码。
    -   **互操作性**: JSON 是一种通用数据格式，便于未来可能出现的、由其他语言（如 Python, Node.js）编写的服务来消费队列中的消息。

3.  **通用组件声明 (可选)**: 本 starter 可以在内部预先声明一些在整个系统中通用的交换机（Exchange）、队列（Queue）和绑定（Binding），以进一步减少各个业务模块的重复配置代码。 (此功能已在代码中注释，可按需启用)。

## 使用方法

在任何需要使用 RabbitMQ 的微服务中（例如 `cloud-web` 或 `cloud-auth`），请执行以下步骤：

### 1. 添加 Maven 依赖

在您的微服务的 `pom.xml` 文件中，添加对此 starter 的依赖。（请确保已移除旧的 `spring-boot-starter-amqp` 依赖，以避免冲突）。

```xml
<dependency>
    <groupId>com.overthinker.cloud</groupId>
    <artifactId>system-rabbitmq-starter</artifactId>
    <version>${project.version}</version>
</dependency>
```

### 2. 直接注入并使用

完成依赖添加后，您无需任何额外配置。可以直接在您的代码中注入并使用 `RabbitTemplate` 或通过 `@RabbitListener` 注解来创建消费者。

**生产者示例：**

```java
@Service
@RequiredArgsConstructor
public class MyProducerService {

    private final RabbitTemplate rabbitTemplate;

    public void sendSomeMessage(AuditLogDTO logDto) {
        // 由于自动配置了JSON转换器，您可以直接发送一个POJO对象
        rabbitTemplate.convertAndSend("audit.log.exchange", "audit.log.routingKey", logDto);
    }
}
```

**消费者示例：**

```java
@Component
public class MyConsumerService {

    @RabbitListener(queues = "audit.log.queue")
    public void receiveMessage(AuditLogDTO logDto) {
        // Spring会自动将JSON消息反序列化为POJO对象
        System.out.println("Received log: " + logDto.getOperation());
    }
}
```

通过使用此 starter，您可以极大地简化 RabbitMQ 在各个微服务中的集成工作，并确保整个项目使用一套统一、高效、可读的消息传输标准。

---
*本文档最后更新于 2025-07-25。*
