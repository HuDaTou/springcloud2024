package com.overthinker.cloud.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.overthinker.cloud.auth.entity.DTO.UserRegisterDTO;
import com.overthinker.cloud.auth.entity.DTO.UserResetConfirmDTO;
import com.overthinker.cloud.auth.entity.DTO.UserResetPasswordDTO;
import com.overthinker.cloud.auth.entity.SysUser;
import com.overthinker.cloud.auth.entity.SysUserRole;
import com.overthinker.cloud.auth.mapper.SysUserMapper;
import com.overthinker.cloud.auth.mapper.SysUserRoleMapper;
import com.overthinker.cloud.auth.service.AuthService;
import com.overthinker.cloud.common.core.resp.ResultData;
import com.overthinker.cloud.system.redis.constant.RedisConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private SysUserMapper userMapper;

    @Autowired
    private SysUserRoleMapper userRoleMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private StringRedisTemplate redisTemplate;

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
        if (userMapper.selectCount(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, dto.getUsername())) > 0) {
            return ResultData.failure("用户名已存在");
        }

        // 3. 检查邮箱是否存在
        if (userMapper.selectCount(new LambdaQueryWrapper<SysUser>().eq(SysUser::getEmail, dto.getEmail())) > 0) {
            return ResultData.failure("邮箱已被注册");
        }

        // 4. 创建用户
        SysUser user = new SysUser();
        user.setUsername(dto.getUsername());
        user.setNickname(dto.getUsername()); // 默认昵称同用户名
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        user.setIsDeleted(0);
        user.setIsDisable(0);
        // 设置默认头像等
        user.setAvatar("https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png");

        userMapper.insert(user);

        // 5. 分配默认角色 (如 USER)
        SysUserRole userRole = new SysUserRole();
        userRole.setUserId(user.getId());
        userRole.setRoleId("3"); // 假设3是普通用户角色ID
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
