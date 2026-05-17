package com.overthinker.cloud.auth.service;

import com.nimbusds.jose.jwk.JWKSet;

/**
 * JWK Service 接口
 * 负责从 JWKSource 获取公钥，并提供缓存机制
 */
public interface JwkService {

    /**
     * 获取 JWK Set（带缓存）
     *
     * @return JWK Set
     */
    JWKSet getJwkSet();

    /**
     * 清除缓存（当密钥更新时调用）
     */
    void clearCache();
}
