package com.overthinker.cloud.system.auth.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.overthinker.cloud.system.auth.service.PermissionScanner;
import com.overthinker.cloud.system.auth.service.PermissionSender;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * 权限自动配置类
 * 负责自动装配权限扫描器和发送器。
 * 当属性 permission.scanner.enabled 为 true 时（或缺省时）启用。
 */
@Configuration
@ConditionalOnProperty(name = "permission.scanner.enabled", havingValue = "true", matchIfMissing = true)
public class PermissionAutoConfiguration {

    @Bean
    public PermissionScanner permissionScanner(ApplicationContext applicationContext) {
        return new PermissionScanner(applicationContext);
    }

    @Bean
    @ConditionalOnBean(RabbitTemplate.class) // 仅当RabbitTemplate存在时才创建发送器
    public PermissionSender permissionSender(PermissionScanner permissionScanner,
                                             RabbitTemplate rabbitTemplate,
                                             ObjectMapper objectMapper,
                                             Environment environment) {
        return new PermissionSender(permissionScanner, rabbitTemplate, objectMapper, environment);
    }
}