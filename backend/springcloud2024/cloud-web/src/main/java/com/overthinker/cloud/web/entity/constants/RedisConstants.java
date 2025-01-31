package com.overthinker.cloud.web.entity.constants;

public class RedisConstants {

    /**
     * 一分钟的过期时间
     */
    public static final Integer REDIS_KEY_EXPIRES_ONE_MIN = 6000;

//    一天过期时间
    /**
     * 一天的过期时间
     */
    public static final Integer REDIS_KEY_EXPIRES_ONE_DAY = REDIS_KEY_EXPIRES_ONE_MIN * 60 * 24;

//    一小时过期时间

    /**
     * 一小时的过期时间
     */
    public static final Integer REDIS_KEY_EXPIRES_ONE_HOUR = REDIS_KEY_EXPIRES_ONE_MIN * 60;
    /**
     * 总分类
     */
    public static final String REDIS_PREFIX = "com:overthinker:cloud:";

//    邮箱校验验证码
    /**
     * 邮箱校验验证码
     */
    public static final String REDIS_KEY_USER_EMAILCODE = "user:emailcode:";


    /**
     * 图形验证码
     */
    public static final String REDIS_KEY_CAPTCHA = REDIS_PREFIX+ "captcha:";

    /**
     * token
     */
    public static final String REDIS_KEY_TOKEN = REDIS_PREFIX+ "token:";





}
