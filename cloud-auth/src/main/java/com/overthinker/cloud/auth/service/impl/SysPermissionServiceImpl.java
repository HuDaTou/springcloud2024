package com.overthinker.cloud.auth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.overthinker.cloud.auth.entity.PO.SysPermission;
import com.overthinker.cloud.auth.mapper.SysPermissionMapper;
import com.overthinker.cloud.auth.service.PermissionService;
import org.springframework.stereotype.Service;

@Service
public class SysPermissionServiceImpl extends ServiceImpl<SysPermissionMapper, SysPermission> implements PermissionService {
}
