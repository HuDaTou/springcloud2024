package com.overthinker.cloud.auth.controller;

import com.overthinker.cloud.auth.service.RolePermissionService;
import com.overthinker.cloud.common.core.resp.ResultData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * (RolePermission)表控制层
 *
 * @author overH
 * @since 2024-09-05 16:13:19
 */
@Tag(name = "角色权限管理", description = "角色权限关联管理接口")
@RestController
@RequestMapping("/roles/{roleId}/permissions")
@RequiredArgsConstructor
public class RolePermissionController {

    private final RolePermissionService rolePermissionService;

    @Operation(summary = "获取角色权限ID列表", description = "根据角色ID获取该角色拥有的权限ID列表")
    @PreAuthorize("hasAuthority('auth:role:permission:query')")
    @GetMapping
    public ResultData<List<Long>> getPermissionIdsByRoleId(
            @Parameter(description = "角色ID", required = true) @PathVariable Long roleId) {
        return ResultData.success(rolePermissionService.getPermissionIdsByRoleId(roleId));
    }

    @Operation(summary = "分配权限给角色", description = "为指定角色分配权限列表")
    @PreAuthorize("hasAuthority('auth:role:permission:edit')")
    @PutMapping
    public ResultData<Void> assignPermissionsToRole(
            @Parameter(description = "角色ID", required = true) @PathVariable Long roleId,
            @Parameter(description = "权限ID列表", required = true) @RequestBody @Valid List<Long> permissionIds) {
        rolePermissionService.assignPermissionsToRole(roleId, permissionIds);
        return ResultData.success();
    }
}
