package com.overthinker.cloud.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 验证码服务
 * 使用 Redis 存储验证码，支持图片验证码、短信验证码等
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CaptchaService {
    
    private final RedisTemplate<String, String> redisTemplate;
    
    private static final String CAPTCHA_PREFIX = "auth:captcha:";
    private static final int CAPTCHA_LENGTH = 4;
    private static final long CAPTCHA_EXPIRE_MINUTES = 5;
    
    /**
     * 生成验证码
     * @return 验证码对象
     */
    public CaptchaResult generate() {
        String captchaKey = UUID.randomUUID().toString().replace("-", "");
        String captchaCode = generateRandomCode(CAPTCHA_LENGTH);
        
        // 存储到 Redis
        String key = CAPTCHA_PREFIX + captchaKey;
        redisTemplate.opsForValue().set(key, captchaCode, CAPTCHA_EXPIRE_MINUTES, TimeUnit.MINUTES);
        
        log.debug("生成验证码成功，key: {}, code: {}", captchaKey, captchaCode);
        
        // 返回验证码结果（实际项目中，captchaImage 应该是生成的图片 Base64）
        return new CaptchaResult(captchaKey, captchaCode);
    }
    
    /**
     * 校验验证码
     * @param captchaKey 验证码 key
     * @param inputCaptcha 用户输入的验证码
     * @return 是否校验通过
     */
    public boolean validate(String captchaKey, String inputCaptcha) {
        if (captchaKey == null || captchaKey.trim().isEmpty()) {
            log.warn("验证码 key 为空");
            return false;
        }
        
        if (inputCaptcha == null || inputCaptcha.trim().isEmpty()) {
            log.warn("用户输入的验证码为空");
            return false;
        }
        
        String key = CAPTCHA_PREFIX + captchaKey;
        String storedCaptcha = redisTemplate.opsForValue().get(key);
        
        if (storedCaptcha == null) {
            log.warn("验证码已过期或不存在，key: {}", captchaKey);
            return false;
        }
        
        boolean isValid = storedCaptcha.equalsIgnoreCase(inputCaptcha.trim());
        
        if (isValid) {
            // 验证成功后删除验证码（防止重复使用）
            redisTemplate.delete(key);
            log.debug("验证码校验成功并已删除，key: {}", captchaKey);
        } else {
            log.warn("验证码校验失败，key: {}, input: {}, stored: {}", 
                    captchaKey, inputCaptcha, storedCaptcha);
        }
        
        return isValid;
    }
    
    /**
     * 生成随机验证码
     */
    private String generateRandomCode(int length) {
        String chars = "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghjkmnpqrstuvwxyz23456789";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        
        return sb.toString();
    }
    
    /**
     * 验证码结果
     */
    public static class CaptchaResult {
        private final String captchaKey;
        private final String captchaCode;
        
        public CaptchaResult(String captchaKey, String captchaCode) {
            this.captchaKey = captchaKey;
            this.captchaCode = captchaCode;
        }
        
        public String getCaptchaKey() {
            return captchaKey;
        }
        
        public String getCaptchaCode() {
            return captchaCode;
        }
    }
}
