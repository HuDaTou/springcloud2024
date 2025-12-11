package com.overthinker.cloud.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.overthinker.cloud.auth.entity.PO.SysUser;

import java.util.List;

public interface ISysUserService extends IService<SysUser> {
    /**
     * 根据用户名获取用户及权限信息
     * @param username 用户名
     * @return 用户对象（包含角色和权限信息，需后续封装）
     */
    SysUser getUserByUsername(String username);

    /**
     * 根据邮箱获取用户
     * @param email 邮箱
     * @return 用户对象
     */
    SysUser getUserByEmail(String email);

    /**
     * 根据用户ID获取角色列表
     * @param userId 用户ID
     * @return 角色标识列表
     */
    List<String> getRolesByUserId(Long userId);

    /**
     * 根据用户ID获取权限列表
     * @param userId 用户ID
     * @return 权限标识列表
     */
    List<String> getPermissionsByUserId(Long userId);
}
