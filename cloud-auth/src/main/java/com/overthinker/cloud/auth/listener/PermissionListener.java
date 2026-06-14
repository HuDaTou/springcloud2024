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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
     * 先根据服务名称删除该服务下的所有权限，再批量新增新的权限。
     * 适用于服务启动时全量上报权限的场景。
     * </p>
     *
     * @param serviceName 服务名称
     * @param permissions 权限列表
     */
    private void replaceServicePermissions(String serviceName, List<PermissionDTO> permissions) {
        // 1. 删除该服务下的所有权限
        int deleteCount = permissionService.getBaseMapper().delete(
                new LambdaQueryWrapper<SysPermission>()
                        .eq(SysPermission::getServiceName, serviceName)
        );
        log.info("已删除服务 [{}] 的 {} 条旧权限记录", serviceName, deleteCount);

        // 2. 批量新增权限
        if (!permissions.isEmpty()) {
            List<SysPermission> permissionList = new ArrayList<>();
            for (PermissionDTO dto : permissions) {
                SysPermission permission = new SysPermission()
                        .setPermissonCode(dto.getPermissonCode())
                        .setName(dto.getName())
                        .setPath(dto.getPath())
                        .setHttpMethod(dto.getHttpMethod())
                        .setCategory(dto.getCategory())
                        .setServiceName(dto.getServiceName());
                permissionList.add(permission);
            }
            permissionService.saveBatch(permissionList);
            log.info("已新增服务 [{}] 的 {} 条权限记录", serviceName, permissionList.size());
        }
    }

    /**
     * 保存或更新权限信息（保留，暂不使用）
     * <p>
     * 根据权限标识（permissionCode）判断是新增还是更新。
     * 实现幂等性，同一权限标识多次上报不会创建重复记录。
     * </p>
     *
     * @param dto 权限 DTO 对象
     */
    private void saveOrUpdatePermission(PermissionDTO dto) {
        String code = dto.getPermissonCode();

        // 1. 根据权限标识查询是否已存在
        SysPermission existing = permissionService.getOne(new LambdaQueryWrapper<SysPermission>()
                .eq(SysPermission::getPermissonCode, code));

        if (existing != null) {
            // 2. 已存在，检查字段是否需要更新
            boolean needUpdate = false;

            if (!Objects.equals(dto.getName(), existing.getName())) {
                existing.setName(dto.getName());
                needUpdate = true;
            }
            if (!Objects.equals(dto.getPath(), existing.getPath())) {
                existing.setPath(dto.getPath());
                needUpdate = true;
            }
            if (!Objects.equals(dto.getHttpMethod(), existing.getHttpMethod())) {
                existing.setHttpMethod(dto.getHttpMethod());
                needUpdate = true;
            }
            if (dto.getCategory() != null && !dto.getCategory().equals(existing.getCategory())) {
                existing.setCategory(dto.getCategory());
                needUpdate = true;
            }
            if (dto.getServiceName() != null && !dto.getServiceName().equals(existing.getServiceName())) {
                existing.setServiceName(dto.getServiceName());
                needUpdate = true;
            }

            // 3. 有变化则更新
            if (needUpdate) {
                permissionService.updateById(existing);
                log.debug("更新权限: {} - {}", code, dto.getName());
            }
        } else {
            // 4. 不存在，新增权限
            SysPermission newPermission = new SysPermission()
                    .setPermissonCode(code)
                    .setName(dto.getName())
                    .setPath(dto.getPath())
                    .setHttpMethod(dto.getHttpMethod())
                    .setCategory(dto.getCategory())
                    .setServiceName(dto.getServiceName());

            permissionService.save(newPermission);
            log.debug("新增权限: {} - {}", code, dto.getName());
        }
    }
}