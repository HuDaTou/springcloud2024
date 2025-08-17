package com.overthinker.cloud.auth.service;

import com.overthinker.cloud.common.resp.ResultData;
import com.overthinker.cloud.web.entity.DTO.UserRegisterDTO;
import com.overthinker.cloud.web.entity.DTO.UserResetConfirmDTO;
import com.overthinker.cloud.web.entity.DTO.UserResetPasswordDTO;
import org.springframework.security.core.userdetails.UserDetails;

public interface AuthService {

    /**
     * 处理用户注册。
     *
     * @param userRegisterDTO 包含注册信息的DTO。
     * @return ResultData 指示成功或失败。
     */
    ResultData<Void> register(UserRegisterDTO userRegisterDTO);

    /**
     * 确认用于重置密码的电子邮件。
     *
     * @param userResetDTO 包含电子邮件和验证码的DTO。
     * @return ResultData 指示成功或失败。
     */
    ResultData<Void> resetConfirm(UserResetConfirmDTO userResetDTO);

    /**
     * 重置用户密码。
     *
     * @param userResetDTO 包含电子邮件、验证码和新密码的DTO。
     * @return ResultData 指示成功或失败。
     */
    ResultData<Void> resetPassword(UserResetPasswordDTO userResetDTO);

    /**
     * 处理第三方登录或注册。
     *
     * @param type        第三方类型 (例如, "gitee", "github")
     * @param accessToken 来自第三方的访问令牌
     * @return UserDetails 用于进一步处理。
     */
    UserDetails loginOrRegisterThirdParty(String type, String accessToken) throws Exception;

}
