package com.overthinker.cloud.auth.listener;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.overthinker.cloud.api.apis.auth.mq.PermissionDTO;
import com.overthinker.cloud.auth.entity.PO.SysPermission;
import com.overthinker.cloud.auth.service.PermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 权限注册监听器
 * <p>
 * 监听 RabbitMQ 队列，接收各微服务上报的接口权限信息，并持久化到数据库。
 * 用于实现接口权限的自动注册功能。
 * </p>
 *
 * @author overthinker
 * @since 2024-06-13
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PermissionListener {

    private final PermissionService permissionService;

    private final ObjectMapper objectMapper;

    /**
     * 权限注册队列名称
     */
    private static final String PERMISSION_REGISTER_QUEUE = "auth.permission.register.queue";

    /**
     * 权限交换机名称
     */
    private static final String PERMISSION_EXCHANGE = "auth.permission.exchange";

    /**
     * 权限注册路由键
     */
    private static final String PERMISSION_REGISTER_ROUTING_KEY = "auth.permission.register";

    /**
     * 接收权限注册消息
     * <p>
     * 从 RabbitMQ 队列接收权限注册消息，解析后批量处理权限数据。
     * 每条消息包含一个微服务的所有接口权限信息。
     * </p>
     *
     * @param message 消息体（JSON 字符串，格式为 List<PermissionDTO>）
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = PERMISSION_REGISTER_QUEUE, durable = "true"),
            exchange = @Exchange(value = PERMISSION_EXCHANGE, type = "topic"),
            key = PERMISSION_REGISTER_ROUTING_KEY
    ))
    @Transactional(rollbackFor = Exception.class)
    public void handlePermissionRegistration(String message) {
        log.info("收到权限注册消息，开始处理...");
        try {
            // 1. 解析 JSON 消息
            List<PermissionDTO> permissions = objectMapper.readValue(message, new TypeReference<List<PermissionDTO>>() {});
            if (permissions == null || permissions.isEmpty()) {
                log.warn("权限列表为空，忽略。");
                return;
            }

            // 2. 获取服务名称
            String serviceName = permissions.get(0).getServiceName();
            log.info("正在处理服务 [{}] 的 {} 条权限记录", serviceName, permissions.size());

            // 3. 先删除该服务下的所有权限，再批量新增
            replaceServicePermissions(serviceName, permissions);

            log.info("服务 [{}] 权限注册完成。", serviceName);

        } catch (Exception e) {
            log.error("处理权限注册消息失败！", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 替换服务权限
     * <p>
     * 根据 permissionCode 判断是更新还是新增，保留原有ID。
     * 删除服务上报中不再存在的权限。
     * 适用于服务启动时全量上报权限的场景。
     * </p>
     *
     * @param serviceName 服务名称
     * @param permissions 权限列表
     */
    private void replaceServicePermissions(String serviceName, List<PermissionDTO> permissions) {
        // 1. 获取该服务下已有的权限
        List<SysPermission> existingPermissions = permissionService.list(
                new LambdaQueryWrapper<SysPermission>().eq(SysPermission::getServiceName, serviceName)
        );

        // 2. 按 serviceName + permissionCode + path 构建唯一标识Map（应对同一permissionCode不同路径的情况）
        Map<String, SysPermission> existingMap = existingPermissions.stream()
                .collect(Collectors.toMap(
                        p -> buildUniqueKey(p.getServiceName(), p.getPermissonCode(), p.getPath()),
                        p -> p,
                        (existing, replacement) -> existing  // 冲突时保留旧的
                ));

        int updateCount = 0;
        int insertCount = 0;

        // 3. 遍历新权限，更新或新增
        for (PermissionDTO dto : permissions) {
            String uniqueKey = buildUniqueKey(dto.getServiceName(), dto.getPermissonCode(), dto.getPath());
            SysPermission existing = existingMap.get(uniqueKey);
            if (existing != null) {
                // 更新（保留原ID）
                boolean needUpdate = false;

                if (!Objects.equals(dto.getName(), existing.getName())) {
                    existing.setName(dto.getName());
                    needUpdate = true;
                }
                if (!Objects.equals(dto.getHttpMethod(), existing.getHttpMethod())) {
                    existing.setHttpMethod(dto.getHttpMethod());
                    needUpdate = true;
                }
                if (!Objects.equals(dto.getCategory(), existing.getCategory())) {
                    existing.setCategory(dto.getCategory());
                    needUpdate = true;
                }

                if (needUpdate) {
                    permissionService.updateById(existing);
                    updateCount++;
                }
                // 从Map中移除，剩下的就是需要删除的
                existingMap.remove(uniqueKey);
            } else {
                // 新增
                SysPermission newPermission = new SysPermission()
                        .setPermissonCode(dto.getPermissonCode())
                        .setName(dto.getName())
                        .setPath(dto.getPath())
                        .setHttpMethod(dto.getHttpMethod())
                        .setCategory(dto.getCategory())
                        .setServiceName(dto.getServiceName());
                permissionService.save(newPermission);
                insertCount++;
            }
        }

        // 4. 删除不再存在的权限（已移除的服务接口）
        int deleteCount = 0;
        if (!existingMap.isEmpty()) {
            List<Long> deleteIds = existingMap.values().stream()
                    .map(SysPermission::getId)
                    .collect(Collectors.toList());
            permissionService.removeByIds(deleteIds);
            deleteCount = deleteIds.size();
        }

        log.info("服务 [{}] 权限处理完成：更新 {} 条，新增 {} 条，删除 {} 条",
                serviceName, updateCount, insertCount, deleteCount);
    }

    /**
     * 构建权限唯一标识
     * <p>
     * 使用 serviceName + permissionCode + path 组合确保唯一性，
     * 因为同一 permissionCode 可能对应多个不同的接口路径。
     * </p>
     *
     * @param serviceName    服务名称
     * @param permissionCode 权限标识
     * @param path           接口路径
     * @return 唯一标识字符串
     */
    private String buildUniqueKey(String serviceName, String permissionCode, String path) {
        return serviceName + ":" + permissionCode + ":" + path;
    }
}