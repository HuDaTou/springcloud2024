package com.overthinker.cloud.system.auth.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.overthinker.cloud.system.auth.service.PermissionScanner;
import com.overthinker.cloud.system.auth.service.PermissionSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;

import java.util.Objects;

/**
 * 权限自动配置类
 * 负责自动装配权限扫描器和发送器。
 * 当属性 permission.scanner.enabled 为 true 时（或缺省时）启用。
 */
@Slf4j
@AutoConfiguration
@ConditionalOnProperty(name = "permission.scanner.enabled", havingValue = "true", matchIfMissing = true)
@ConditionalOnClass({RabbitTemplate.class, ConnectionFactory.class})
public class PermissionAutoConfiguration {

    public PermissionAutoConfiguration() {
        log.info("========== PermissionAutoConfiguration 已加载 ==========");
    }

    @Bean
    @Primary
    @ConditionalOnMissingBean
    public RabbitTemplate permissionRabbitTemplate(ConnectionFactory connectionFactory) {
        log.info("========== 正在创建 RabbitTemplate Bean ==========");
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        log.info("========== RabbitTemplate Bean 创建完成 ==========");
        return rabbitTemplate;
    }

    @Bean
    public PermissionScanner permissionScanner(org.springframework.context.ApplicationContext applicationContext) {
        log.info("========== 正在创建 PermissionScanner Bean ==========");
        return new PermissionScanner(applicationContext);
    }

    @Bean
    public PermissionSender permissionSender(PermissionScanner permissionScanner,
                                             RabbitTemplate rabbitTemplate,
                                             ObjectMapper objectMapper,
                                             Environment environment) {
        Objects.requireNonNull(rabbitTemplate, "RabbitTemplate 不能为空");
        log.info("========== 正在创建 PermissionSender Bean ==========");
        return new PermissionSender(permissionScanner, rabbitTemplate, objectMapper, environment);
    }
}