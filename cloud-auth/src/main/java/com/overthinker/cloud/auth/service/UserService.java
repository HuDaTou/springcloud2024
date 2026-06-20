package com.overthinker.cloud.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;

import com.overthinker.cloud.auth.entity.DTO.*;
import com.overthinker.cloud.auth.entity.PO.User;
import com.overthinker.cloud.auth.entity.VO.UserAccountVO;
import com.overthinker.cloud.auth.entity.VO.UserDetailsVO;
import com.overthinker.cloud.auth.entity.VO.UserListVO;
import com.overthinker.cloud.common.core.resp.ResultData;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;


/**
 * (User)表服务接口
 *
 * @author overH
 * @since 2023-10-10 19:33:43
 */
public interface UserService extends IService<User>, UserDetailsService {
    /**
     * 根据用户名或者邮箱查询用户
     *
     * @param userName 用户名
     * @param email 用户邮箱
     * @return 用户信息
     */
    User findAccountByNameOrEmail(String userName,String email);

    /**
     * 根据用户id查询用户信息
     *
     * @param id 用户id
     * @return 用户信息
     */
    UserAccountVO findAccountById(Long id);

    /**
     * 用户登录状态
     *
     * @param id   用户id
     * @param type 登录类型
     */
    void userLoginStatus(Long id, Integer type);

    /**
     * 用户注册
     *
     * @param userRegisterDTO 参数
     * @return 是否成功
     */
    ResultData<Void> userRegister(UserRegisterDTO userRegisterDTO);

    /**
     * 用户重置密码，步骤一
     *
     * @param userResetDTO 参数
     * @return 是否成功
     */
    ResultData<Void> userResetConfirm(UserResetConfirmDTO userResetDTO);

    /**
     * 重置密码，已经确认邮箱
     *
     * @param userResetDTO 参数
     * @return 是否成功
     */
    ResultData<Void> userResetPassword(UserResetPasswordDTO userResetDTO);

    /**
     * 获取所有的用户
     *
     * @param userSearchDTO 查询条件
     * @return 用户列表
     */
    List<UserListVO> getUserOrSearch(UserSearchDTO userSearchDTO);

    /**
     * 修改用户状态
     *
     * @param id     用户id
     * @param status 状态
     * @return 是否成功
     */
    ResultData<Void> updateStatus(Long id, Integer status);

    /**
     * 查看用户详情
     *
     * @param id 用户id
     * @return 用户信息
     */
    UserDetailsVO findUserDetailsById(Long id);

    /**
     * 删除用户
     *
     * @param ids 用户id
     * @return 是否成功
     */
    ResultData<Void> deleteUser(List<Long> ids);

    /**
     * 修改用户信息
     *
     * @param userUpdateDTO 参数
     * @return 是否成功
     */
    ResultData<Void> updateUser(UserUpdateDTO userUpdateDTO);

    /**
     * 更新用户头像路径
     *
     * @param avatarUrl 头像路径
     * @return 是否成功, 返回头像地址
     */
    ResultData<String> uploadAvatar(String avatarUrl);

    /**
     * 修改邮箱
     *
     * @param updateEmailDTO 参数
     * @return 是否成功
     */
    ResultData<Void> updateEmailAndVerify(UpdateEmailDTO updateEmailDTO);

    /**
     * 第三方登录修改用户邮箱
     *
     * @param updateEmailDTO 参数
     * @return 是否成功
     */
    ResultData<Void> thirdUpdateEmail(UpdateEmailDTO updateEmailDTO);

    List<String> getUserAuthorities(Long userId);

    List<String> getUserRoleNames(Long userId);

    /**
     * 管理员创建用户
     *
     * @param adminCreateUserDTO 创建用户参数
     * @return 是否成功
     */
    ResultData<Void> adminCreateUser(AdminCreateUserDTO adminCreateUserDTO);
    
    /**
     * 更新用户登录信息
     *
     * @param userId 用户ID
     * @param loginIp 登录IP
     * @param userAgent 用户代理
     */
    void updateLoginInfo(Long userId, String loginIp, String userAgent);
}
