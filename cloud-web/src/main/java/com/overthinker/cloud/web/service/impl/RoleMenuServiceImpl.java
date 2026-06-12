package com.overthinker.cloud.web.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.overthinker.cloud.web.entity.PO.RoleMenu;
import com.overthinker.cloud.web.mapper.RoleMenuMapper;
import com.overthinker.cloud.web.service.RoleMenuService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * (RoleMenu)表服务实现类
 *
 * @author overH
 * @since 2023-11-28 10:23:17
 */
@Slf4j
@Service("roleMenuService")
@RequiredArgsConstructor
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu> implements RoleMenuService {

}
