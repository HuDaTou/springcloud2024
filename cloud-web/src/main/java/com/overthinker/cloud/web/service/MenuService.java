package com.overthinker.cloud.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.overthinker.cloud.common.core.resp.ResultData;
import com.overthinker.cloud.web.entity.DTO.MenuDTO;
import com.overthinker.cloud.web.entity.PO.Menu;
import com.overthinker.cloud.web.entity.VO.MenuByIdVO;
import com.overthinker.cloud.web.entity.VO.MenuVO;

import java.util.List;


/**
 * (Menu)表服务接口
 *
 * @author overH
 * @since 2023-11-17 22:15:22
 */
public interface MenuService extends IService<Menu> {

    /**
     * 获取菜单列表,0:系统菜单，1:菜单管理
     *
     * @return 菜单列表
     */
    ResultData<List<MenuVO>> getMenuList(Integer typeId, String username, Integer status);

    /**
     * 添加菜单
     *
     * @param menuDTO 菜单信息
     * @return 添加菜单的结果
     */
    ResultData<Void> addMenu(MenuDTO menuDTO);

    /**
     * 修改菜单，id回滚
     *
     * @param id 菜单id
     * @return 数据
     */
    ResultData<MenuByIdVO> selectUpdateMenu(Long id);

    /**
     * 修改菜单
     *
     * @param menuDTO 菜单信息
     * @return 是否成功
     */
    ResultData<Void> updateMenu(MenuDTO menuDTO);

    /**
     * 根据id删除菜单
     *
     * @param id 对应的id
     * @return 是否成功
     */
    ResultData<String> deleteMenu(Long id);

}
