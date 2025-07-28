package com.overthinker.cloud.web.controller;

import com.overthinker.cloud.annotation.LogAnnotation;
import com.overthinker.cloud.controller.base.BaseController;
import com.overthinker.cloud.resp.ResultData;
import com.overthinker.cloud.annotation.AccessLimit;

import com.overthinker.cloud.web.entity.DTO.MenuDTO;
import com.overthinker.cloud.web.entity.VO.MenuByIdVO;
import com.overthinker.cloud.web.entity.VO.MenuVO;
import com.overthinker.cloud.web.entity.VO.RoleVO;
import com.overthinker.cloud.annotation.LogConst;
import com.overthinker.cloud.web.service.MenuService;
import com.overthinker.cloud.web.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * (Menu)表控制层
 *
 * @author overH
 * @since 2023-11-17 22:15:18
 */
@RestController
@Tag(name = "菜单相关接口")
@RequestMapping("menu")
public class MenuController extends BaseController {
    /**
     * 服务对象
     */
    @Resource
    private MenuService menuService;

    @Resource
    private RoleService roleService;

    @PreAuthorize("hasAnyAuthority('system:menu:list')")
    @Operation(summary = "获取管理菜单列表")
    @Parameters({
            @Parameter(name = "typeId", description = "菜单类型id", required = true),
    })
    @AccessLimit(seconds = 60, maxCount = 30)
    @GetMapping("/list/{typeId}")
    public ResultData<List<MenuVO>> list(@PathVariable("typeId") Integer typeId) {
        return menuService.getMenuList(typeId, null, null);
    }

    @PreAuthorize("hasAnyAuthority('system:search:menu:list')")
    @Operation(summary = "搜索管理菜单列表")
    @Parameters({
            @Parameter(name = "typeId", description = "菜单类型id", required = true),
            @Parameter(name = "username", description = "用户名", required = true),
            @Parameter(name = "status", description = "状态", required = true)
    })
    @LogAnnotation(module = "菜单管理", operation = LogConst.SEARCH)
    @AccessLimit(seconds = 60, maxCount = 30)
    @GetMapping("/search/list/{typeId}")
    public ResultData<List<MenuVO>> searchMenu(@PathVariable("typeId") Integer typeId, String username, Integer status) {
        return menuService.getMenuList(typeId, username, status);
    }

    @PreAuthorize("hasAnyAuthority('system:menu:role:list')")
    @Operation(summary = "获取修改菜单角色列表")
    @AccessLimit(seconds = 60, maxCount = 5)
    @GetMapping("/role/list")
    public ResultData<List<RoleVO>> selectAll() {
        return roleService.selectAll();
    }

    @Operation(summary = "获取路由菜单列表")
    @Parameters({
            @Parameter(name = "typeId", description = "菜单类型id", required = true),
    })
    @AccessLimit(seconds = 60, maxCount = 30)
    @GetMapping("/router/list/{typeId}")
    public ResultData<List<MenuVO>> routerList(@PathVariable("typeId") Integer typeId) {
        return menuService.getMenuList(typeId, null, null);
    }

    @PreAuthorize("hasAnyAuthority('system:menu:add')")
    @Operation(summary = "添加菜单")
    @Parameter(name = "menuDTO", description = "菜单信息", required = true)
    @AccessLimit(seconds = 60, maxCount = 30)
    @LogAnnotation(module = "菜单管理", operation = LogConst.INSERT)
    @PostMapping
    public ResultData<Void> add(@RequestBody MenuDTO menuDTO) {
        return menuService.addMenu(menuDTO);
    }

    @PreAuthorize("hasAnyAuthority('system:menu:select')")
    @Operation(summary = "根据id查询菜单信息")
    @Parameter(name = "id", description = "菜单id", required = true)
    @AccessLimit(seconds = 60, maxCount = 30)
    @GetMapping("/{id}")
    public ResultData<MenuByIdVO> getById(@PathVariable("id") @NotNull Long id) {
        return menuService.selectUpdateMenu(id);
    }

    @PreAuthorize("hasAnyAuthority('system:menu:update')")
    @Operation(summary = "修改菜单")
    @Parameter(name = "menuDTO", description = "菜单信息", required = true)
    @AccessLimit(seconds = 60, maxCount = 30)
    @LogAnnotation(module = "菜单管理", operation = LogConst.UPDATE)
    @PutMapping
    public ResultData<Void> update(@RequestBody MenuDTO menuDTO) {
        return menuService.updateMenu(menuDTO);
    }

    @PreAuthorize("hasAnyAuthority('system:menu:delete')")
    @Operation(summary = "根据id删除菜单")
    @Parameter(name = "id", description = "菜单id", required = true)
    @AccessLimit(seconds = 60, maxCount = 30)
    @LogAnnotation(module = "菜单管理", operation = LogConst.DELETE)
    @DeleteMapping("/{id}")
    public ResultData<String> delete(@PathVariable("id") @NotNull Long id) {
        return menuService.deleteMenu(id);
    }

}

