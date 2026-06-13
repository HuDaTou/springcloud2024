package com.overthinker.cloud.system.starter.auth.service;

import com.overthinker.cloud.api.auth.api.AuthClient;
import com.overthinker.cloud.api.auth.mq.PermissionDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

/**
 * Feign权限注册客户端
 * <p>
 * 通过Feign客户端调用认证服务的API，将扫描到的权限信息注册到权限管理服务。
 * 这是RabbitMQ方案的备用方案，当不使用消息队列时可使用此客户端。
 * </p>
 *
 * @author overthinker
 * @since 2024-06-13
 */
@Slf4j
public class PermissionRegistryClient {

    private final AuthClient authClient;

    /**
     * 构造函数
     *
     * @param authClient Feign客户端实例，用于调用认证服务API
     */
    public PermissionRegistryClient(AuthClient authClient) {
        Objects.requireNonNull(authClient, "AuthClient cannot be null");
        this.authClient = authClient;
        log.info("========== PermissionRegistryClient 已实例化 ==========");
    }

    /**
     * 注册权限信息
     * <p>
     * 调用认证服务的权限注册接口，将权限信息批量注册到权限管理服务。
     * </p>
     *
     * @param permissions 权限信息列表
     */
    public void registerPermissions(List<PermissionDTO> permissions) {
        // 1. 空值检查
        if (CollectionUtils.isEmpty(permissions)) {
            log.info("未找到需要注册的权限信息");
            return;
        }

        // 2. 调用Feign客户端注册权限
        try {
            log.info("开始通过Feign客户端注册 {} 个权限信息", permissions.size());
            authClient.registerPermissions(permissions);
            log.info("权限信息注册成功，共注册 {} 个权限点", permissions.size());
        } catch (Exception e) {
            log.error("通过Feign客户端注册权限信息失败", e);
        }
    }
}
