package com.overthinker.cloud.auth.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.overthinker.cloud.auth.dto.PermissionDTO; // This DTO will be from the starter
import com.overthinker.cloud.auth.entity.SysPermission;
import com.overthinker.cloud.auth.mapper.SysPermissionMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service for managing permissions.
 */
@Service
public class PermissionService extends ServiceImpl<SysPermissionMapper, SysPermission> {

    @Transactional
    public void registerPermissions(List<PermissionDTO> permissions) {
        for (PermissionDTO dto : permissions) {
            // Check if a permission with the same path and method already exists
            QueryWrapper<SysPermission> wrapper = new QueryWrapper<>();
            wrapper.eq("path", dto.path());
            wrapper.eq("http_method", dto.httpMethod());

            SysPermission existingPermission = this.getOne(wrapper);

            if (existingPermission != null) {
                // Update existing permission
                existingPermission.setCategory(dto.category());
                existingPermission.setName(dto.name());
                this.updateById(existingPermission);
            } else {
                // Insert new permission
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
