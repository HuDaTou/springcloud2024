package com.overthinker.cloud.auth.service;

import com.overthinker.cloud.common.resp.ResultData;
import me.zhyd.oauth.model.AuthCallback;

public interface SocialLoginService {

    /**
     * 生成授权页面URL。
     *
     * @param source 第三方源 (例如 gitee, github, wechat)。
     * @return 授权页面URL。
     */
    String renderAuth(String source);

    /**
     * 处理第三方服务的回调。
     *
     * @param source   第三方源。
     * @param callback 包含授权码的回调对象。
     * @return 如果登录成功，则返回JWT。
     */
    ResultData<String> login(String source, AuthCallback callback);
}
