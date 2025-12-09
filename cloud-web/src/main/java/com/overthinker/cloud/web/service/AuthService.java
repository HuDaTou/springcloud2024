package com.overthinker.cloud.web.service;

import com.overthinker.cloud.common.core.resp.ResultData;
import org.springframework.security.core.userdetails.UserDetails;


public interface AuthService {

    /**
     * Handle user registration.
     *
     * @param userRegisterDTO DTO containing registration info.
     * @return ResultData indicating success or failure.
     */
    ResultData<Void> register(UserRegisterDTO userRegisterDTO);

    /**
     * Confirm the email for password reset.
     *
     * @param userResetDTO DTO containing email and verification code.
     * @return ResultData indicating success or failure.
     */
    ResultData<Void> resetConfirm(UserResetConfirmDTO userResetDTO);

    /**
     * Reset user password.
     *
     * @param userResetDTO DTO containing email, code, and new password.
     * @return ResultData indicating success or failure.
     */
    ResultData<Void> resetPassword(UserResetPasswordDTO userResetDTO);

    /**
     * Handle third-party login or registration.
     *
     * @param type        the type of third party (e.g., "gitee", "github")
     * @param accessToken the access token from the third party
     * @return UserDetails for further processing.
     */
    UserDetails loginOrRegisterThirdParty(String type, String accessToken) throws Exception;

}
