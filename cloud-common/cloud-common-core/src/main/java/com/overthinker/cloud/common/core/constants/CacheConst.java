package com.overthinker.cloud.common.core.constants;

/**
 * 缓存相关常量类
 * 定义Redis缓存中使用的Key前缀和过期时间等常量
 *
 * @author overthinker
 */
public class CacheConst {

    // ========== 缓存Key前缀 ==========

    /** 用户缓存前缀 */
    public static final String USER_PREFIX = "user:";

    /** 用户Token缓存前缀 */
    public static final String USER_TOKEN_PREFIX = "user:token:";

    /** 用户权限缓存前缀 */
    public static final String USER_PERMISSION_PREFIX = "user:permission:";

    /** 用户角色缓存前缀 */
    public static final String USER_ROLE_PREFIX = "user:role:";

    /** 验证码缓存前缀 */
    public static final String CAPTCHA_PREFIX = "captcha:";

    /** 短信验证码缓存前缀 */
    public static final String SMS_CODE_PREFIX = "sms:code:";

    /** 邮箱验证码缓存前缀 */
    public static final String EMAIL_CODE_PREFIX = "email:code:";

    /** 菜单缓存前缀 */
    public static final String MENU_PREFIX = "menu:";

    /** 角色缓存前缀 */
    public static final String ROLE_PREFIX = "role:";

    /** 字典缓存前缀 */
    public static final String DICT_PREFIX = "dict:";

    /** 配置缓存前缀 */
    public static final String CONFIG_PREFIX = "config:";

    /** 在线用户缓存前缀 */
    public static final String ONLINE_USER_PREFIX = "online:user:";

    /** 接口访问限流前缀 */
    public static final String RATE_LIMIT_PREFIX = "rate:limit:";

    /** 系统公告缓存前缀 */
    public static final String NOTICE_PREFIX = "notice:";

    /** 博客文章缓存前缀 */
    public static final String ARTICLE_PREFIX = "article:";

    /** 博客分类缓存前缀 */
    public static final String CATEGORY_PREFIX = "category:";

    /** 博客标签缓存前缀 */
    public static final String TAG_PREFIX = "tag:";

    /** 博客评论缓存前缀 */
    public static final String COMMENT_PREFIX = "comment:";

    /** 网站配置缓存前缀 */
    public static final String WEBSITE_CONFIG_PREFIX = "website:config:";

    /** 友链缓存前缀 */
    public static final String FRIEND_LINK_PREFIX = "friend:link:";

    /** 音乐缓存前缀 */
    public static final String MUSIC_PREFIX = "music:";

    // ========== 缓存过期时间（秒） ==========

    /** 验证码过期时间：5分钟 */
    public static final long CAPTCHA_EXPIRE_TIME = 300L;

    /** 短信验证码过期时间：10分钟 */
    public static final long SMS_CODE_EXPIRE_TIME = 600L;

    /** 邮箱验证码过期时间：15分钟 */
    public static final long EMAIL_CODE_EXPIRE_TIME = 900L;

    /** Token过期时间：7天 */
    public static final long TOKEN_EXPIRE_TIME = 604800L;

    /** 短缓存过期时间：1小时 */
    public static final long SHORT_EXPIRE_TIME = 3600L;

    /** 中缓存过期时间：6小时 */
    public static final long MEDIUM_EXPIRE_TIME = 21600L;

    /** 长缓存过期时间：12小时 */
    public static final long LONG_EXPIRE_TIME = 43200L;

    /** 一天缓存过期时间 */
    public static final long DAY_EXPIRE_TIME = 86400L;

    /** 一周缓存过期时间 */
    public static final long WEEK_EXPIRE_TIME = 604800L;

    /** 一个月缓存过期时间 */
    public static final long MONTH_EXPIRE_TIME = 2592000L;

    /** 永久缓存（理论上） */
    public static final long PERMANENT_EXPIRE_TIME = -1L;

    private CacheConst() {
    }
}
