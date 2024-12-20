package com.overthinker.cloud.web.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.CircleCaptcha;
import com.overthinker.cloud.resp.ResultData;
import com.overthinker.cloud.resp.ReturnCodeEnum;
import com.overthinker.cloud.web.component.RedisComponent;
import com.overthinker.cloud.web.entity.DTO.UserRegisterDTO;
import com.overthinker.cloud.web.entity.enums.ErrorEnum;
import com.overthinker.cloud.web.exception.BusinessException;
import com.overthinker.cloud.web.exception.CustomException;
import com.overthinker.cloud.web.service.UserInfoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
    @RequestMapping("/captchaCode")
    public ResultData<Map> checkCode() {
        CircleCaptcha captcha = CaptchaUtil.createCircleCaptcha(200, 100, 4, 20);
        String code = captcha.getCode();
        String checkcodeKey = redisComponent.saveCaptchaCode(code);
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
        //        1.判断验证码是否正确
        String captchaCode = redisComponent.getCaptchaCode(userRegisterDTO.getSimpleUUID());
        if (!userRegisterDTO.getCode().equals(captchaCode)) {
            // 如果验证码不正确，抛出异常BusinessException
            throw new BusinessException(ReturnCodeEnum.CAPTCHA_CHECK_ERROR,HttpStatusCode.valueOf(400));
        }

        return userInfoService.userRegister(userRegisterDTO);

    }

    @GetMapping("/custom-exception")
    public ResponseEntity<String> goneStatusCode() {
        throw new CustomException("Resource Gone", HttpStatusCode.valueOf(400));
    }

    @GetMapping("/redis")
    public  ResponseEntity<String> getRedis() {
        redisComponent.saveCaptchaCode("123456");
        return  ResponseEntity.ok(redisComponent.getCaptchaCode("123456"));

    }


}
