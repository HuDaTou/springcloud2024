//package com.overthinker.cloud.auth.listener;
//
//import com.fasterxml.jackson.core.type.TypeReference;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
//
//import com.overthinker.cloud.api.auth.dto.PermissionDTO;
//import com.overthinker.cloud.auth.entity.PO.SysPermission;
//import com.overthinker.cloud.auth.entity.PO.SysRolePermission;
//import com.overthinker.cloud.auth.service.PermissionService;
//import lombok.extern.slf4j.Slf4j;
//
//import org.springframework.amqp.rabbit.annotation.Exchange;
//import org.springframework.amqp.rabbit.annotation.Queue;
//import org.springframework.amqp.rabbit.annotation.QueueBinding;
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
///**
// * 权限注册监听器
// * 监听RabbitMQ队列，接收各微服务上报的接口权限信息，并持久化到数据库。
// */
//@Slf4j
//@Component
//public class PermissionListener {
//
//    @Autowired
//    private PermissionService permissionService;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    // 监听的队列名称
//    private static final String PERMISSION_REGISTER_QUEUE = "auth.permission.register.queue";
//    // 交换机名称
//    private static final String PERMISSION_EXCHANGE = "auth.permission.exchange";
//    // 路由键
//    private static final String PERMISSION_REGISTER_ROUTING_KEY = "auth.permission.register";
//
//    /**
//     * 接收权限注册消息
//     * @param message 消息体（JSON字符串）
//     */
//    @RabbitListener(bindings = @QueueBinding(
//            value = @Queue(value = PERMISSION_REGISTER_QUEUE, durable = "true"),
//            exchange = @Exchange(value = PERMISSION_EXCHANGE, type = "topic"),
//            key = PERMISSION_REGISTER_ROUTING_KEY
//    ))
//    @Transactional(rollbackFor = Exception.class)
//    public void handlePermissionRegistration(String message) {
//        log.info("收到权限注册消息，开始处理...");
//        try {
//            List<PermissionDTO> permissions = objectMapper.readValue(message, new TypeReference<List<PermissionDTO>>() {});
//            if (permissions == null || permissions.isEmpty()) {
//                log.warn("权限列表为空，忽略。");
//                return;
//            }
//
//            String serviceName = permissions.get(0).getServiceName();
//            log.info("正在处理服务 [{}] 的 {} 条权限记录", serviceName, permissions.size());
//
//            // 简单的处理逻辑：遍历入库
//            // 实际生产环境可能需要更复杂的逻辑：比如先删除该服务旧的所有权限，或者增量更新
//            // 这里采用：根据 permissionKey (code) 判断，存在则更新，不存在则插入
//            for (PermissionDTO dto : permissions) {
//                saveOrUpdatePermission(dto);
//            }
//
//            log.info("服务 [{}] 权限注册完成。", serviceName);
//
//        } catch (Exception e) {
//            log.error("处理权限注册消息失败！", e);
//            // 抛出异常，让MQ重试（根据配置）或进入死信队列
//            throw new RuntimeException(e);
//        }
//    }
//
//    private void saveOrUpdatePermission(PermissionDTO dto) {
//        String code = dto.getCode();
//        // 1. 查询是否存在
//        SysPermission existing = permissionService.getOne(new LambdaQueryWrapper<SysPermission>()
//                .eq(SysPermission::getPermissionKey, code));
//
//        if (existing != null) {
//            // 2. 更新 (只更新描述等非关键字段，以免覆盖管理员的手动配置)
//            // 如果需要更新菜单ID等，需要更复杂的逻辑。这里主要确保code和desc是最新的。
//            boolean needUpdate = false;
//            if (!dto.getName().equals(existing.getPermissionDesc())) {
//                existing.setPermissionDesc(dto.getName());
//                needUpdate = true;
//            }
//            // 可以在这里更新其他字段
//
//            if (needUpdate) {
//                existing.setUpdateTime(LocalDateTime.now());
//                permissionService.updateById(existing);
//                log.debug("更新权限: {} - {}", code, dto.getName());
//            }
//        } else {
//            // 3. 插入
//            SysPermission newPermission = new SysPermission();
//            newPermission.setPermissionKey(code);
//            newPermission.setPermissionDesc(dto.getName());
//            newPermission.setMenuId(0L); // 默认为0，表示未分配菜单，需管理员后续在后台关联菜单
//
//            permissionService.save(newPermission);
//            log.debug("新增权限: {} - {}", code, dto.getName());
//        }
//    }
//}
