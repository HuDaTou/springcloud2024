package com.overthinker.cloud.auth.controller;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.overthinker.cloud.common.core.resp.ResultData;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.interfaces.RSAPublicKey;
import java.util.Map;

/**
 * JWKS (JSON Web Key Set) 控制器
 * 提供公钥发布接口，供资源服务器验证 JWE/JWT Token
 */
@Slf4j
@RestController
@RequestMapping("/oauth2")
public class JwksController {

    private final JWKSet jwkSet;

    public JwksController(RSAPublicKey rsaPublicKey) {
        this.jwkSet = createJwkSet(rsaPublicKey);
    }

    /**
     * JWKS 端点 - 发布公钥（标准接口）
     * 
     * @return JWK Set JSON
     */
    @GetMapping("/jwks")
    public ResultData<Map<String, Object>> getJwks() {
        log.debug("获取 JWKS 公钥");
        return ResultData.success(jwkSet.toJSONObject());
    }

    /**
     * 创建 JWK Set
     */
    private JWKSet createJwkSet(RSAPublicKey rsaPublicKey) {
        RSAKey rsaKey = new RSAKey.Builder(rsaPublicKey)
                .keyID("cloud-auth-jwe-key")
                .build();
        return new JWKSet(rsaKey);
    }
}