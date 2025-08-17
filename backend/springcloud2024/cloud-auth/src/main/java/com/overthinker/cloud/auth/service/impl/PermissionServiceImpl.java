package com.overthinker.cloud.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.overthinker.cloud.api.dto.PermissionDTO;
import com.overthinker.cloud.auth.entity.PO.SysPermission;
import com.overthinker.cloud.auth.mapper.SysPermissionMapper;
import com.overthinker.cloud.auth.service.PermissionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PermissionServiceImpl extends ServiceImpl<SysPermissionMapper, SysPermission> implements PermissionService {

    @Override
    @Transactional
    public void registerPermissions(List<PermissionDTO> permissions) {
        for (PermissionDTO dto : permissions) {
            // 检查是否已存在具有相同路径和方法的权限
            QueryWrapper<SysPermission> wrapper = new QueryWrapper<>();
            wrapper.eq("path", dto.path());
            wrapper.eq("http_method", dto.httpMethod());

            SysPermission existingPermission = this.getOne(wrapper);

            if (existingPermission != null) {
                // 更新现有权限
                existingPermission.setCategory(dto.category());
                existingPermission.setName(dto.name());
                this.updateById(existingPermission);
            } else {
                // 插入新权限
                SysPermission newPermission = new SysPermission();
                newPermission.setCategory(dto.category());
                newPermission.setName(dto.name());
                newPermission.setPath(dto.path());
                newPermission.setHttpMethod(dto.httpMethod());
                this.save(newPermission);
            }
        }
    }
}
