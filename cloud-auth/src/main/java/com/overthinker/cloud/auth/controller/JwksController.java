package com.overthinker.cloud.auth.controller;

import com.overthinker.cloud.auth.service.JwkService;
import com.overthinker.cloud.common.core.resp.ResultData;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import lombok.extern.slf4j.Slf4j;

/**
 * JWKS (JSON Web Key Set) 控制器
 * 提供公钥发布接口，供资源服务器验证 JWE/JWT Token
 */
@Slf4j
@RestController
@RequestMapping("/oauth2")
@RequiredArgsConstructor
public class JwksController {

    private final JwkService jwkService;


    /**
     * JWKS 端点 - 发布公钥（标准接口）
     *
     * @return JWK Set JSON
     */
    @GetMapping("/jwks")
    public ResultData<Map<String, Object>> getJwks() {
        try {
            log.debug("获取 JWKS 公钥");
            return ResultData.success(jwkService.getJwkSet().toJSONObject());
        } catch (Exception e) {
            log.error("获取 JWKS 失败", e);
            return ResultData.failure("获取 JWKS 失败");
        }
    }
}
