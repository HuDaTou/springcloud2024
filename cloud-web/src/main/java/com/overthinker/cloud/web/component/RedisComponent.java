package com.overthinker.cloud.web.component;


import cn.hutool.core.util.IdUtil;
import cn.hutool.crypto.SecureUtil;
import com.overthinker.cloud.web.entity.PO.UserInfo;
import com.overthinker.cloud.web.entity.VO.UserInfoVO;
import com.overthinker.cloud.web.utils.RedisUtils;
import com.overthinker.cloud.web.entity.constants.RedisConstants;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

@Component
public class RedisComponent {
    @Resource
    private RedisUtils redisUtils;


    /**
     * 保存验证码
     * @param code
     * @param type
     * @return
     */
    public String saveTypeCode(String code, String type) {
        String simpleUUID = IdUtil.simpleUUID();
        redisUtils.setex(RedisConstants.REDIS_KEY_CAPTCHA + type + simpleUUID,code,RedisConstants.REDIS_KEY_EXPIRES_ONE_MIN*10);
        return simpleUUID;
    }


    /**
     * 返回验证码
     * @param simpleUUID
     * @return
     */
    public String getRedisCode(String simpleUUID, String type) {
        return (String) redisUtils.get(RedisConstants.REDIS_KEY_CAPTCHA + type + simpleUUID);
    }


    /**
     * 清除验证码
     * @param checkCode
     */
    public void clearCheckCode(String checkCode, String type) {
        redisUtils.delete(RedisConstants.REDIS_KEY_CAPTCHA+type +checkCode);
    }

    /**
     * 保存token
     * @param userInfo
     * @return
     */
    public void saveToken(UserInfo userInfo) {
//        TODO：这里的token生成规则需要修改 可以设置成非堆成加密
        String token = SecureUtil.md5(userInfo.getNickName() + userInfo.getLastLoginTime());
        redisUtils.setex(RedisConstants.REDIS_KEY_TOKEN + userInfo.getLastLoginTime()+userInfo.getUserId(), token, RedisConstants.REDIS_KEY_EXPIRES_ONE_DAY* 7L);
    }
}
