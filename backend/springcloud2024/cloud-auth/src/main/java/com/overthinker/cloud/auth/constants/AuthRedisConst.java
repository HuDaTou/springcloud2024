package com.overthinker.cloud.auth.constants;

/**
 * Redis认证相关常量
 *
 * @author overH
 */
public class AuthRedisConst {

    /**
     * 邮箱验证码
     */
    public static final String EMAIL_VERIFY_CODE_KEY = "auth:verify:code:";

    /**
     * 邮箱验证码过期时间 (分钟)
     */
    public static final Integer EMAIL_VERIFY_CODE_TTL_MINUTES = 5;

    /**
     * 分隔符
     */
    public static final String SEPARATOR = ":";

    /**
     * 注册
     */
    public static final String REGISTER = "register";

    /**
     * 重置密码
     */
    public static final String RESET = "reset";

    /**
     * 重置邮箱
     */
    public static final String RESET_EMAIL = "resetEmail";

    /**
     * 邮箱确认友链通过，状态码
     */
    public static final String EMAIL_VERIFICATION_LINK = "email:verification:link:";
}
