package com.overthinker.cloud.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.overthinker.cloud.common.resp.ResultData;
import com.overthinker.cloud.auth.entity.DTO.*;
import com.overthinker.cloud.auth.entity.PO.User;
import com.overthinker.cloud.auth.entity.VO.UserAccountVO;
import com.overthinker.cloud.auth.entity.VO.UserDetailsVO;
import com.overthinker.cloud.auth.entity.VO.UserListVO;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


/**
 * (User)表服务接口
 *
 * @author overH
 * @since 2023-10-10 19:33:43
 */
public interface UserService extends IService<User>, UserDetailsService {
    /**
     * 根据用户名或者密码查询用户
     *
     * @param text 用户名或者邮箱
     * @return 用户信息
     */
    User findAccountByNameOrEmail(String text);

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
     * 上传用户头像
     *
     * @param avatarFile 头像
     * @return 是否成功, 返回头像地址
     */
    ResultData<String> uploadAvatar(MultipartFile avatarFile) throws Exception;

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
}
