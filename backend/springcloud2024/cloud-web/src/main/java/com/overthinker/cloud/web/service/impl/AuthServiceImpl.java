package com.overthinker.cloud.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.overthinker.cloud.resp.ResultData;
import com.overthinker.cloud.resp.ReturnCodeEnum;
import com.overthinker.cloud.web.entity.DTO.UserRegisterDTO;
import com.overthinker.cloud.web.entity.DTO.UserResetConfirmDTO;
import com.overthinker.cloud.web.entity.DTO.UserResetPasswordDTO;
import com.overthinker.cloud.web.entity.PO.User;
import com.overthinker.cloud.redis.constants.RedisConst;
import com.overthinker.cloud.web.entity.constants.UserConst;
import com.overthinker.cloud.web.entity.enums.RegisterOrLoginTypeEnum;
import com.overthinker.cloud.web.mapper.UserMapper;
import com.overthinker.cloud.web.service.AuthService;
import com.overthinker.cloud.web.service.IpService;
import com.overthinker.cloud.web.utils.IpUtils;
import com.overthinker.cloud.web.utils.MyRedisCache;
import com.overthinker.cloud.web.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserMapper userMapper;
    private final MyRedisCache redisCache;
    private final PasswordEncoder passwordEncoder;
    private final IpService ipService;

    @Override
    public ResultData<Void> register(UserRegisterDTO userRegisterDTO) {
        // 1.判断验证码是否正确
        ResultData<Void> verifyCodeResult = verifyCode(userRegisterDTO.getEmail(), userRegisterDTO.getCode(), RedisConst.REGISTER);
        if (verifyCodeResult != null) return verifyCodeResult;

        // 2.判断用户名或邮箱是否已存在
        if (userIsExist(userRegisterDTO.getUsername(), userRegisterDTO.getEmail())) {
            return ResultData.failure(ReturnCodeEnum.USERNAME_OR_EMAIL_EXIST.getCode(), ReturnCodeEnum.USERNAME_OR_EMAIL_EXIST.getMsg());
        }
        // 3.密码加密
        String enPassword = passwordEncoder.encode(userRegisterDTO.getPassword());
        Date date = new Date();

        // 获取注册ip地址
        String ipAddr = IpUtils.getIpAddr(SecurityUtils.getCurrentHttpRequest());
        if (IpUtils.isUnknown(ipAddr)) {
            ipAddr = IpUtils.getHostIp();
        }
        // 4.保存用户信息
        User user = User.builder()
                .nickname(userRegisterDTO.getUsername())
                .username(userRegisterDTO.getUsername())
                .password(enPassword)
                .registerType(RegisterOrLoginTypeEnum.EMAIL.getRegisterType())
                .registerIp(ipAddr)
                .gender(UserConst.DEFAULT_GENDER)
                .avatar(UserConst.DEFAULT_AVATAR)
                .intro(UserConst.DEFAULT_INTRODUCTION)
                .registerAddress(ipAddr)
                .isDeleted(UserConst.DEFAULT_STATUS)
                .email(userRegisterDTO.getEmail())
                .loginTime(date).build();
        if (userMapper.insert(user) > 0) {
            // 删除验证码
            ipService.refreshIpDetailAsyncByUidAndRegister(user.getId());
            redisCache.deleteObject(RedisConst.VERIFY_CODE + RedisConst.REGISTER + RedisConst.SEPARATOR + userRegisterDTO.getEmail());
            return ResultData.success();
        } else {
            return ResultData.failure();
        }
    }

    private boolean userIsExist(String username, String email) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(username != null, User::getUsername, username).or().eq(email != null, User::getEmail, email);
        return userMapper.exists(wrapper);
    }

    private ResultData<Void> verifyCode(String email, String code, String type) {
        String redisCode = redisCache.getCacheObject(RedisConst.VERIFY_CODE + type + RedisConst.SEPARATOR + email);
        if (redisCode == null)
            return ResultData.failure(ReturnCodeEnum.VERIFY_CODE_ERROR.getCode(), "验证码已过期或不存在");
        if (!redisCode.equals(code))
            return ResultData.failure(ReturnCodeEnum.VERIFY_CODE_ERROR.getCode(), "验证码错误");
        return null;
    }

    @Override
    public ResultData<Void> resetConfirm(UserResetConfirmDTO userResetDTO) {
        // 1.判断验证码是否正确
        ResultData<Void> verifyCodeResult = verifyCode(userResetDTO.getEmail(), userResetDTO.getCode(), RedisConst.RESET);
        if (verifyCodeResult != null) return verifyCodeResult;
        return ResultData.success();
    }

    @Override
    public ResultData<Void> resetPassword(UserResetPasswordDTO userResetDTO) {
        // ... (implementation)
    }

    @Override
    public UserDetails loginOrRegisterThirdParty(String type, String accessToken) throws Exception {
        // This is a simplified migration. The full logic from UserServiceImpl's
        // loadUserByUsername for third-party login should be moved here.
        // For now, we'll just add a placeholder.
        log.info("Attempting third-party login for type: {}", type);
        // TODO: Implement the actual logic by moving it from UserServiceImpl.
        return null;
    }
        // 校验验证码
        ResultData<Void> verifyCodeResult = verifyCode(userResetDTO.getEmail(), userResetDTO.getCode(), RedisConst.RESET);
        if (verifyCodeResult != null) return verifyCodeResult;
        String password = passwordEncoder.encode(userResetDTO.getPassword());
        User user = User.builder().password(password).build();
        if (userMapper.update(user, new LambdaQueryWrapper<User>().eq(User::getEmail, userResetDTO.getEmail())) > 0) {
            // 删除验证码
            redisCache.deleteObject(RedisConst.VERIFY_CODE + RedisConst.RESET + RedisConst.SEPARATOR + userResetDTO.getEmail());
            return ResultData.success();
        } else {
            return ResultData.failure();
        }
    }
}
