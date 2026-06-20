package com.overthinker.cloud.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.overthinker.cloud.auth.entity.PO.SysMenu;
import com.overthinker.cloud.auth.entity.VO.MenuVO;

import java.util.List;

/**
 * 菜单服务接口
 *
 * @author overthinker
 * @since 2025-06-13
 */
public interface MenuService extends IService<SysMenu> {

    /**
     * 获取所有菜单（树形结构）
     *
     * @return 树形菜单列表
     */
    List<MenuVO> getAllMenus();

    /**
     * 删除菜单（包含子菜单）
     *
     * @param id 菜单ID
     */
    void deleteMenu(Long id);
}
