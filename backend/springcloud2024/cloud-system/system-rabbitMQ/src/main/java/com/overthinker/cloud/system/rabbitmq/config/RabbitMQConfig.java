package com.overthinker.cloud.system.rabbitmq.config;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class RabbitMQConfig {
    // 可在此配置队列、交换机、绑定关系等
    // 示例：
    // @Bean
    // public Queue exampleQueue() {
    //     return new Queue("example.queue", true);
    // }
}
