package com.overthinker.cloud.auth.service;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSADecrypter;
import com.nimbusds.jose.crypto.RSAEncrypter;
import com.nimbusds.jwt.EncryptedJWT;
import com.nimbusds.jwt.JWTClaimsSet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

/**
 * JWE Token 服务
 * 提供 JWE Token 的加密和解密功能
 */
@Slf4j
@Service
public class JweTokenService {

    private final RSAPublicKey publicKey;
    private final RSAPrivateKey privateKey;

    public JweTokenService(RSAPublicKey publicKey, RSAPrivateKey privateKey) {
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }

    /**
     * 加密 JWT 为 JWE Token
     *
     * @param claims 声明集合
     * @param expiresIn 过期时间（秒）
     * @return JWE Token 字符串
     */
    public String encrypt(JWTClaimsSet claims, long expiresIn) {
        try {
            Instant now = Instant.now();
            
            claims.setExpirationTime(Date.from(now.plusSeconds(expiresIn)));
            claims.setIssueTime(Date.from(now));

            JWEHeader header = new JWEHeader.Builder(JWEAlgorithm.RSA_OAEP_256, EncryptionMethod.A256GCM)
                    .contentType("application/json")
                    .build();

            EncryptedJWT jwt = new EncryptedJWT(header, claims);

            JWEEncrypter encrypter = new RSAEncrypter(publicKey);
            jwt.encrypt(encrypter);

            return jwt.serialize();
        } catch (Exception e) {
            log.error("JWE 加密失败", e);
            throw new RuntimeException("JWE 加密失败", e);
        }
    }

    /**
     * 解密 JWE Token
     *
     * @param token JWE Token 字符串
     * @return 解密后的声明集合
     */
    public JWTClaimsSet decrypt(String token) {
        try {
            EncryptedJWT jwt = EncryptedJWT.parse(token);

            JWEDecrypter decrypter = new RSADecrypter(privateKey);
            jwt.decrypt(decrypter);

            return jwt.getJWTClaimsSet();
        } catch (Exception e) {
            log.error("JWE 解密失败", e);
            throw new RuntimeException("JWE 解密失败", e);
        }
    }

    /**
     * 创建 JWT 声明集合
     *
     * @param subject 主题
     * @param issuer 发行者
     * @param customClaims 自定义声明
     * @return JWT 声明集合
     */
    public JWTClaimsSet createClaims(String subject, String issuer, Map<String, Object> customClaims) {
        JWTClaimsSet.Builder builder = new JWTClaimsSet.Builder()
                .subject(subject)
                .issuer(issuer)
                .issueTime(Date.from(Instant.now()));

        if (customClaims != null) {
            customClaims.forEach(builder::claim);
        }

        return builder.build();
    }

    /**
     * 验证 Token 是否过期
     *
     * @param claims 声明集合
     * @return 是否过期
     */
    public boolean isExpired(JWTClaimsSet claims) {
        Date expiration = claims.getExpirationTime();
        return expiration != null && expiration.before(Date.from(Instant.now()));
    }

    /**
     * 验证 Token 发行者
     *
     * @param claims 声明集合
     * @param expectedIssuer 期望的发行者
     * @return 是否匹配
     */
    public boolean validateIssuer(JWTClaimsSet claims, String expectedIssuer) {
        String issuer = claims.getIssuer();
        return expectedIssuer != null && expectedIssuer.equals(issuer);
    }
}