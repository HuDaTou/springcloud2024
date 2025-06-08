package com.overthinker.cloud.web.handler.security;


import com.overthinker.cloud.resp.ResultData;
import com.overthinker.cloud.resp.ReturnCodeEnum;
import com.overthinker.cloud.web.entity.PO.LoginUser;
import com.overthinker.cloud.web.entity.VO.AuthorizeVO;
import com.overthinker.cloud.web.entity.constants.Const;
import com.overthinker.cloud.web.entity.constants.RespConst;
import com.overthinker.cloud.web.service.LoginLogService;
import com.overthinker.cloud.web.utils.JwtUtils;
import com.overthinker.cloud.web.utils.MyRedisCache;
import com.overthinker.cloud.web.utils.StringUtils;
import com.overthinker.cloud.web.utils.WebUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * @author overH
 * <p>
 * 创建时间：2023/10/11 16:03
 */
@Component
public class SecurityHandler implements
        AuthenticationSuccessHandler,
        AuthenticationFailureHandler,
        LogoutSuccessHandler,
        AuthenticationEntryPoint,
        AccessDeniedHandler {

    @Resource
    private JwtUtils jwtUtils;

    @Resource
    private MyRedisCache myRedisCache;

    @Resource
    private LoginLogService loginLogService;

    public static final String USER_NAME = "username";


    /**
     * 登录成功处理
     *
     * @param request        请求
     * @param response       响应
     * @param authentication 认证信息
     */
    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) {
        handlerOnAuthenticationSuccess(request, response, (LoginUser) authentication.getPrincipal());
    }

    public void handlerOnAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            LoginUser user
    ) {
        String typeHeader = request.getHeader(Const.TYPE_HEADER);
        if ((!StringUtils.matches(typeHeader, List.of(Const.BACKEND_REQUEST, Const.FRONTEND_REQUEST)))) {
            throw new BadCredentialsException("非法请求");
        }
        Long id = user.getUser().getId();
        String name = user.getUser().getUsername();
        // UUID做jwt的id
        String uuid = UUID.randomUUID().toString();
        // 生成jwt
        String token = jwtUtils.createJwt(uuid, user, id, name);

        // 转换VO
        AuthorizeVO authorizeVO = user.getUser().copyProperties(AuthorizeVO.class, v -> {
            v.setToken(token);
            v.setExpire(jwtUtils.expireTime());
        });
//        userService.userLoginStatus(user.getUser().getId());
        loginLogService.loginLog(request, request.getParameter(USER_NAME), 0, RespConst.SUCCESS_LOGIN_MSG);
        WebUtil.renderString(response, ResultData.success(authorizeVO, RespConst.SUCCESS_LOGIN_MSG).asJsonString());
    }


    /**
     * 登录失败处理
     */
    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception
    ) throws IOException {
        loginLogService.loginLog(request, request.getParameter(USER_NAME), 1, exception.getMessage());
        WebUtil.renderString(response, ResultData.failure(ReturnCodeEnum.USERNAME_OR_PASSWORD_ERROR.getCode(), exception.getMessage()).asJsonString());
    }

    /**
     * 退出登录处理
     */
    @Override
    public void onLogoutSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) {
        boolean invalidateJwt = jwtUtils.invalidateJwt(request.getHeader("Authorization"));
        if (invalidateJwt) {
            WebUtil.renderString(response, ResultData.success().asJsonString());
            return;
        }
        WebUtil.renderString(response, ResultData.failure(ReturnCodeEnum.NOT_LOGIN.getCode(), ReturnCodeEnum.NOT_LOGIN.getMsg()).asJsonString());
    }

    /**
     * 没有登录处理
     */
    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception
    ) throws IOException {
        WebUtil.renderString(response, ResultData.failure(ReturnCodeEnum.NOT_LOGIN.getCode(), ReturnCodeEnum.NOT_LOGIN.getMsg()).asJsonString());
    }

    /**
     * 没有权限处理
     */

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException exception
    ) {
        WebUtil.renderString(response, ResultData.failure(ReturnCodeEnum.NO_PERMISSION.getCode(), ReturnCodeEnum.NO_PERMISSION.getMsg()).asJsonString());
    }
}
