package com.overthinker.cloud.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.overthinker.cloud.api.auth.dto.PermissionDTO;
import com.overthinker.cloud.auth.entity.PO.SysPermission;

import java.util.List;

public interface PermissionService extends IService<SysPermission> {

    void registerPermissions(List<PermissionDTO> permissions);
}