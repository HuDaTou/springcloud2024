package com.overthinker.cloud.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.overthinker.cloud.auth.entity.DTO.*;
import com.overthinker.cloud.auth.entity.PO.LoginUser;
import com.overthinker.cloud.auth.entity.PO.RolePermission;
import com.overthinker.cloud.auth.entity.PO.User;
import com.overthinker.cloud.auth.entity.PO.UserRole;
import com.overthinker.cloud.auth.entity.VO.UserAccountVO;
import com.overthinker.cloud.auth.entity.VO.UserDetailsVO;
import com.overthinker.cloud.auth.entity.VO.UserListVO;
import com.overthinker.cloud.auth.mapper.*;
import com.overthinker.cloud.auth.service.UserService;
import com.overthinker.cloud.auth.utils.SecurityUtils;

import com.overthinker.cloud.common.resp.ResultData;

import com.overthinker.cloud.auth.constants.AuthRedisConst;
import com.overthinker.cloud.redis.utils.MyRedisCache;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserRoleMapper userRoleMapper;

    @Resource
    private RolePermissionMapper rolePermissionMapper;

    @Resource
    private PermissionMapper permissionMapper;

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private MyRedisCache myRedisCache;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.findAccountByNameOrEmail(username);
        if (Objects.isNull(user)) {
            throw new UsernameNotFoundException("用户不存在");
        }
        return new LoginUser(user, this.getUserAuthorities(user.getId()));
    }

    @Override
    public User findAccountByNameOrEmail(String text) {
        return userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, text).or().eq(User::getEmail, text));
    }

    @Override
    public UserAccountVO findAccountById(Long id) {
        User user = userMapper.selectById(id);
        UserAccountVO vo = user.copyProperties(UserAccountVO.class);
        vo.setPermissions(this.getUserAuthorities(id));
        vo.setRoles(this.getUserRoleNames(id));
        return vo;
    }

    @Override
    public void userLoginStatus(Long id, Integer type) {
        // 在此处实现用户登录状态逻辑
    }

    @Override
    @Transactional
    public ResultData<Void> userRegister(UserRegisterDTO userRegisterDTO) {
        // 在此处实现用户注册逻辑
        return ResultData.success();
    }

    @Override
    public ResultData<Void> userResetConfirm(UserResetConfirmDTO userResetDTO) {
        // 在此处实现用户重置确认逻辑
        return ResultData.success();
    }

    @Override
    @Transactional
    public ResultData<Void> userResetPassword(UserResetPasswordDTO userResetDTO) {
        // 在此处实现用户重置密码逻辑
        return ResultData.success();
    }

    @Override
    public List<UserListVO> getUserOrSearch(UserSearchDTO userSearchDTO) {
        // 在此处实现获取用户或搜索逻辑
        return null;
    }

    @Override
    @Transactional
    public ResultData<Void> updateStatus(Long id, Integer status) {
        // 在此处实现更新状态逻辑
        return ResultData.success();
    }

    @Override
    public UserDetailsVO findUserDetailsById(Long id) {
        // 在此处实现通过id查找用户详细信息逻辑
        return null;
    }

    @Override
    @Transactional
    public ResultData<Void> deleteUser(List<Long> ids) {
        // 在此处实现删除用户逻辑
        return ResultData.success();
    }

    @Override
    @Transactional
    public ResultData<Void> updateUser(UserUpdateDTO userUpdateDTO) {
        // 在此处实现更新用户逻辑
        return ResultData.success();
    }

    @Override
    public ResultData<String> uploadAvatar(MultipartFile avatarFile) throws Exception {
        // 在此处实现上传头像逻辑
        return ResultData.success();
    }

    @Override
    @Transactional
    public ResultData<Void> updateEmailAndVerify(UpdateEmailDTO updateEmailDTO) {
        // 在此处实现更新邮件和验证逻辑
        return ResultData.success();
    }

    @Override
    @Transactional
    public ResultData<Void> thirdUpdateEmail(UpdateEmailDTO updateEmailDTO) {
        Long userId = SecurityUtils.getUserId();
        User user = userMapper.selectById(userId);
        if (Objects.isNull(user)) {
            return ResultData.failure("用户不存在");
        }

        String redisCode = myRedisCache.getCacheObject(AuthRedisConst.EMAIL_VERIFY_CODE_KEY + updateEmailDTO.getEmail());
        if (!updateEmailDTO.getCode().equals(redisCode)) {
            return ResultData.failure("验证码错误");
        }

        if (!passwordEncoder.matches(updateEmailDTO.getPassword(), user.getPassword())) {
            return ResultData.failure("密码错误");
        }

        user.setEmail(updateEmailDTO.getEmail());
        userMapper.updateById(user);
        myRedisCache.deleteObject(AuthRedisConst.EMAIL_VERIFY_CODE_KEY + updateEmailDTO.getEmail());
        return ResultData.success();
    }

    @Override
    public List<String> getUserAuthorities(Long userId) {
        List<UserRole> userRoles = userRoleMapper.selectList(new LambdaQueryWrapper<UserRole>().eq(UserRole::getUserId, userId));
        if (userRoles.isEmpty()) {
            return Collections.emptyList();
        }
        List<Long> roleIds = userRoles.stream().map(UserRole::getRoleId).toList();
        List<RolePermission> rolePermissions = rolePermissionMapper.selectList(new LambdaQueryWrapper<RolePermission>().in(RolePermission::getRoleId, roleIds));
        if (rolePermissions.isEmpty()) {
            return Collections.emptyList();
        }
        List<Long> permissionIds = rolePermissions.stream().map(RolePermission::getPermissionId).toList();
        return permissionMapper.selectBatchIds(permissionIds).stream().map(com.overthinker.cloud.auth.entity.PO.Permission::getPermissionKey).toList();
    }

    @Override
    public List<String> getUserRoleNames(Long userId) {
        List<UserRole> userRoles = userRoleMapper.selectList(new LambdaQueryWrapper<UserRole>().eq(UserRole::getUserId, userId));
        if (userRoles.isEmpty()) {
            return Collections.emptyList();
        }
        List<Long> roleIds = userRoles.stream().map(UserRole::getRoleId).toList();
        return roleMapper.selectBatchIds(roleIds).stream().map(com.overthinker.cloud.auth.entity.PO.Role::getRoleName).toList();
    }
}
