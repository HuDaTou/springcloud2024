package com.overthinker.cloud.auth.constants;

/**
 * Redis黑名单常量
 *
 * @author overH
 */
public class BlackListConst {

    /**
     * jwt 黑名单（退出登录的用户jwt加入黑名单）
     */
    public static final String JWT_BLACK_LIST = "auth:blacklist:jwt:";

    /**
     * 缓存黑名单--uid
     */
    public static final String BLACK_LIST_UID_KEY = "auth:blacklist:uid:";

    /**
     * 缓存黑名单--ip
     */
    public static final String BLACK_LIST_IP_KEY = "auth:blacklist:ip:";

    /**
     * 黑名单类型：用户
     */
    public static final Integer BLACK_LIST_TYPE_USER = 1;

    /**
     * 黑名单类型：机器人/爬虫
     */
    public static final Integer BLACK_LIST_TYPE_BOT = 2;
}
