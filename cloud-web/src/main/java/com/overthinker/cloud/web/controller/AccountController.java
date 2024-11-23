package com.overthinker.cloud.web.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.CircleCaptcha;
import cn.hutool.core.codec.Base64;
import com.overthinker.cloud.resp.ResultData;
import com.overthinker.cloud.web.component.RedisComponent;
import com.overthinker.cloud.web.config.redis.RedisUtils;
import com.overthinker.cloud.web.entity.DTO.UserRegisterDTO;
import com.overthinker.cloud.web.service.UserInfoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/account")
@Tag(name = "登录注册相关接口")
public class AccountController {
    @Resource
    private UserInfoService userInfoService;

    @Resource
    private RedisComponent redisComponent;
    @Operation(summary = "获取验证码")
    @RequestMapping("/checkCode")
    public ResultData checkCode() {
        CircleCaptcha captcha = CaptchaUtil.createCircleCaptcha(200, 100, 4, 20);
        String code = captcha.getCode();
        String checkcodeKey = redisComponent.saveCheckCode(code);
//        将checkCode 转换成base64
        String imageChackCodeBase64 = captcha.getImageBase64();
        Map<String,String> result = new HashMap<>();

        result.put("checkCodeKey",checkcodeKey);
        result.put("imageChackCodeBase64","data:image/png;base64," +imageChackCodeBase64);


        return ResultData.success(result);

    }

    /**
     * 注册
     * @param userRegisterDTO
     * @return
     */
    @RequestMapping("/register")
    @Operation(summary = "注册")
    public ResultData register(@RequestBody @Valid UserRegisterDTO userRegisterDTO) {
//        String myCode = (String) redisUtils.get("checkCode");
//        return ResultData.success(myCode.equalsIgnoreCase(checkCode));
        return null;

    }
}
