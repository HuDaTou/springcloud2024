package com.overthinker.cloud.auth.service.impl;

import com.nimbusds.jose.jwk.JWKSet;
import com.overthinker.cloud.auth.service.JwkService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

/**
 * JWK Service 实现类
 * 负责获取 JWK Set 公钥，并提供缓存机制
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class JwkServiceImpl implements JwkService {
    
    private final JWKSet jwkSet;

    private volatile JWKSet cachedJwkSet = null;

    /**
     * 获取 JWK Set（带缓存）
     *
     * @return JWK Set
     */
    @Override
    public JWKSet getJwkSet() {
        if (cachedJwkSet == null) {
            synchronized (this) {
                if (cachedJwkSet == null) {
                    cachedJwkSet = loadJwkSet();
                }
            }
        }
        return cachedJwkSet;
    }

    /**
     * 从 Bean 中加载 JWK Set
     *
     * @return JWK Set
     */
    private JWKSet loadJwkSet() {
        try {
            log.debug("成功加载 JWK Set");
            return jwkSet;
        } catch (Exception e) {
            log.error("加载 JWK Set 失败", e);
            throw new RuntimeException("加载 JWK Set 失败", e);
        }
    }

    /**
     * 清除缓存（当密钥更新时调用）
     */
    @Override
    public void clearCache() {
        this.cachedJwkSet = null;
        log.info("JWK Set 缓存已清除");
    }
}
