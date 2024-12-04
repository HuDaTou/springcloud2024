package com.overthinker.cloud.web.component;


import cn.hutool.core.util.IdUtil;
import com.overthinker.cloud.web.utils.RedisUtils;
import com.overthinker.cloud.web.entity.constants.RedisConstants;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

@Component
public class RedisComponent {
    @Resource
    private RedisUtils redisUtils;

    /**
     * 保存图形验证码的数字
     * @param code
     * @return
     */
    public String saveCaptchaCode(String code) {
        String simpleUUID = IdUtil.simpleUUID();
        redisUtils.setex(RedisConstants.REDIS_KEY_CAPTCHA +simpleUUID,code,RedisConstants.REDIS_KEY_EXPIRES_ONE_MIN*10);
        return simpleUUID;
    }


    /**
     * 返回验证码
     * @param simpleUUID
     * @return
     */
    public String getCaptchaCode(String simpleUUID) {
       return (String) redisUtils.get(RedisConstants.REDIS_KEY_CAPTCHA +simpleUUID);
    }

    /**
     * 清除验证码
     * @param checkCode
     */
    public void clearCheckCode(String checkCode) {
        redisUtils.delete(RedisConstants.REDIS_KEY_CAPTCHA +checkCode);
    }
}
