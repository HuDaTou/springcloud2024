package com.overthinker.cloud.auth.config;

import com.nimbusds.jose.jwk.Curve;
import com.nimbusds.jose.jwk.ECKey;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.OctetSequenceKey;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;

/**
 * JWE (JSON Web Encryption) 密钥配置
 * 用于配置加密 Token 的密钥，支持 RSA、EC 和 AES 三种算法
 */
@Configuration
public class JweKeyConfig {

    /**
     * RSA 密钥对 - 用于 JWE 加密
     */
    private KeyPair rsaKeyPair;

    /**
     * EC 密钥对 - 用于 JWE 加密
     */
    private KeyPair ecKeyPair;

    public JweKeyConfig() {
        this.rsaKeyPair = generateRsaKey();
        this.ecKeyPair = generateEcKey();
    }

    /**
     * RSA 公钥
     */
    @Bean
    public RSAPublicKey rsaPublicKey() {
        return (RSAPublicKey) rsaKeyPair.getPublic();
    }

    /**
     * RSA 私钥
     */
    @Bean
    public RSAPrivateKey rsaPrivateKey() {
        return (RSAPrivateKey) rsaKeyPair.getPrivate();
    }

    /**
     * EC 公钥
     */
    @Bean
    public ECPublicKey ecPublicKey() {
        return (ECPublicKey) ecKeyPair.getPublic();
    }

    /**
     * EC 私钥
     */
    @Bean
    public ECPrivateKey ecPrivateKey() {
        return (ECPrivateKey) ecKeyPair.getPrivate();
    }

    /**
     * JWE 加密密钥源 - 使用 RSA 算法
     * RSA 适合用于非对称加密场景
     */
    @Bean
    public JWKSource<SecurityContext> jweRsaJwkSource() {
        RSAPublicKey publicKey = (RSAPublicKey) rsaKeyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) rsaKeyPair.getPrivate();
        
        RSAKey rsaKey = new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID(UUID.randomUUID().toString())
                .build();
        
        JWKSet jwkSet = new JWKSet(rsaKey);
        return new ImmutableJWKSet<>(jwkSet);
    }

    /**
     * JWE 加密密钥源 - 使用 EC (椭圆曲线) 算法
     * EC 算法更高效，适合移动端和性能敏感场景
     */
    @Bean
    public JWKSource<SecurityContext> jweEcJwkSource() {
        ECPublicKey publicKey = (ECPublicKey) ecKeyPair.getPublic();
        ECPrivateKey privateKey = (ECPrivateKey) ecKeyPair.getPrivate();
        
        ECKey ecKey = new ECKey.Builder(Curve.P_256, publicKey)
                .privateKey(privateKey)
                .keyID(UUID.randomUUID().toString())
                .build();
        
        JWKSet jwkSet = new JWKSet(ecKey);
        return new ImmutableJWKSet<>(jwkSet);
    }

    /**
     * JWE 对称加密密钥源 - 使用 AES 算法
     * 适合同一系统内部的 Token 加密
     */
    @Bean
    public JWKSource<SecurityContext> jweAesJwkSource() {
        // 生成 256 位 AES 密钥
        byte[] secret = new byte[32];
        new java.security.SecureRandom().nextBytes(secret);
        
        OctetSequenceKey octetKey = new OctetSequenceKey.Builder(secret)
                .keyID(UUID.randomUUID().toString())
                .build();
        
        JWKSet jwkSet = new JWKSet(octetKey);
        return new ImmutableJWKSet<>(jwkSet);
    }

    /**
     * 生成 RSA 密钥对 (2048位)
     */
    private static KeyPair generateRsaKey() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            return keyPairGenerator.generateKeyPair();
        } catch (Exception ex) {
            throw new IllegalStateException("RSA 密钥生成失败", ex);
        }
    }

    /**
     * 生成 EC (椭圆曲线) 密钥对
     */
    private static KeyPair generateEcKey() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("EC");
            keyPairGenerator.initialize(Curve.P_256.toECParameterSpec());
            return keyPairGenerator.generateKeyPair();
        } catch (Exception ex) {
            throw new IllegalStateException("EC 密钥生成失败", ex);
        }
    }
}