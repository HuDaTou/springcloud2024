package com.overthinker.cloud.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.overthinker.cloud.resp.ResultData;
import com.overthinker.cloud.web.entity.DTO.RoleUserDTO;
import com.overthinker.cloud.web.entity.DTO.UserRoleDTO;
import com.overthinker.cloud.web.entity.PO.UserRole;
import com.overthinker.cloud.web.entity.VO.RoleAllVO;
import com.overthinker.cloud.web.entity.VO.RoleUserVO;

import java.util.List;


/**
 * (UserRole)表服务接口
 *
 * @author overH
 * @since 2023-11-17 16:34:06
 */
public interface UserRoleService extends IService<UserRole> {

    /**
     * @param roleId   角色id
     * @param username 用户名
     * @param email    邮箱
     * @param type     角色类型,0：该角色的使用用户 1：该角色的未使用用户
     * @return 角色用户列表
     */
    List<RoleUserVO> selectRoleUser(Long roleId, String username, String email, Integer type);

    /**
     * 给角色授权用户
     *
     * @param userRoleDTO 用户角色对象
     * @return 是否成功
     */
    ResultData<Void> addUserRole(UserRoleDTO userRoleDTO);

    /**
     * 取消授权
     *
     * @param userRoleDTO 用户角色对象
     * @return 是否成功
     */
    ResultData<Void> deleteUserRole(UserRoleDTO userRoleDTO);

    /**
     * 所有使用该用户的角色
     *
     * @param userId   用户id
     * @param roleName 角色名称
     * @param roleKey  角色字符
     * @param type     角色类型,0：该角色的使用用户 1：该角色的未使用用户
     * @return 角色列表
     */
    List<RoleAllVO> selectRoleByUserId(Long userId, String roleName, String roleKey, Integer type);

    /**
     * 给多个角色添加某个用户
     *
     * @param roleUserDTO 角色用户数据
     * @return 是否成功
     */
    ResultData<Void> addRoleUser(RoleUserDTO roleUserDTO);

    /**
     * 批量或单个取消授权
     *
     * @param roleUserDTO 角色用户数据
     * @return 是否成功
     */
    ResultData<Void> deleteRoleUser(RoleUserDTO roleUserDTO);
}
