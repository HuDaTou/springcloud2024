package com.overthinker.cloud.auth.service.impl;

import com.overthinker.cloud.auth.entity.DTO.UserRegisterDTO;
import com.overthinker.cloud.auth.entity.DTO.UserResetConfirmDTO;
import com.overthinker.cloud.auth.entity.DTO.UserResetPasswordDTO;
import com.overthinker.cloud.auth.entity.PO.SysRole;
import com.overthinker.cloud.auth.entity.PO.User;
import com.overthinker.cloud.auth.entity.PO.UserRole;
import com.overthinker.cloud.auth.mapper.UserRoleMapper;
import com.overthinker.cloud.auth.service.AuthService;
import com.overthinker.cloud.auth.service.RoleService;
import com.overthinker.cloud.common.core.resp.ResultData;
import com.overthinker.cloud.common.core.utils.MyStringUtils;
import com.overthinker.cloud.system.starter.redis.constants.RedisConstants;
import com.overthinker.cloud.system.starter.redis.utils.MyRedisCache;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
     * Redis 操作工具（用于验证码存储、Token管理等）
     */
    private final MyRedisCache myRedisCache;

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


    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultData<Void> register(UserRegisterDTO dto) {
        // 1. 校验验证码
        String codeKey = MyStringUtils.buildRedisKey(RedisConstants.REGISTER_CODE_KEY_PREFIX, dto.getEmail());
        String cachedCode = myRedisCache.getCacheObject(codeKey);
        String code = dto.getCode();
        if (MyStringUtils.isBlank(code)) {
            return ResultData.failure("验证码不能为空");
        }
        if (MyStringUtils.isEmpty(cachedCode)) {
            return ResultData.failure("验证码不存在");
        }
        if (!code.equals(cachedCode)) {
            return ResultData.failure("验证码错误或已过期");
        }


        // 2. 检查用户名或邮箱是否已存在
        User accountByNameOrEmail = userService.findAccountByNameOrEmail(dto.getUsername(), dto.getEmail());
        if (Objects.nonNull(accountByNameOrEmail)) return ResultData.failure("用户名或邮箱已存在");


        // 4. 创建用户
        User user = new User()
                .setUsername(dto.getUsername())
                .setNickname(dto.getUsername())
                .setEmail(dto.getEmail())
                .setPassword(passwordEncoder.encode(dto.getPassword()))
                .setAvatar("""
                        https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png""")
                .setRegisterType(0)
                .setRegisterIp("127.0.0.1")
                .setRegisterAddress("未知")
                .setIsDisable(false)
                .setLoginTime(LocalDateTime.now());

        userService.save(user);

        // 5. 分配默认角色 (如 USER)
        SysRole dfalultRole = roleService.getDfalultRole();
        UserRole userRole = new UserRole().setUserId(user.getId())
                .setRoleId(dfalultRole.getId());
        userRoleMapper.insert(userRole);


        // 6. 删除验证码
        myRedisCache.deleteObject(codeKey);

        return ResultData.success();
    }

    @Override
    public ResultData<Void> resetConfirm(UserResetConfirmDTO userResetDTO) {
        String email = userResetDTO.getEmail();
        
        User user = userService.findAccountByNameOrEmail(email, email);
        if (Objects.isNull(user)) {
            return ResultData.failure("该邮箱未注册");
        }

        String codeKey = MyStringUtils.buildRedisKey(RedisConstants.USER_CODE_KEY_PREFIX, email);
        String cachedCode = myRedisCache.getCacheObject(codeKey);
        
        if (cachedCode != null && cachedCode.equals(userResetDTO.getCode())) {
            myRedisCache.deleteObject(codeKey);
            return ResultData.success();
        }
        
        return ResultData.failure("验证码错误或已过期");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultData<Void> resetPassword(UserResetPasswordDTO userResetDTO) {
        String email = userResetDTO.getEmail();
        String code = userResetDTO.getCode();
        String newPassword = userResetDTO.getPassword();

        User user = userService.findAccountByNameOrEmail(email, email);
        if (Objects.isNull(user)) {
            return ResultData.failure("该邮箱未注册");
        }

        String codeKey = MyStringUtils.buildRedisKey(RedisConstants.USER_CODE_KEY_PREFIX, email);
        String cachedCode = myRedisCache.getCacheObject(codeKey);
        
        if (cachedCode == null || !cachedCode.equals(code)) {
            return ResultData.failure("验证码错误或已过期");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setUpdateTime(LocalDateTime.now());
        userService.updateById(user);
        
        myRedisCache.deleteObject(codeKey);
        
        return ResultData.success();
    }

    @Override
    public UserDetails loginOrRegisterThirdParty(String type, String accessToken) throws Exception {
        // 暂不实现，使用Spring Security OAuth2 Login
        return null;
    }
}
