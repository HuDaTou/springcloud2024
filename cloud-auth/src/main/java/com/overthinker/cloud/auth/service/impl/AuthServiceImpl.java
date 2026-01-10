package com.overthinker.cloud.auth.service.impl;

import com.overthinker.cloud.auth.entity.DTO.UserRegisterDTO;
import com.overthinker.cloud.auth.entity.DTO.UserResetConfirmDTO;
import com.overthinker.cloud.auth.entity.DTO.UserResetPasswordDTO;
import com.overthinker.cloud.auth.entity.PO.SysRole;
import com.overthinker.cloud.auth.entity.PO.User;
import com.overthinker.cloud.auth.entity.PO.UserRole;
import com.overthinker.cloud.auth.entity.VO.UserRegisterBuildVO;
import com.overthinker.cloud.auth.mapper.UserRoleMapper;
import com.overthinker.cloud.auth.service.AuthService;
import com.overthinker.cloud.auth.service.RoleService;
import com.overthinker.cloud.common.core.resp.ResultData;
import com.overthinker.cloud.system.redis.constant.RedisConstants;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {




    /**
     * 密码加密工具
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * Redis 操作模板（用于验证码存储、Token管理等）
     */
    private final StringRedisTemplate redisTemplate;

    /**
     * 用户基本信息服务
     */
    private final UserServiceImpl userService;

    /**
     * 角色/权限服务
     */
    private final RoleService roleService;

    /**
     * 用户与角色关联表的持久层
     */
    private final UserRoleMapper userRoleMapper;

    /**
     * 消息队列模板（用于异步发送邮件/短信）
     */
    private final RabbitTemplate rabbitTemplate;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultData<Void> register(UserRegisterDTO dto) {
        // 1. 校验验证码
        String codeKey = RedisConstants.USER_CODE_KEY_PREFIX + dto.getEmail();
        String cachedCode = redisTemplate.opsForValue().get(codeKey);
        if (cachedCode == null || !cachedCode.equals(dto.getCode())) {
            return ResultData.failure("验证码错误或已过期");
        }

        // 2. 检查用户名是否存在
        User accountByNameOrEmail = userService.findAccountByNameOrEmail(dto.getUsername(), dto.getEmail());
        if (Objects.isNull(accountByNameOrEmail)) return ResultData.failure("用户名已存在");


        // 4. 创建用户
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setNickname(dto.getUsername()); // 默认昵称同用户名
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        // TODO 这里需要从minio中拿取
        user.setAvatar("""
                https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png""");

        userService.save(user);

        // 5. 分配默认角色 (如 USER)
        SysRole dfalultRole = roleService.getDfalultRole();
        UserRole userRole = new UserRole().setUserId(user.getId())
                .setRoleId(dfalultRole.getId());
        userRoleMapper.insert(userRole);


        // 6. 删除验证码
        redisTemplate.delete(codeKey);

        return ResultData.success();
    }

    @Override
    public ResultData<Void> resetConfirm(UserResetConfirmDTO userResetDTO) {
        // TODO: 实现重置密码验证逻辑
        return ResultData.success();
    }

    @Override
    public ResultData<Void> resetPassword(UserResetPasswordDTO userResetDTO) {
        // TODO: 实现重置密码逻辑
        return ResultData.success();
    }

    @Override
    public UserDetails loginOrRegisterThirdParty(String type, String accessToken) throws Exception {
        // 暂不实现，使用Spring Security OAuth2 Login
        return null;
    }
}
