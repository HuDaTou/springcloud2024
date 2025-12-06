package com.overthinker.cloud.auth.controller;

import com.overthinker.cloud.auth.service.SocialLoginService;

import com.overthinker.cloud.common.core.resp.ResultData;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.model.AuthCallback;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/oauth")
public class SocialLoginController {

    @Resource
    private SocialLoginService socialLoginService;

    @GetMapping("/render/{source}")
    public void renderAuth(@PathVariable String source, HttpServletResponse response) throws IOException {
        String url = socialLoginService.renderAuth(source);
        response.sendRedirect(url);
    }

    @RequestMapping("/callback/{source}")
    public ResultData<String> login(@PathVariable String source, AuthCallback callback) {
        return socialLoginService.login(source, callback);
    }
}
