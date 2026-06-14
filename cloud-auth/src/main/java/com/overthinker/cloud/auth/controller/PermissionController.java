package com.overthinker.cloud.auth.controller;

import com.overthinker.cloud.api.apis.auth.mq.PermissionDTO;
import com.overthinker.cloud.auth.entity.PO.SysPermission;
import com.overthinker.cloud.auth.entity.VO.PermissionTreeVO;
import com.overthinker.cloud.auth.service.PermissionService;
import com.overthinker.cloud.common.core.resp.ResultData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * (SysPermission)表控制层
 *
 * @author overH
 * @since 2024-09-05 16:13:19
 */
@Tag(name = "权限管理", description = "系统权限数据的增删改查及注册接口")
@RestController
@RequestMapping("/permissions")
@RequiredArgsConstructor
public class PermissionController {
    private final PermissionService permissionService;

    @Operation(summary = "获取所有权限", description = "获取系统中所有的权限列表")
    @PreAuthorize("hasAuthority('auth:permission:list')")
    @GetMapping
    public ResultData<List<SysPermission>> getAllPermissions() {
        return ResultData.success(permissionService.list());
    }

    @Operation(summary = "创建权限", description = "手动创建一个新的权限")
    @PreAuthorize("hasAuthority('auth:permission:add')")
    @PostMapping
    public ResultData<Void> createPermission(@RequestBody @Valid SysPermission permission) {
        permissionService.save(permission);
        return ResultData.success();
    }

    @Operation(summary = "更新权限", description = "根据ID更新权限信息")
    @PreAuthorize("hasAuthority('auth:permission:edit')")
    @PutMapping("/{id}")
    public ResultData<Void> updatePermission(
            @Parameter(description = "权限ID", required = true) @PathVariable Long id,
            @RequestBody @Valid SysPermission permission) {
        permission.setId(id);
        permissionService.updateById(permission);
        return ResultData.success();
    }

    @Operation(summary = "删除权限", description = "根据ID删除权限")
    @PreAuthorize("hasAuthority('auth:permission:delete')")
    @DeleteMapping("/{id}")
    public ResultData<Void> deletePermission(
            @Parameter(description = "权限ID", required = true) @PathVariable Long id) {
        permissionService.removeById(id);
        return ResultData.success();
    }

    @Operation(summary = "获取权限树", description = "按分类分组返回权限树结构，用于角色分配权限时的树形选择")
    @PreAuthorize("hasAuthority('auth:permission:list')")
    @GetMapping("/tree")
    public ResultData<List<PermissionTreeVO>> getPermissionTree() {
        List<SysPermission> allPermissions = permissionService.list();

        Map<String, List<SysPermission>> grouped = allPermissions.stream()
                .collect(Collectors.groupingBy(p ->
                        p.getCategory() != null ? p.getCategory() : "未分类"));

        List<PermissionTreeVO> tree = grouped.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> {
                    List<PermissionTreeVO> children = entry.getValue().stream()
                            .sorted(Comparator.comparing(SysPermission::getName, Comparator.nullsLast(Comparator.naturalOrder())))
                            .map(p -> PermissionTreeVO.builder()
                                    .label(p.getName())
                                    .value(p.getPermissonCode())
                                    .id(p.getId())
                                    .build())
                            .collect(Collectors.toList());
                    return PermissionTreeVO.builder()
                            .label(entry.getKey())
                            .value(entry.getKey())
                            .children(children)
                            .build();
                })
                .collect(Collectors.toList());

        return ResultData.success(tree);
    }

    @Operation(summary = "注册权限（内部调用）", description = "批量注册其他服务的权限数据")
    @PostMapping("/register")
    public void registerPermissions(@RequestBody List<PermissionDTO> permissions) {
        List<SysPermission> sysPermissions = permissions.stream().map(p -> {
            SysPermission sp = new SysPermission();
            sp.setPermissonCode(p.getPermissonCode());
            sp.setName(p.getName());
            sp.setCategory(p.getCategory());
            return sp;
        }).toList();
        permissionService.saveBatch(sysPermissions);
    }
}
