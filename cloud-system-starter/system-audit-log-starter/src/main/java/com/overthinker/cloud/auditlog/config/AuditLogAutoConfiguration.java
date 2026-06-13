package com.overthinker.cloud.auditlog.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.overthinker.cloud.auditlog.aop.AuditLogAspect;
import com.overthinker.cloud.auditlog.config.properties.RabbitMqAuditLogProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass({RabbitTemplate.class, ObjectMapper.class})
@EnableConfigurationProperties(RabbitMqAuditLogProperties.class)
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "audit.log.rabbitmq", name = "enable", havingValue = "true", matchIfMissing = true)
public class AuditLogAutoConfiguration {

    private final RabbitMqAuditLogProperties properties;


    @Bean
    public TopicExchange auditLogTopicExchange() {
        return new TopicExchange(properties.getExchangeName());
    }

    @Bean
    public Queue auditLogQueue() {
        return new Queue(properties.getQueueName());
    }

    @Bean
    public Binding auditLogBinding(Queue auditLogQueue, TopicExchange auditLogTopicExchange) {
        return BindingBuilder.bind(auditLogQueue).to(auditLogTopicExchange).with(properties.getRoutingKey());
    }

    @Bean
    public AuditLogAspect auditLogAspect(RabbitTemplate rabbitTemplate, ObjectMapper objectMapper) {
        return new AuditLogAspect(rabbitTemplate, objectMapper, properties.getExchangeName(), properties.getRoutingKey());
    }
}
