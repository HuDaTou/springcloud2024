package com.overthinker.cloud.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.overthinker.cloud.auth.entity.PO.RolePermission;

import java.util.List;

public interface RolePermissionService extends IService<RolePermission> {

    void assignPermissionsToRole(Long roleId, List<Long> permissionIds);

    List<Long> getPermissionIdsByRoleId(Long roleId);
}
