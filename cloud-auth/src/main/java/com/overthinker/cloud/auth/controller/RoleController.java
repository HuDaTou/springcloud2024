package com.overthinker.cloud.auth.controller;

import com.overthinker.cloud.auth.entity.PO.SysRole;
import com.overthinker.cloud.auth.service.RoleService;
import lombok.RequiredArgsConstructor;

import com.overthinker.cloud.common.core.resp.ResultData;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService;

    @GetMapping
    public ResultData<List<SysRole>> getAllRoles() {
        return ResultData.success(roleService.list());
    }

    @PostMapping
    public ResultData<Void> createRole(@RequestBody SysRole role) {
        roleService.save(role);
        return ResultData.success();
    }

    @PutMapping("/{id}")
    public ResultData<Void> updateRole(@PathVariable Long id, @RequestBody SysRole role) {
        role.setId(id);
        roleService.updateById(role);
        return ResultData.success();
    }

    @DeleteMapping("/{id}")
    public ResultData<Void> deleteRole(@PathVariable Long id) {
        roleService.removeById(id);
        return ResultData.success();
    }
}
