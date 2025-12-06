package com.overthinker.cloud.auth.service;

import com.overthinker.cloud.auth.entity.SysUser;
import com.overthinker.cloud.auth.service.ISysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 自定义用户详情服务实现
 * 从数据库加载用户、角色和权限信息，供Spring Security使用。
 */
@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private ISysUserService sysUserService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1. 根据用户名从数据库加载用户
        SysUser sysUser = sysUserService.getUserByUsername(username);
        if (sysUser == null) {
            log.warn("用户 {} 不存在或密码错误！", username);
            throw new UsernameNotFoundException("用户不存在或密码错误！");
        }
        if (sysUser.getIsDeleted() == 1 || sysUser.getIsDisable() == 1) {
            log.warn("用户 {} 已被禁用或删除！", username);
            throw new UsernameNotFoundException("用户已被禁用！");
        }

        // 2. 获取用户角色和权限
        List<String> roles = sysUserService.getRolesByUserId(sysUser.getId());
        List<String> permissions = sysUserService.getPermissionsByUserId(sysUser.getId());

        // 3. 将角色和权限转换为Spring Security的GrantedAuthority
        Collection<? extends GrantedAuthority> authorities = Stream.concat(
                roles.stream().map(role -> "ROLE_" + role), // 角色需要加上"ROLE_"前缀
                permissions.stream()
        )
        .map(SimpleGrantedAuthority::new)
        .collect(Collectors.toList());

        // 4. 返回Spring Security的用户对象
        return new User(
                sysUser.getUsername(),
                sysUser.getPassword(), // 数据库中存储的是加密后的密码
                authorities
        );
    }
}
