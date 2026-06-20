package com.overthinker.cloud.auth.controller;

import com.overthinker.cloud.auth.entity.DTO.MenuDTO;
import com.overthinker.cloud.auth.entity.PO.SysMenu;
import com.overthinker.cloud.auth.entity.VO.MenuVO;
import com.overthinker.cloud.auth.service.MenuService;
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
 * 菜单管理控制器
 * <p>
 * 提供系统菜单的 CRUD 接口以及前端动态路由所需的树形菜单数据
 * </p>
 *
 * @author overthinker
 * @since 2025-06-13
 */
@Tag(name = "菜单管理", description = "系统菜单数据的增删改查接口")
@RestController
@RequestMapping("/menu")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    @Operation(summary = "获取菜单列表（平铺）", description = "获取系统中所有菜单的平铺列表，用于管理页面展示")
    @PreAuthorize("hasAuthority('auth:menu:list')")
    @GetMapping
    public ResultData<List<SysMenu>> getAllMenus() {
        return ResultData.success(menuService.list());
    }

    @Operation(summary = "获取菜单详情", description = "根据ID获取单个菜单信息")
    @PreAuthorize("hasAuthority('auth:menu:list')")
    @GetMapping("/{id}")
    public ResultData<SysMenu> getMenuById(
            @Parameter(description = "菜单ID", required = true) @PathVariable("id") Long id) {
        return ResultData.success(menuService.getById(id));
    }

    @Operation(summary = "获取菜单树", description = "获取系统所有菜单的树形结构，用于前端动态路由生成")
    @GetMapping("/all")
    public ResultData<List<MenuVO>> getMenuTree() {
        List<MenuVO> menus = menuService.getAllMenus();
        return ResultData.success(menus);
    }

    @Operation(summary = "创建菜单", description = "创建一个新的菜单")
    @PreAuthorize("hasAuthority('auth:menu:add')")
    @PostMapping
    public ResultData<Void> createMenu(@RequestBody @Valid MenuDTO menuDTO) {
        SysMenu menu = menuDTO.copyProperties(SysMenu.class);
        menuService.save(menu);
        return ResultData.success();
    }

    @Operation(summary = "更新菜单", description = "根据ID更新菜单信息")
    @PreAuthorize("hasAuthority('auth:menu:edit')")
    @PutMapping("/{id}")
    public ResultData<Void> updateMenu(
            @Parameter(description = "菜单ID", required = true) @PathVariable("id") Long id,
            @RequestBody @Valid MenuDTO menuDTO) {
        SysMenu menu = menuDTO.copyProperties(SysMenu.class, m -> m.setId(id));
        menuService.updateById(menu);
        return ResultData.success();
    }

    @Operation(summary = "删除菜单", description = "根据ID删除菜单，若存在子菜单则一并删除")
    @PreAuthorize("hasAuthority('auth:menu:delete')")
    @DeleteMapping("/{id}")
    public ResultData<Void> deleteMenu(
            @Parameter(description = "菜单ID", required = true) @PathVariable("id") Long id) {
        menuService.deleteMenu(id);
        return ResultData.success();
    }
}