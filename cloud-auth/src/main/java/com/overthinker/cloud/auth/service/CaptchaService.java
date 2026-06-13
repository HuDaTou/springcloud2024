package com.overthinker.cloud.auth.service;

import com.overthinker.cloud.common.core.utils.MyStringUtils;
import com.overthinker.cloud.system.starter.redis.utils.MyRedisCache;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 验证码服务
 * <p>
 * 使用 Redis 存储验证码，支持图片验证码、短信验证码等多种验证码类型。
 * 验证码默认有效期为 5 分钟，验证成功后立即删除（防止重复使用）。
 * </p>
 * <p>
 * 【Redis 使用规范】
 * - 统一使用 {@link MyRedisCache} 工具类操作 Redis
 * - 不直接注入 {@link org.springframework.data.redis.core.RedisTemplate}
 * - key 命名规范：{模块名}:{业务名}:{唯一标识}
 * </p>
 *
 * @author overH
 * @since 2024-09-05
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CaptchaService {

    /**
     * Redis 缓存工具类（项目统一使用）
     */
    private final MyRedisCache myRedisCache;

    /**
     * 验证码 Redis Key 前缀
     * <p>
     * 命名规范：{模块名}:{业务名}:
     * 示例：auth:captcha: 存储 auth 模块的验证码
     * </p>
     */
    private static final String CAPTCHA_PREFIX = "auth:captcha:";

    /**
     * 验证码长度
     */
    private static final int CAPTCHA_LENGTH = 4;

    /**
     * 验证码过期时间（分钟）
     */
    private static final long CAPTCHA_EXPIRE_MINUTES = 5;

    /**
     * 生成验证码
     * <p>
     * 生成随机验证码并存储到 Redis，返回验证码 key 和 code。
     * 验证码默认有效期为 5 分钟。
     * </p>
     *
     * @return 验证码结果对象，包含 key 和 code
     */
    public CaptchaResult generate() {
        // 1. 生成验证码 key（32 位 UUID）
        String captchaKey = UUID.randomUUID().toString().replace("-", "");

        // 2. 生成随机验证码（4 位，不含易混淆字符）
        String captchaCode = generateRandomCode(CAPTCHA_LENGTH);

        // 3. 存储到 Redis（使用统一工具类）
        String key = CAPTCHA_PREFIX + captchaKey;
        myRedisCache.setCacheObject(key, captchaCode, (int) CAPTCHA_EXPIRE_MINUTES, TimeUnit.MINUTES);

        log.debug("生成验证码成功，key: {}, code: {}", captchaKey, captchaCode);

        // 4. 返回验证码结果
        // 注意：实际项目中，captchaImage 应该是生成的图片 Base64
        return new CaptchaResult()
                .setCaptchaKey(captchaKey)
                .setCaptchaCode(captchaCode);
    }

    /**
     * 校验验证码
     * <p>
     * 校验用户输入的验证码是否正确。
     * 校验成功后立即删除验证码（防止重复使用）。
     * </p>
     *
     * @param captchaKey  验证码 key
     * @param inputCaptcha 用户输入的验证码
     * @return 是否校验通过
     */
    public boolean validate(String captchaKey, String inputCaptcha) {
        // 1. 空值检查
        if (MyStringUtils.isBlank(captchaKey)) {
            log.warn("验证码 key 为空");
            return false;
        }
        if (MyStringUtils.isBlank(inputCaptcha)) {
            log.warn("用户输入的验证码为空");
            return false;
        }

        // 2. 从 Redis 获取验证码（使用统一工具类）
        String key = CAPTCHA_PREFIX + captchaKey;
        String storedCaptcha = myRedisCache.getCacheObject(key);

        // 3. 检查验证码是否存在（可能已过期或不存在）
        if (storedCaptcha == null) {
            log.warn("验证码已过期或不存在，key: {}", captchaKey);
            return false;
        }

        // 4. 验证码比对（忽略大小写）
        boolean isValid = storedCaptcha.equalsIgnoreCase(inputCaptcha.trim());

        // 5. 验证成功后删除验证码（防止重复使用）
        if (isValid) {
            myRedisCache.deleteObject(key);
            log.debug("验证码校验成功并已删除，key: {}", captchaKey);
        } else {
            log.warn("验证码校验失败，key: {}, input: {}, stored: {}",
                    captchaKey, inputCaptcha, storedCaptcha);
        }

        return isValid;
    }

    /**
     * 生成随机验证码
     * <p>
     * 字符集不包含易混淆字符：
     * - 大写：去除 I、O
     * - 小写：去除 i、l、o
     * - 数字：去除 0、1
     * </p>
     *
     * @param length 验证码长度
     * @return 随机验证码字符串
     */
    private String generateRandomCode(int length) {
        // 字符集（不含易混淆字符）
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
     * <p>
     * 包含验证码 key 和 code。
     * key 用于从 Redis 中查找验证码。
     * </p>
     */
    @Data
    @Accessors(chain = true)
    public static class CaptchaResult {

        /**
         * 验证码 key
         */
        private String captchaKey;

        /**
         * 验证码 code
         */
        private String captchaCode;
    }
}
