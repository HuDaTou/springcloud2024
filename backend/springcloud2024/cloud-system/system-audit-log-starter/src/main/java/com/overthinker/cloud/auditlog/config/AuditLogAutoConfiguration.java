package com.overthinker.cloud.auditlog.config;

import com.overthinker.cloud.auditlog.aop.AuditLogAspect;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(RabbitTemplate.class)
public class AuditLogAutoConfiguration {

    @Bean
    public AuditLogAspect auditLogAspect(RabbitTemplate rabbitTemplate) {
        return new AuditLogAspect(rabbitTemplate);
    }
}
