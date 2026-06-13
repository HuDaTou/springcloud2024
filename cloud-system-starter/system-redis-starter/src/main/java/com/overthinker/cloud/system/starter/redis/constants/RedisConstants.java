package com.overthinker.cloud.system.starter.redis.constants;

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
     * 注册验证码Key前缀
     * Pattern: USER_CODE:REGISTER:EMAIL
     */
    public static final String REGISTER_CODE_KEY_PREFIX = USER_CODE_KEY_PREFIX + "register:";
    /**
     * 重置密码验证码Key前缀
     * Pattern: USER_CODE:RESET_PASSWORD:EMAIL
     */
    public static final String RESET_PASSWORD_CODE_KEY_PREFIX = USER_CODE_KEY_PREFIX + "reset_password:";

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

    /**
     * 验证码过期时间，15分钟（单位：分钟）
     */
    public static final Integer EMAIL_CODE_EXPIRATION_MINUTES = 15;

    /**
     * 注册验证码过期时间，10分钟（单位：分钟）
     */
    public static final Integer REGISTER_CODE_EXPIRATION_MINUTES = 10;

    /**
     * 重置密码验证码过期时间，15分钟（单位：分钟）
     */
    public static final Integer RESET_PASSWORD_CODE_EXPIRATION_MINUTES = 15;

    /**
     * ======================== 业务相关常量（从RedisConst合并）========================
     */
    /**
     * 文章访问量Key前缀
     */
    public static final String ARTICLE_VISIT_COUNT = "article:visit:count:";
    /**
     * 文章点赞数量Key（完整Key）
     */
    public static final String ARTICLE_LIKE_COUNT = "article:like:count";
    /**
     * 文章评论数量Key（完整Key）
     */
    public static final String ARTICLE_COMMENT_COUNT = "article:comment:count";
    /**
     * 文章收藏数量Key（完整Key）
     */
    public static final String ARTICLE_FAVORITE_COUNT = "article:favorite:count";
    /**
     * 用户黑名单Key前缀
     */
    public static final String BLACK_LIST_UID_KEY = "auth:blacklist:uid:";
    /**
     * IP黑名单Key前缀
     */
    public static final String BLACK_LIST_IP_KEY = "auth:blacklist:ip:";
    /**
     * 邮箱验证链接Key前缀
     */
    public static final String EMAIL_VERIFICATION_LINK = "email:verification:link:";
    /**
     * 视频访问量Key前缀
     */
    public static final String VIDEO_VISIT_COUNT = "video:visit:count:";
    /**
     * 验证码Key前缀
     */
    public static final String VERIFY_CODE = "email:verify:code:";
    /**
     * 分隔符
     */
    public static final String SEPARATOR = ":";
    /**
     * 验证码过期时间（分钟）
     */
    public static final Integer VERIFY_CODE_EXPIRATION = 5;

    private RedisConstants() {
        // 私有构造器，防止实例化
    }
}
