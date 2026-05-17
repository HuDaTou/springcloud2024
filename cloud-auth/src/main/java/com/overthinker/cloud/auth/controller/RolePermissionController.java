package com.overthinker.cloud.auth.controller;

import com.overthinker.cloud.auth.service.RolePermissionService;

import com.overthinker.cloud.common.core.resp.ResultData;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import lombok.RequiredArgsConstructor;

/**
 * (RolePermission)表控制层
 *
 * @author overH
 * @since 2024-09-05 16:13:19
 */
@RestController
@RequestMapping("/roles/{roleId}/permissions")
@RequiredArgsConstructor
public class RolePermissionController {

    private final RolePermissionService rolePermissionService;

    @GetMapping
    public ResultData<List<Long>> getPermissionIdsByRoleId(@PathVariable Long roleId) {
        return ResultData.success(rolePermissionService.getPermissionIdsByRoleId(roleId));
    }

    @PutMapping
    public ResultData<Void> assignPermissionsToRole(@PathVariable Long roleId, @RequestBody List<Long> permissionIds) {
        rolePermissionService.assignPermissionsToRole(roleId, permissionIds);
        return ResultData.success();
    }
}
