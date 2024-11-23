package com.overthinker.cloud.web.component;


import com.overthinker.cloud.web.config.redis.RedisConfig;
import com.overthinker.cloud.web.config.redis.RedisUtils;
import com.overthinker.cloud.web.entity.constants.RedisConstants;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RedisComponent {
    @Resource
    private RedisUtils redisUtils;

    public String saveCheckCode(String code) {
        String checkCodeKey = UUID.randomUUID().toString();
        redisUtils.setex(RedisConstants.REDIS_KEY_USER_TOKEN+checkCodeKey,code,RedisConstants.REDIS_KEY_EXPIRES_ONE_MIN*10);
        return checkCodeKey;
    }
}
