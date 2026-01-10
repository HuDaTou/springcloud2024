package com.overthinker.cloud.auth.config.mq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitEmailConfig {

    // 交换机名称
    public static final String MAIL_EXCHANGE = "mail.exchange";
    // 队列名称
    public static final String MAIL_QUEUE = "mail.queue";

    public static final String MAIL_ROUTING_KEY = "mail.routing.key";

    /**
     * 声明交换机
     * 如果 RabbitMQ 中没有，Spring 会自动创建
     */
    @Bean
    public DirectExchange mailExchange() {
        // 参数：name, durable(持久化), autoDelete(自动删除)
        return new DirectExchange(MAIL_EXCHANGE, true, false);
    }

    /**
     * 声明队列
     */
    @Bean
    public Queue mailQueue() {
        // 参数：name, durable(持久化)
        // 你之前问过有序性，如果想开启单活消费者，可以在这里加参数
        Map<String, Object> args = new HashMap<>();
        // args.put("x-single-active-consumer", true); // 可选：保证单消费者顺序性

        return new Queue(MAIL_QUEUE, true, false, false, args);
    }

    /**
     * 【必须新增】声明绑定关系
     * 作用：把 mailQueue 绑定到 mailExchange 上，并指定暗号为 MAIL_ROUTING_KEY
     */
    @Bean
    public Binding mailBinding(Queue mailQueue, DirectExchange mailExchange) {
        return BindingBuilder
                .bind(mailQueue)           // 要绑定的队列
                .to(mailExchange)          // 绑定到哪个交换机
                .with(MAIL_ROUTING_KEY);   // 指定暗号（Binding Key）
    }
}