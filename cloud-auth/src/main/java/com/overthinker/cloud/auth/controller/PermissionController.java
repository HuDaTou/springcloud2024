package com.overthinker.cloud.auth.controller;

import com.overthinker.cloud.api.dto.PermissionDTO;
import com.overthinker.cloud.auth.entity.PO.SysPermission;
import com.overthinker.cloud.auth.service.PermissionService;

import com.overthinker.cloud.common.core.resp.ResultData;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/permissions")
public class PermissionController {

    @Resource
    private PermissionService permissionService;

    @GetMapping
    public ResultData<List<SysPermission>> getAllPermissions() {
        return ResultData.success(permissionService.list());
    }

    @PostMapping
    public ResultData<Void> createPermission(@RequestBody SysPermission permission) {
        permissionService.save(permission);
        return ResultData.success();
    }

    @PutMapping("/{id}")
    public ResultData<Void> updatePermission(@PathVariable Long id, @RequestBody SysPermission permission) {
        permission.setId(id);
        permissionService.updateById(permission);
        return ResultData.success();
    }

    @DeleteMapping("/{id}")
    public ResultData<Void> deletePermission(@PathVariable Long id) {
        permissionService.removeById(id);
        return ResultData.success();
    }

    @PostMapping("/internal/register")
    public ResultData<Void> registerPermissions(@RequestBody List<PermissionDTO> permissions) {
        permissionService.registerPermissions(permissions);
        return ResultData.success();
    }
}
