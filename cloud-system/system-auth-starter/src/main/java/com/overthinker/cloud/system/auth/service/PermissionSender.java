package com.overthinker.cloud.system.auth.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.overthinker.cloud.api.auth.dto.PermissionDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 权限发送器
 * 负责在应用启动后扫描权限，并通过RabbitMQ将权限信息异步发送给权限管理服务。
 */
@Slf4j
@Component
public class PermissionSender implements ApplicationRunner {

    private final PermissionScanner permissionScanner;
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;
    private final Environment environment;

    // TODO: 将这些配置项放入配置文件中
    private static final String PERMISSION_EXCHANGE = "auth.permission.exchange";
    private static final String PERMISSION_REGISTER_ROUTING_KEY = "auth.permission.register";

    public PermissionSender(PermissionScanner permissionScanner,
                            RabbitTemplate rabbitTemplate,
                            ObjectMapper objectMapper,
                            Environment environment) {
        this.permissionScanner = permissionScanner;
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
        this.environment = environment;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 确保只在非测试环境下执行权限扫描和发送
        String[] activeProfiles = environment.getActiveProfiles();
        boolean isTestProfile = false;
        for (String profile : activeProfiles) {
            if ("test".equals(profile)) {
                isTestProfile = true;
                break;
            }
        }

        if (isTestProfile) {
            log.info("当前为测试环境，跳过权限扫描与注册。");
            return;
        }

        log.info("应用启动完成，开始扫描并注册权限信息...");
        List<PermissionDTO> permissions = permissionScanner.scanPermissions();

        if (!permissions.isEmpty()) {
            try {
                String permissionsJson = objectMapper.writeValueAsString(permissions);
                rabbitTemplate.convertAndSend(PERMISSION_EXCHANGE, PERMISSION_REGISTER_ROUTING_KEY, permissionsJson);
                log.info("已成功发送 {} 个权限点到 RabbitMQ，Exchange: {}, RoutingKey: {}", permissions.size(), PERMISSION_EXCHANGE, PERMISSION_REGISTER_ROUTING_KEY);
            } catch (JsonProcessingException e) {
                log.error("权限信息序列化失败，无法发送到 RabbitMQ。", e);
            } catch (Exception e) {
                log.error("发送权限信息到 RabbitMQ 失败。", e);
            }
        } else {
            log.info("未扫描到需要注册的权限点。");
        }
    }
}
