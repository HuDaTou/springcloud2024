package com.overthinker.cloud.system.redis.constant;

/**
 * Redis常用常量类
 * 定义了Redis中使用的各种Key前缀和过期时间，方便统一管理和维护。
 */
public class RedisConstants {

    /**
     * ======================== 用户相关 ========================
     */
    /**
     * 用户Token的Key前缀
     * Pattern: USER_TOKEN:USER_ID
     */
    public static final String USER_TOKEN_KEY_PREFIX = "user:token:";
    /**
     * 用户登录信息Key前缀
     * Pattern: USER_LOGIN:USER_ID
     */
    public static final String USER_LOGIN_KEY_PREFIX = "user:login:";
    /**
     * 用户验证码Key前缀
     * Pattern: USER_CODE:EMAIL/PHONE
     */
    public static final String USER_CODE_KEY_PREFIX = "user:code:";

    /**
     * ======================== 业务相关 ========================
     */
    /**
     * 博客文章缓存Key前缀
     * Pattern: BLOG_ARTICLE:ID
     */
    public static final String BLOG_ARTICLE_KEY_PREFIX = "blog:article:";

    /**
     * 权限信息缓存Key前缀
     * Pattern: PERMISSION:SERVICE_NAME
     */
    public static final String PERMISSION_KEY_PREFIX = "permission:";

    /**
     * ======================== 通用过期时间 ========================
     */
    /**
     * 默认过期时间，24小时（单位：秒）
     */
    public static final long DEFAULT_EXPIRATION_SECONDS = 60 * 60 * 24;

    /**
     * 短暂过期时间，5分钟（单位：秒）
     */
    public static final long SHORT_EXPIRATION_SECONDS = 60 * 5;

    /**
     * 验证码过期时间，5分钟（单位：秒）
     */
    public static final long CODE_EXPIRATION_SECONDS = 60 * 5;

    /**
     * Token过期时间，30分钟（单位：秒）
     */
    public static final long TOKEN_EXPIRATION_SECONDS = 60 * 30;

    private RedisConstants() {
        // 私有构造器，防止实例化
    }
}
