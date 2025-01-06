package com.overthinker.cloud.web.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.overthinker.cloud.web.entity.entity.RoleMenu;
import com.overthinker.cloud.web.mapper.RoleMenuMapper;
import com.overthinker.cloud.web.service.RoleMenuService;

/**
 * (RoleMenu)表服务实现类
 *
 * @author kuailemao
 * @since 2023-11-28 10:23:17
 */
@Service("roleMenuService")
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu> implements RoleMenuService {

}
