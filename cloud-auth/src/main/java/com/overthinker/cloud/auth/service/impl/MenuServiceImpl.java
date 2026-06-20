package com.overthinker.cloud.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.overthinker.cloud.auth.entity.PO.SysMenu;
import com.overthinker.cloud.auth.entity.VO.MenuVO;
import com.overthinker.cloud.auth.mapper.SysMenuMapper;
import com.overthinker.cloud.auth.service.MenuService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 菜单服务实现类
 * <p>
 * 负责菜单的查询和树形结构构建
 * </p>
 *
 * @author overthinker
 * @since 2025-06-13
 */
@Service
public class MenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements MenuService {

    public void deleteMenu(Long id) {
        deleteMenuRecursively(id);
    }

    private void deleteMenuRecursively(Long parentId) {
        List<SysMenu> children = this.list(new LambdaQueryWrapper<SysMenu>()
                .eq(SysMenu::getParentId, parentId));
        
        for (SysMenu child : children) {
            deleteMenuRecursively(child.getId());
        }
        
        this.removeById(parentId);
    }

    @Override
    public List<MenuVO> getAllMenus() {
        // 查询所有未禁用的菜单
        List<SysMenu> allMenus = this.list(new LambdaQueryWrapper<SysMenu>()
                .eq(SysMenu::getIsDisable, false)
                .orderByAsc(SysMenu::getOrderNum));

        if (allMenus.isEmpty()) {
            return Collections.emptyList();
        }

        // 按 parentId 分组（null 视为 0）
        Map<Long, List<SysMenu>> childrenMap = allMenus.stream()
                .collect(Collectors.groupingBy(m ->
                        m.getParentId() != null ? m.getParentId() : 0L));

        // 获取顶级菜单（parentId 为 null 或 0）
        List<SysMenu> rootMenus = childrenMap.getOrDefault(0L, Collections.emptyList());

        // 递归构建树形 VO
        return rootMenus.stream()
                .map(root -> buildMenuTree(root, childrenMap))
                .collect(Collectors.toList());
    }

    /**
     * 递归构建菜单树
     */
    private MenuVO buildMenuTree(SysMenu menu, Map<Long, List<SysMenu>> childrenMap) {
        List<SysMenu> children = childrenMap.getOrDefault(menu.getId(), Collections.emptyList());

        List<MenuVO> childVOs = children.stream()
                .map(child -> buildMenuTree(child, childrenMap))
                .collect(Collectors.toList());

        MenuVO vo = convertToVO(menu);
        if (!childVOs.isEmpty()) {
            vo.setChildren(childVOs);
        }
        return vo;
    }

    /**
     * 将 SysMenu PO 转换为 MenuVO
     */
    private MenuVO convertToVO(SysMenu menu) {
        MenuVO.MenuMetaVO meta = MenuVO.MenuMetaVO.builder()
                .title(menu.getTitle())
                .icon(menu.getIcon())
                .order(menu.getOrderNum())
                .hideInMenu(menu.getHideInMenu() != null && menu.getHideInMenu() == 1)
                .keepAlive(menu.getKeepAlive() != null && menu.getKeepAlive() == 1)
                .affixTab(menu.getAffix() != null && menu.getAffix() == 1)
                .build();

        return MenuVO.builder()
                .name(menu.getName())
                .path(menu.getPath())
                .component(menu.getComponent())
                .meta(meta)
                .build();
    }
}
