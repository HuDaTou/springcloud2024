package com.overthinker.cloud.web;

import com.overthinker.cloud.web.component.RedisComponent;
import com.overthinker.cloud.web.entity.constants.RedisConstants;
import com.overthinker.cloud.web.utils.RedisUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @Title: com.overthinker.cloud.web.RedisDemoApplicationTests
 * @Author overthinker
 * @Package PACKAGE_NAME
 * @Date 2024/12/5 20:51
 * @description: redis连接测试
 */
@SpringBootTest
public class RedisDemoApplicationTests {


    @Autowired
    private RedisComponent redisComponent;
    @Autowired
    private RedisUtils<String> redisUtils;
    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;


    @Test
    public void redisComponent() {
        String key = "test1";
        String uuid = redisComponent.saveTypeCode(key, RedisConstants.REDIS_KEY_CAPTCHA);
        String keycode = redisComponent.getRedisCode(uuid,RedisConstants.REDIS_KEY_CAPTCHA);
        System.out.printf(keycode);
    }


    @Test
    public void redisutils() {
        redisUtils.set("test2", "test2");
        String code = redisUtils.get("test2");
        System.out.printf(code);
    }

    @Test
    public void redistemplate() {
        redisTemplate.opsForValue().set("test3", "test3");
        redisTemplate.opsForValue().get("test3");
        System.out.println(redisTemplate.opsForValue().get("test3"));

    }
}
