package com.overthinker.cloud.auth.controller;

import com.overthinker.cloud.auth.entity.PO.SysRolePermission;
import com.overthinker.cloud.auth.service.ISysPermissionService;
import com.overthinker.cloud.common.core.resp.ResultData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "权限管理", description = "系统权限数据的增删改查及注册接口")
@RestController
@RequestMapping("/permissions")
public class PermissionController {

    @Resource
    private ISysPermissionService permissionService;

    @Operation(summary = "获取所有权限", description = "获取系统中所有的权限列表")
    @GetMapping
    public ResultData<List<SysRolePermission.SysPermission>> getAllPermissions() {
        return ResultData.success(permissionService.list());
    }

    @Operation(summary = "创建权限", description = "手动创建一个新的权限")
    @PostMapping
    public ResultData<Void> createPermission(@RequestBody SysRolePermission.SysPermission permission) {
        permissionService.save(permission);
        return ResultData.success();
    }

    @Operation(summary = "更新权限", description = "根据ID更新权限信息")
    @PutMapping("/{id}")
    public ResultData<Void> updatePermission(
            @Parameter(description = "权限ID", required = true) @PathVariable Long id,
            @RequestBody SysRolePermission.SysPermission permission) {
        permission.setId(id);
        permissionService.updateById(permission);
        return ResultData.success();
    }

    @Operation(summary = "删除权限", description = "根据ID删除权限")
    @DeleteMapping("/{id}")
    public ResultData<Void> deletePermission(
            @Parameter(description = "权限ID", required = true) @PathVariable Long id) {
        permissionService.removeById(id);
        return ResultData.success();
    }
}
