package com.overthinker.cloud.web.entity.constants;

public class RedisConstants {

    public static final Integer REDIS_KEY_EXPIRES_ONE_MIN = 6000;


    public static final String REDIS_KEY_PREFIX = "overthinker:cloud:web:cache:";

    public static final String REDIS_KEY_USER_TOKEN = REDIS_KEY_PREFIX + "user:checkcode:";
}
