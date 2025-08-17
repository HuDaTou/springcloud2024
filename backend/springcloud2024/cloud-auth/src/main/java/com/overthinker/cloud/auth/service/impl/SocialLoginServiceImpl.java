package com.overthinker.cloud.auth.service.impl;

import com.overthinker.cloud.auth.config.JustAuthAutoConfiguration;
import com.overthinker.cloud.auth.entity.PO.User;
import com.overthinker.cloud.auth.service.SocialLoginService;
import com.overthinker.cloud.auth.service.UserService;
import com.overthinker.cloud.auth.utils.JwtUtils;
import com.overthinker.cloud.common.resp.ResultData;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.AuthRequest;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
public class SocialLoginServiceImpl implements SocialLoginService {

    @Resource
    private JustAuthAutoConfiguration justAuthAutoConfiguration;

    @Resource
    private UserService userService;

    @Resource
    private JwtUtils jwtUtils;

    @Override
    public String renderAuth(String source) {
        AuthRequest authRequest = justAuthAutoConfiguration.getAuthRequest(source);
        return authRequest.authorize(source);
    }

    @Override
    public ResultData<String> login(String source, AuthCallback callback) {
        AuthRequest authRequest = justAuthAutoConfiguration.getAuthRequest(source);
        AuthResponse<AuthUser> response = authRequest.login(callback);
        if (response.ok()) {
            AuthUser authUser = response.getData();
            User user = userService.findAccountByNameOrEmail(authUser.getUsername());
            if (Objects.isNull(user)) {
                // 创建一个新用户
                user = new User();
                user.setUsername(authUser.getUsername());
                user.setNickname(authUser.getNickname());
                user.setAvatar(authUser.getAvatar());
                user.setRegisterType(getRegisterType(source));
                userService.save(user);
            }
            // 生成JWT
            String jwt = jwtUtils.createJwt(user, user.getId(), user.getUsername());
            return ResultData.success(jwt);
        } else {
            return ResultData.fail(response.getMsg());
        }
    }

    private Integer getRegisterType(String source) {
        switch (source.toLowerCase()) {
            case "gitee":
                return 1;
            case "github":
                return 2;
            case "wechat":
                return 3;
            default:
                return 0;
        }
    }
}
