package com.overthinker.cloud.rabbitmq.config;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQAutoConfiguration {

    /**
     * Configures a message converter to use JSON for serializing and deserializing messages.
     * This allows sending and receiving POJOs as JSON strings, which is great for interoperability and debugging.
     * @return A Jackson2JsonMessageConverter bean.
     */
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    // You can add more common beans here, such as Exchange, Queue, and Binding declarations.
    // For example, to declare the audit log exchange and queue globally:
    /*
    @Bean
    public TopicExchange auditLogExchange(@Value("${spring.rabbitmq.exchange.audit-log:audit.log.exchange}") String exchangeName) {
        return new TopicExchange(exchangeName, true, false);
    }

    @Bean
    public Queue auditLogQueue(@Value("${spring.rabbitmq.queue.audit-log:audit.log.queue}") String queueName) {
        return new Queue(queueName, true);
    }

    @Bean
    public Binding auditLogBinding(Queue auditLogQueue, TopicExchange auditLogExchange, @Value("${spring.rabbitmq.routingKey.audit-log:audit.log.routingKey}") String routingKey) {
        return BindingBuilder.bind(auditLogQueue).to(auditLogExchange).with(routingKey);
    }
    */
}
