package com.overthinker.cloud.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.overthinker.cloud.common.resp.ResultData;
import com.overthinker.cloud.web.entity.DTO.RoleDTO;
import com.overthinker.cloud.web.entity.DTO.RoleSearchDTO;
import com.overthinker.cloud.web.entity.PO.Role;
import com.overthinker.cloud.web.entity.PO.RoleMenu;
import com.overthinker.cloud.web.entity.VO.RoleAllVO;
import com.overthinker.cloud.web.entity.VO.RoleByIdVO;
import com.overthinker.cloud.web.entity.VO.RoleVO;
import com.overthinker.cloud.web.mapper.RoleMapper;
import com.overthinker.cloud.web.mapper.RoleMenuMapper;
import com.overthinker.cloud.web.service.RoleMenuService;
import com.overthinker.cloud.web.service.RoleService;
import com.overthinker.cloud.web.utils.StringUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * (Role)表服务实现类
 *
 * @author overH
 * @since 2023-10-13 15:02:40
 */
@Service("roleService")
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private RoleMenuMapper roleMenuMapper;

    @Resource
    private RoleMenuService roleMenuService;

    @Override
    public ResultData<List<RoleVO>> selectAll() {
        List<Role> roles = roleMapper.selectList(new LambdaQueryWrapper<Role>().eq(Role::getStatus, 0));
        List<RoleVO> vos = roles.stream().map(role -> role.copyProperties(RoleVO.class)).toList();
        if (!vos.isEmpty()) return ResultData.success(vos);
        return ResultData.failure();
    }

    @Override
    public List<RoleAllVO> selectRole(RoleSearchDTO roleSearchDTO) {
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        if (Objects.nonNull(roleSearchDTO)) {
            wrapper.like(Objects.nonNull(roleSearchDTO.getRoleName()), Role::getRoleName, roleSearchDTO.getRoleName())
                    .like(Objects.nonNull(roleSearchDTO.getRoleKey()), Role::getRoleKey, roleSearchDTO.getRoleKey())
                    .eq(Objects.nonNull(roleSearchDTO.getStatus()), Role::getStatus, roleSearchDTO.getStatus());
            if (roleSearchDTO.getCreateTimeStart() != null && roleSearchDTO.getCreateTimeEnd() != null) {
                wrapper.gt(Role::getCreateTime, roleSearchDTO.getCreateTimeStart()).and(a -> a.lt(Role::getCreateTime, roleSearchDTO.getCreateTimeEnd()));
            }
        }
        wrapper.orderByAsc(Role::getOrderNum);
        return roleMapper.selectList(wrapper).stream().map(role -> role.copyProperties(RoleAllVO.class)).toList();
    }

    @Override
    public ResultData<Void> updateStatus(Long id, Integer status) {
        if (roleMapper.updateById(new Role().setId(id).setStatus(status)) > 0) {
            return ResultData.success();
        }
        return ResultData.failure();
    }

    @Override
    public ResultData<RoleByIdVO> selectRoleById(Long id) {
        List<Long> menuIds = roleMenuMapper
                .selectList(new LambdaQueryWrapper<RoleMenu>().eq(RoleMenu::getRoleId, id))
                .stream().map(RoleMenu::getMenuId).toList();
        Role role = roleMapper.selectById(id);
        if (role != null) {
            RoleByIdVO vo = role.copyProperties(RoleByIdVO.class, v -> v.setMenuIds(menuIds));
            return ResultData.success(vo);
        }
        return ResultData.failure();
    }

    @Transactional
    @Override
    public ResultData<Void> updateOrInsertRole(RoleDTO roleDTO) {
        Role role = roleDTO.copyProperties(Role.class);
        // 角色字符是否重复
        Role isRole = roleMapper.selectOne(new LambdaQueryWrapper<Role>().eq(Role::getRoleKey, role.getRoleKey().trim()));
        if (StringUtils.isNotNull(isRole) && !isRole.getId().equals(role.getId())) {
            return ResultData.failure("角色字符不可重复");
        }
        if (this.saveOrUpdate(role)) {
            // 添加与菜单的权限
            roleMenuMapper.delete(new LambdaQueryWrapper<RoleMenu>().eq(RoleMenu::getRoleId, role.getId()));
            List<RoleMenu> roleMenus = roleDTO.getMenuIds().stream().map(menuId -> new RoleMenu(role.getId(), menuId)).toList();
            roleMenuService.saveBatch(roleMenus);
        }
        return ResultData.success();
    }

    @Transactional
    @Override
    public ResultData<Void> deleteRole(List<Long> ids) {
        if (roleMapper.deleteBatchIds(ids) > 0) {
            roleMenuMapper.deleteBatchIds(ids);
            return ResultData.success();
        }
        return ResultData.failure();
    }
}

