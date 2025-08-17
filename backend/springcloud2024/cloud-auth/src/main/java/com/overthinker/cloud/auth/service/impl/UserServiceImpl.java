package com.overthinker.cloud.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.overthinker.cloud.auth.entity.DTO.*;
import com.overthinker.cloud.auth.entity.PO.User;
import com.overthinker.cloud.auth.entity.VO.UserAccountVO;
import com.overthinker.cloud.auth.entity.VO.UserDetailsVO;
import com.overthinker.cloud.auth.entity.VO.UserListVO;
import com.overthinker.cloud.auth.mapper.UserMapper;
import com.overthinker.cloud.auth.service.UserService;
import com.overthinker.cloud.auth.utils.SecurityUtils;
import com.overthinker.cloud.common.constants.RedisConstants;
import com.overthinker.cloud.common.resp.ResultData;
import com.overthinker.cloud.common.service.RedisService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private RedisService redisService;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.findAccountByNameOrEmail(username);
        if (Objects.isNull(user)) {
            throw new UsernameNotFoundException("用户不存在");
        }
        return user;
    }

    @Override
    public User findAccountByNameOrEmail(String text) {
        return userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, text).or().eq(User::getEmail, text));
    }

    @Override
    public UserAccountVO findAccountById(Long id) {
        User user = userMapper.selectById(id);
        return user.copyProperties(UserAccountVO.class);
    }

    @Override
    public void userLoginStatus(Long id, Integer type) {
        // Implement user login status logic here
    }

    @Override
    @Transactional
    public ResultData<Void> userRegister(UserRegisterDTO userRegisterDTO) {
        // Implement user register logic here
        return ResultData.success();
    }

    @Override
    public ResultData<Void> userResetConfirm(UserResetConfirmDTO userResetDTO) {
        // Implement user reset confirm logic here
        return ResultData.success();
    }

    @Override
    @Transactional
    public ResultData<Void> userResetPassword(UserResetPasswordDTO userResetDTO) {
        // Implement user reset password logic here
        return ResultData.success();
    }

    @Override
    public List<UserListVO> getUserOrSearch(UserSearchDTO userSearchDTO) {
        // Implement get user or search logic here
        return null;
    }

    @Override
    @Transactional
    public ResultData<Void> updateStatus(Long id, Integer status) {
        // Implement update status logic here
        return ResultData.success();
    }

    @Override
    public UserDetailsVO findUserDetailsById(Long id) {
        // Implement find user details by id logic here
        return null;
    }

    @Override
    @Transactional
    public ResultData<Void> deleteUser(List<Long> ids) {
        // Implement delete user logic here
        return ResultData.success();
    }

    @Override
    @Transactional
    public ResultData<Void> updateUser(UserUpdateDTO userUpdateDTO) {
        // Implement update user logic here
        return ResultData.success();
    }

    @Override
    public ResultData<String> uploadAvatar(MultipartFile avatarFile) throws Exception {
        // Implement upload avatar logic here
        return ResultData.success();
    }

    @Override
    @Transactional
    public ResultData<Void> updateEmailAndVerify(UpdateEmailDTO updateEmailDTO) {
        // Implement update email and verify logic here
        return ResultData.success();
    }

    @Override
    @Transactional
    public ResultData<Void> thirdUpdateEmail(UpdateEmailDTO updateEmailDTO) {
        Long userId = SecurityUtils.getUserId();
        User user = userMapper.selectById(userId);
        if (Objects.isNull(user)) {
            return ResultData.fail("用户不存在");
        }

        String redisCode = (String) redisService.get(RedisConstants.EMAIL_CODE_KEY + updateEmailDTO.getEmail());
        if (!updateEmailDTO.getCode().equals(redisCode)) {
            return ResultData.fail("验证码错误");
        }

        if (!passwordEncoder.matches(updateEmailDTO.getPassword(), user.getPassword())) {
            return ResultData.fail("密码错误");
        }

        user.setEmail(updateEmailDTO.getEmail());
        userMapper.updateById(user);
        redisService.del(RedisConstants.EMAIL_CODE_KEY + updateEmailDTO.getEmail());
        return ResultData.success();
    }
}
