package com.overthinker.cloud.auth.controller;

import com.overthinker.cloud.auth.service.RolePermissionService;
import com.overthinker.cloud.common.resp.ResultData;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles/{roleId}/permissions")
public class RolePermissionController {

    @Resource
    private RolePermissionService rolePermissionService;

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
