package com.overthinker.cloud.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.overthinker.cloud.auth.entity.PO.SysMenu;
import org.apache.ibatis.annotations.Mapper;

/**
 * 系统菜单 Mapper
 *
 * @author overthinker
 * @since 2025-06-13
 */
@Mapper
public interface SysMenuMapper extends BaseMapper<SysMenu> {
}
