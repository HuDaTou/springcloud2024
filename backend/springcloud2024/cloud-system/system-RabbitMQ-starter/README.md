# System RabbitMQ 启动器

## 1. 模块概述

`system-rabbitmq-starter` 是一个 Spring Boot 启动器，旨在简化 `springcloud2024` 项目中 [RabbitMQ](https://www.rabbitmq.com/) 的集成。它为常见的 RabbitMQ 使用场景提供自动配置，确保所有微服务之间消息传递的一致性和可靠性。

该启动器使用 JSON 消息转换器预配置了 `RabbitTemplate`，从而简化了将 Java 对象作为消息发送和接收的过程，无需手动序列化。

## 2. 核心功能

- **简化配置：** 为 RabbitMQ 连接提供合理的默认值。
- **JSON 消息转换：** 自动配置 `Jackson2JsonMessageConverter`，允许将 Java 对象无缝序列化为 JSON 或从 JSON 反序列化。
- **自动配置的 RabbitTemplate：** 提供一个预配置的 `RabbitTemplate` bean，可立即在生产者服务中使用。
- **监听器支持：** 完全兼容 Spring AMQP 的 `@RabbitListener` 注解，用于创建消息消费者。

## 3. 关键依赖

- **Spring AMQP Starter:** `org.springframework.boot:spring-boot-starter-amqp`

## 4. 如何使用

要在微服务中启用 RabbitMQ，请按照以下步骤操作：

### 第 1 步：添加依赖

将此启动器添加到目标微服务的 `pom.xml` 中：

```xml
<dependency>
    <groupId>com.overthinker.cloud</groupId>
    <artifactId>system-rabbitmq-starter</artifactId>
    <version>${project.version}</version>
</dependency>
```

### 第 2 步：配置连接

在服务的 `application.yaml` 文件中，提供 RabbitMQ 代理的连接详细信息：

```yaml
spring:
  rabbitmq:
    host: localhost
    port: 5672
    username: guest
    password: guest
    virtual-host: /
```

### 第 3 步：发送和接收消息

**发送消息：**

将 `RabbitTemplate` 注入到您的服务中，并使用它来发送消息。对象将自动转换为 JSON。

```java
@Autowired
private RabbitTemplate rabbitTemplate;

public void sendMessage(MyObject myObject) {
    rabbitTemplate.convertAndSend("myExchange", "myRoutingKey", myObject);
}
```

**接收消息：**

创建一个监听器组件并使用 `@RabbitListener` 注解。JSON 消息将自动转换回 Java 对象。

```java
@Component
public class MyMessageListener {

    @RabbitListener(queues = "myQueue")
    public void handleMessage(MyObject myObject) {
        // 处理消息
    }
}
```