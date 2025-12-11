package com.overthinker.cloud.auth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.overthinker.cloud.auth.entity.PO.SysRolePermission;
import com.overthinker.cloud.auth.mapper.SysPermissionMapper;
import com.overthinker.cloud.auth.service.ISysPermissionService;
import org.springframework.stereotype.Service;

@Service
public class SysPermissionServiceImpl extends ServiceImpl<SysPermissionMapper, SysRolePermission.SysPermission> implements ISysPermissionService {
}
