package com.overthinker.cloud.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.overthinker.cloud.auth.entity.LoginUser;
import com.overthinker.cloud.auth.entity.User;
import com.overthinker.cloud.auth.entity.Role;
import com.overthinker.cloud.auth.entity.Permission;
import com.overthinker.cloud.auth.entity.UserRole;
import com.overthinker.cloud.auth.entity.RolePermission;
import com.overthinker.cloud.auth.mapper.UserMapper;
import com.overthinker.cloud.auth.mapper.RoleMapper;
import com.overthinker.cloud.auth.mapper.PermissionMapper;
import com.overthinker.cloud.auth.mapper.UserRoleMapper;
import com.overthinker.cloud.auth.mapper.RolePermissionMapper;
import com.overthinker.cloud.common.constant.SecurityConst;
import jakarta.annotation.Resource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * User service implementation for authentication and authorization.
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserDetailsService {

    @Resource
    private UserMapper userMapper;
    @Resource
    private RoleMapper roleMapper;
    @Resource
    private UserRoleMapper userRoleMapper;
    @Resource
    private RolePermissionMapper rolePermissionMapper;
    @Resource
    private PermissionMapper permissionMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // In auth service, we only support loading by username/email from our own DB.
        // Third-party login logic should be handled by a dedicated controller.
        User user = findAccountByNameOrEmail(username);

        if (ObjectUtils.isEmpty(user)) {
            throw new UsernameNotFoundException("Username or password error.");
        }

        // Check if user is disabled
        if (user.getIsDisable() == 1) {
            throw new UsernameNotFoundException("Account is disabled.");
        }

        List<String> authorities = getUserAuthorities(user.getId());
        return new LoginUser(user, authorities);
    }

    public User findAccountByNameOrEmail(String text) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, text).or().eq(User::getEmail, text);
        return userMapper.selectOne(wrapper);
    }

    public List<String> getUserAuthorities(Long userId) {
        // Get user roles
        List<UserRole> userRoles = userRoleMapper.selectList(new LambdaQueryWrapper<UserRole>().eq(UserRole::getUserId, userId));
        if (userRoles.isEmpty()) {
            return List.of();
        }

        List<Long> roleIds = userRoles.stream().map(UserRole::getRoleId).collect(Collectors.toList());
        List<Role> roles = roleMapper.selectBatchIds(roleIds);
        roles = roles.stream().filter(role -> Objects.equals(role.getStatus(), 0)).collect(Collectors.toList()); // Assuming 0 is active

        if (roles.isEmpty()) {
            return List.of();
        }

        // Get role permissions
        List<Long> activeRoleIds = roles.stream().map(Role::getId).collect(Collectors.toList());
        List<RolePermission> rolePermissions = rolePermissionMapper.selectList(new LambdaQueryWrapper<RolePermission>().in(RolePermission::getRoleId, activeRoleIds));
        if (rolePermissions.isEmpty()) {
            return roles.stream().map(role -> SecurityConst.ROLE_PREFIX + role.getRoleKey()).collect(Collectors.toList());
        }

        List<Long> permissionIds = rolePermissions.stream().map(RolePermission::getPermissionId).collect(Collectors.toList());
        List<Permission> permissions = permissionMapper.selectBatchIds(permissionIds);

        // Combine roles and permissions
        List<String> authorities = permissions.stream().map(Permission::getPermissionKey).collect(Collectors.toList());
        authorities.addAll(roles.stream().map(role -> SecurityConst.ROLE_PREFIX + role.getRoleKey()).collect(Collectors.toList()));

        return authorities;
    }
}
