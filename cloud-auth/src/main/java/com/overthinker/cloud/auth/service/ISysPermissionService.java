package com.overthinker.cloud.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.overthinker.cloud.auth.entity.PO.SysRolePermission;

public interface ISysPermissionService extends IService<SysRolePermission.SysPermission> {
    // 可以在这里添加根据code查询权限等方法
}
