package com.overthinker.cloud.auth.listener;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.overthinker.cloud.api.auth.dto.PermissionDTO;
import com.overthinker.cloud.auth.entity.PO.SysPermission;
import com.overthinker.cloud.auth.service.PermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 权限注册监听器
 * 监听RabbitMQ队列，接收各微服务上报的接口权限信息，并持久化到数据库。
 */
@Slf4j
@Component
public class PermissionListener {

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String PERMISSION_REGISTER_QUEUE = "auth.permission.register.queue";
    private static final String PERMISSION_EXCHANGE = "auth.permission.exchange";
    private static final String PERMISSION_REGISTER_ROUTING_KEY = "auth.permission.register";

    /**
     * 接收权限注册消息
     * @param message 消息体（JSON字符串）
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
            List<PermissionDTO> permissions = objectMapper.readValue(message, new TypeReference<List<PermissionDTO>>() {});
            if (permissions == null || permissions.isEmpty()) {
                log.warn("权限列表为空，忽略。");
                return;
            }

            String serviceName = permissions.get(0).getServiceName();
            log.info("正在处理服务 [{}] 的 {} 条权限记录", serviceName, permissions.size());

            for (PermissionDTO dto : permissions) {
                saveOrUpdatePermission(dto);
            }

            log.info("服务 [{}] 权限注册完成。", serviceName);

        } catch (Exception e) {
            log.error("处理权限注册消息失败！", e);
            throw new RuntimeException(e);
        }
    }

    private void saveOrUpdatePermission(PermissionDTO dto) {
        String code = dto.getCode();
        SysPermission existing = permissionService.getOne(new LambdaQueryWrapper<SysPermission>()
                .eq(SysPermission::getPermissonCode, code));

        if (existing != null) {
            boolean needUpdate = false;
            if (!dto.getName().equals(existing.getName())) {
                existing.setName(dto.getName());
                needUpdate = true;
            }
            if (!dto.getPath().equals(existing.getPath())) {
                existing.setPath(dto.getPath());
                needUpdate = true;
            }
            if (!dto.getHttpMethod().equals(existing.getHttpMethod())) {
                existing.setHttpMethod(dto.getHttpMethod());
                needUpdate = true;
            }
            if (dto.getCategory() != null && !dto.getCategory().equals(existing.getCategory())) {
                existing.setCategory(dto.getCategory());
                needUpdate = true;
            }

            if (needUpdate) {
                existing.setUpdateTime(LocalDateTime.now());
                permissionService.updateById(existing);
                log.debug("更新权限: {} - {}", code, dto.getName());
            }
        } else {
            SysPermission newPermission = new SysPermission();
            newPermission.setPermissonCode(code);
            newPermission.setName(dto.getName());
            newPermission.setPath(dto.getPath());
            newPermission.setHttpMethod(dto.getHttpMethod());
            newPermission.setCategory(dto.getCategory());
            newPermission.setCreateTime(LocalDateTime.now());
            newPermission.setUpdateTime(LocalDateTime.now());

            permissionService.save(newPermission);
            log.debug("新增权限: {} - {}", code, dto.getName());
        }
    }
}