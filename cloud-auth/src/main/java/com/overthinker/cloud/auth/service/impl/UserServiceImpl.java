package com.overthinker.cloud.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.overthinker.cloud.auth.entity.DTO.*;
import com.overthinker.cloud.auth.entity.PO.*;
import com.overthinker.cloud.auth.entity.VO.UserAccountVO;
import com.overthinker.cloud.auth.entity.VO.UserDetailsVO;
import com.overthinker.cloud.auth.entity.VO.UserListVO;
import com.overthinker.cloud.auth.mapper.*;
import com.overthinker.cloud.auth.service.UserService;
import com.overthinker.cloud.auth.utils.SecurityUtils;

import com.overthinker.cloud.common.core.resp.ResultData;

import com.overthinker.cloud.auth.constants.AuthRedisConst;
import com.overthinker.cloud.system.redis.utils.MyRedisCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import lombok.RequiredArgsConstructor;

@Slf4j
@Service("userService")
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final UserMapper userMapper;

    private final UserRoleMapper userRoleMapper;

        
    private final RolePermissionMapper rolePermissionMapper;

    
    private final SysPermissionMapper sysPermissionMapper;



    
    private final RoleMapper roleMapper;

    
    private final MyRedisCache myRedisCache;

    private final PasswordEncoder passwordEncoder;

    private final BlackListServiceImpl blackListService;

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        User user = this.findAccountByNameOrEmail(usernameOrEmail, usernameOrEmail);
        if (Objects.isNull(user)) {
            throw new UsernameNotFoundException("用户不存在");
        }
        
        if (user.getIsDisable() != null && user.getIsDisable() == 1) {
            throw new UsernameNotFoundException("用户已被禁用");
        }

        BlackList byId = blackListService.getById(user.getId());
        if (Objects.nonNull(byId)) {
            throw new UsernameNotFoundException("用户已被封禁");
        }

        return new LoginUser(user, this.getUserAuthorities(user.getId()));
    }

    @Override
    public User findAccountByNameOrEmail(String userName,String email) {
        return userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, userName).or().eq(User::getEmail, email));
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
        User user = userMapper.selectById(id);
        if (Objects.nonNull(user)) {
            user.setLoginType(type);
            user.setLoginTime(LocalDateTime.now());
            userMapper.updateById(user);
        }
    }

    @Override
    @Transactional
    public ResultData<Void> userRegister(UserRegisterDTO userRegisterDTO) {
        String codeKey = AuthRedisConst.EMAIL_VERIFY_CODE_KEY + userRegisterDTO.getEmail();
        String cachedCode = myRedisCache.getCacheObject(codeKey);
        
        if (cachedCode == null || !cachedCode.equals(userRegisterDTO.getCode())) {
            return ResultData.failure("验证码错误或已过期");
        }

        User existing = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, userRegisterDTO.getUsername())
                .or()
                .eq(User::getEmail, userRegisterDTO.getEmail()));
        
        if (Objects.nonNull(existing)) {
            return ResultData.failure("用户名或邮箱已存在");
        }

        User user = new User();
        user.setUsername(userRegisterDTO.getUsername());
        user.setNickname(userRegisterDTO.getUsername());
        user.setEmail(userRegisterDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userRegisterDTO.getPassword()));
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        user.setAvatar("https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png");
        user.setRegisterType(0);
        user.setIsDisable(0);
        
        userMapper.insert(user);

        SysRole defaultRole = roleMapper.selectOne(new LambdaQueryWrapper<SysRole>()
                .eq(SysRole::getRoleKey, "ROLE_USER"));
        if (Objects.nonNull(defaultRole)) {
            UserRole userRole = new UserRole();
            userRole.setUserId(user.getId());
            userRole.setRoleId(defaultRole.getId());
            userRoleMapper.insert(userRole);
        }

        myRedisCache.deleteObject(codeKey);
        return ResultData.success();
    }

    @Override
    public ResultData<Void> userResetConfirm(UserResetConfirmDTO userResetDTO) {
        String codeKey = AuthRedisConst.EMAIL_VERIFY_CODE_KEY + userResetDTO.getEmail();
        String cachedCode = myRedisCache.getCacheObject(codeKey);
        
        if (cachedCode != null && cachedCode.equals(userResetDTO.getCode())) {
            myRedisCache.deleteObject(codeKey);
            return ResultData.success();
        }
        return ResultData.failure("验证码错误或已过期");
    }

    @Override
    @Transactional
    public ResultData<Void> userResetPassword(UserResetPasswordDTO userResetDTO) {
        String codeKey = AuthRedisConst.EMAIL_VERIFY_CODE_KEY + userResetDTO.getEmail();
        String cachedCode = myRedisCache.getCacheObject(codeKey);
        
        if (cachedCode == null || !cachedCode.equals(userResetDTO.getCode())) {
            return ResultData.failure("验证码错误或已过期");
        }

        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getEmail, userResetDTO.getEmail()));
        
        if (Objects.isNull(user)) {
            return ResultData.failure("该邮箱未注册");
        }

        user.setPassword(passwordEncoder.encode(userResetDTO.getPassword()));
        user.setUpdateTime(LocalDateTime.now());
        userMapper.updateById(user);
        
        myRedisCache.deleteObject(codeKey);
        return ResultData.success();
    }

    @Override
    public List<UserListVO> getUserOrSearch(UserSearchDTO userSearchDTO) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        
        if (StringUtils.isNotBlank(userSearchDTO.getUsername())) {
            queryWrapper.like(User::getUsername, userSearchDTO.getUsername());
        }
        if (StringUtils.isNotBlank(userSearchDTO.getEmail())) {
            queryWrapper.like(User::getEmail, userSearchDTO.getEmail());
        }
        if (userSearchDTO.getIsDisable() != null) {
            queryWrapper.eq(User::getIsDisable, userSearchDTO.getIsDisable());
        }
        if (userSearchDTO.getCreateTimeStart() != null) {
            queryWrapper.ge(User::getCreateTime, userSearchDTO.getCreateTimeStart());
        }
        if (userSearchDTO.getCreateTimeEnd() != null) {
            queryWrapper.le(User::getCreateTime, userSearchDTO.getCreateTimeEnd());
        }
        
        queryWrapper.orderByDesc(User::getCreateTime);
        
        List<User> users = userMapper.selectList(queryWrapper);
        return users.stream().map(user -> {
            UserListVO vo = new UserListVO();
            vo.setId(user.getId());
            vo.setUsername(user.getUsername());
            vo.setAvatar(user.getAvatar());
            vo.setEmail(user.getEmail());
            vo.setRegisterType(user.getRegisterType());
            vo.setLoginAddress(user.getLoginAddress());
            vo.setIsDisable(user.getIsDisable());
            vo.setCreateTime(user.getCreateTime());
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ResultData<Void> updateStatus(Long id, Integer status) {
        User user = userMapper.selectById(id);
        if (Objects.isNull(user)) {
            return ResultData.failure("用户不存在");
        }
        user.setIsDisable(status);
        user.setUpdateTime(LocalDateTime.now());
        userMapper.updateById(user);
        return ResultData.success();
    }

    @Override
    public UserDetailsVO findUserDetailsById(Long id) {
        User user = userMapper.selectById(id);
        if (Objects.isNull(user)) {
            return null;
        }
        
        UserDetailsVO vo = new UserDetailsVO();
        vo.setId(user.getId());
        vo.setNickname(user.getNickname());
        vo.setUsername(user.getUsername());
        vo.setRoles(this.getUserRoleNames(id));
        vo.setGender(user.getGender());
        vo.setAvatar(user.getAvatar());
        vo.setIntro(user.getIntro());
        vo.setEmail(user.getEmail());
        vo.setRegisterType(user.getRegisterType());
        vo.setRegisterIp(user.getRegisterIp());
        vo.setRegisterAddress(user.getRegisterAddress());
        vo.setLoginType(user.getLoginType());
        vo.setLoginIp(user.getLoginIp());
        vo.setLoginAddress(user.getLoginAddress());
        vo.setIsDisable(user.getIsDisable());
        vo.setLoginTime(user.getLoginTime());
        vo.setCreateTime(user.getCreateTime());
        vo.setUpdateTime(user.getUpdateTime());
        
        return vo;
    }

    @Override
    @Transactional
    public ResultData<Void> deleteUser(List<Long> ids) {
        userRoleMapper.delete(new LambdaQueryWrapper<UserRole>().in(UserRole::getUserId, ids));
        userMapper.deleteBatchIds(ids);
        return ResultData.success();
    }

    @Override
    @Transactional
    public ResultData<Void> updateUser(UserUpdateDTO userUpdateDTO) {
        Long userId = SecurityUtils.getUserId();
        User user = userMapper.selectById(userId);
        if (Objects.isNull(user)) {
            return ResultData.failure("用户不存在");
        }
        
        user.setNickname(userUpdateDTO.getNickname());
        user.setGender(userUpdateDTO.getGender());
        user.setAvatar(userUpdateDTO.getAvatar());
        user.setIntro(userUpdateDTO.getIntro());
        user.setUpdateTime(LocalDateTime.now());
        
        userMapper.updateById(user);
        return ResultData.success();
    }

    @Override
    public ResultData<String> uploadAvatar(MultipartFile avatarFile) throws Exception {
        String fileName = UUID.randomUUID().toString() + "." + 
                avatarFile.getOriginalFilename().split("\\.")[1];
        String avatarUrl = "https://example.com/avatar/" + fileName;
        return ResultData.success(avatarUrl);
    }

    @Override
    @Transactional
    public ResultData<Void> updateEmailAndVerify(UpdateEmailDTO updateEmailDTO) {
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
        user.setUpdateTime(LocalDateTime.now());
        userMapper.updateById(user);
        myRedisCache.deleteObject(AuthRedisConst.EMAIL_VERIFY_CODE_KEY + updateEmailDTO.getEmail());
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
        return sysPermissionMapper.selectBatchIds(permissionIds).stream().map(SysPermission::getPermissonCode).toList();
    }

    @Override
    public List<String> getUserRoleNames(Long userId) {
        List<UserRole> userRoles = userRoleMapper.selectList(new LambdaQueryWrapper<UserRole>().eq(UserRole::getUserId, userId));
        if (userRoles.isEmpty()) {
            return Collections.emptyList();
        }
        List<Long> roleIds = userRoles.stream().map(UserRole::getRoleId).toList();
        return roleMapper.selectBatchIds(roleIds).stream().map(SysRole::getRoleName).toList();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultData<Void> adminCreateUser(AdminCreateUserDTO adminCreateUserDTO) {
        // 检查用户名是否已存在
        User existingUser = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, adminCreateUserDTO.getUsername()));
        if (Objects.nonNull(existingUser)) {
            return ResultData.failure("用户名已存在");
        }

        // 检查邮箱是否已存在（如果提供了邮箱）
        if (StringUtils.isNotBlank(adminCreateUserDTO.getEmail())) {
            User existingEmail = userMapper.selectOne(new LambdaQueryWrapper<User>()
                    .eq(User::getEmail, adminCreateUserDTO.getEmail()));
            if (Objects.nonNull(existingEmail)) {
                return ResultData.failure("邮箱已存在");
            }
        }

        // 创建用户
        User user = new User();
        user.setUsername(adminCreateUserDTO.getUsername());
        user.setNickname(StringUtils.isNotBlank(adminCreateUserDTO.getNickname()) 
                ? adminCreateUserDTO.getNickname() 
                : adminCreateUserDTO.getUsername());
        user.setEmail(adminCreateUserDTO.getEmail());
        user.setPassword(passwordEncoder.encode(adminCreateUserDTO.getPassword()));
        user.setIsDisable(adminCreateUserDTO.getIsDisable() ? 1 : 0);
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        user.setAvatar("https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png");
        user.setRegisterType(0);

        userMapper.insert(user);

        // 分配角色
        List<Long> roleIds = adminCreateUserDTO.getRoleIds();
        if (roleIds != null && !roleIds.isEmpty()) {
            for (Long roleId : roleIds) {
                UserRole userRole = new UserRole();
                userRole.setUserId(user.getId());
                userRole.setRoleId(roleId);
                userRoleMapper.insert(userRole);
            }
        } else {
            // 如果没有指定角色，分配默认角色
            SysRole defaultRole = roleMapper.selectOne(new LambdaQueryWrapper<SysRole>()
                    .eq(SysRole::getRoleKey, "USER"));
            if (Objects.nonNull(defaultRole)) {
                UserRole userRole = new UserRole();
                userRole.setUserId(user.getId());
                userRole.setRoleId(defaultRole.getId());
                userRoleMapper.insert(userRole);
            }
        }

        return ResultData.success();
    }
    
    @Override
    public void updateLoginInfo(Long userId, String loginIp, String userAgent) {
        User user = userMapper.selectById(userId);
        if (Objects.isNull(user)) {
            log.warn("更新登录信息失败：用户不存在，userId: {}", userId);
            return;
        }
        
        user.setLoginIp(loginIp);
        user.setLoginTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        
        userMapper.updateById(user);
        log.info("更新用户登录信息成功：userId: {}, loginIp: {}", userId, loginIp);
    }
}
