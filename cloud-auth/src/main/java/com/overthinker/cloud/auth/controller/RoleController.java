package com.overthinker.cloud.auth.controller;

import com.overthinker.cloud.auth.entity.PO.SysRole;
import com.overthinker.cloud.auth.service.RolePermissionService;
import com.overthinker.cloud.auth.service.RoleService;
import com.overthinker.cloud.common.core.resp.ResultData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Tag(name = "角色管理", description = "系统角色数据的增删改查接口")
@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService;
    private final RolePermissionService rolePermissionService;

    @Operation(summary = "获取所有角色", description = "获取系统中所有的角色列表")
    @PreAuthorize("hasAuthority('auth:role:list')")
    @GetMapping
    public ResultData<List<SysRole>> getAllRoles() {
        return ResultData.success(roleService.list());
    }

    @Operation(summary = "创建角色", description = "创建一个新的角色")
    @PreAuthorize("hasAuthority('auth:role:add')")
    @PostMapping
    public ResultData<Void> createRole(@RequestBody @Valid SysRole role) {
        roleService.save(role);
        return ResultData.success();
    }

    @Operation(summary = "更新角色", description = "根据ID更新角色信息")
    @PreAuthorize("hasAuthority('auth:role:edit')")
    @PutMapping("/{id}")
    public ResultData<Void> updateRole(
            @Parameter(description = "角色ID", required = true) @PathVariable("id") Long id,
            @RequestBody @Valid SysRole role) {
        role.setId(id);
        roleService.updateById(role);
        return ResultData.success();
    }

    @Operation(summary = "删除角色", description = "根据ID删除角色")
    @PreAuthorize("hasAuthority('auth:role:delete')")
    @DeleteMapping("/{id}")
    public ResultData<Void> deleteRole(
            @Parameter(description = "角色ID", required = true) @PathVariable("id") Long id) {
        roleService.removeById(id);
        return ResultData.success();
    }

    @Operation(summary = "获取角色权限ID列表", description = "根据角色ID获取该角色拥有的权限ID列表")
    @PreAuthorize("hasAuthority('auth:role:permission:query')")
    @GetMapping("/{roleId}/permissions")
    public ResultData<List<Long>> getPermissionIdsByRoleId(
            @Parameter(description = "角色ID", required = true) @PathVariable("roleId") Long roleId) {
        return ResultData.success(rolePermissionService.getPermissionIdsByRoleId(roleId));
    }

    @Operation(summary = "分配权限给角色", description = "为指定角色分配权限列表")
    @PreAuthorize("hasAuthority('auth:role:permission:edit')")
    @PutMapping("/{roleId}/permissions")
    public ResultData<Void> assignPermissionsToRole(
            @Parameter(description = "角色ID", required = true) @PathVariable("roleId") Long roleId,
            @Parameter(description = "权限ID列表", required = true) @RequestBody List<String> permissionIds) {
        List<Long> ids = permissionIds.stream()
                .map(Long::parseLong)
                .toList();
        log.info("为角色 {} 分配 {} 个权限", roleId, ids.size());
        rolePermissionService.assignPermissionsToRole(roleId, ids);
        return ResultData.success();
    }
}
