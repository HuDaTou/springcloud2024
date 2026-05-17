package com.overthinker.cloud.auth.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.overthinker.cloud.auth.config.mq.RabbitEmailConfig;
import com.overthinker.cloud.auth.entity.DTO.MailDTO;
import com.overthinker.cloud.auth.entity.ENUMS.EmailCodeType;
import com.overthinker.cloud.auth.entity.ENUMS.EmailCompolent;
import com.overthinker.cloud.auth.service.EmailService;
import com.overthinker.cloud.common.core.constants.NumberConst;
import com.overthinker.cloud.common.core.utils.MyStringUtils;
import com.overthinker.cloud.system.redis.utils.MyRedisCache;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor

public class EmailServiceImpl implements EmailService {
    private final RabbitTemplate rabbitTemplate;
    private final MyRedisCache myRedisCache;

    @Override
    public void sendEmailCode(String email, String type) {
        // 先检查缓存中是否有该邮箱的验证码
        // 构建缓存key
        EmailCodeType typeByCode = EmailCodeType.getByCode(type);
        String redisKey = MyStringUtils.buildRedisKey(typeByCode.getCodeType(), email);
        String cacheObject = myRedisCache.getCacheObject(redisKey);

        if (MyStringUtils.isNotEmpty(cacheObject)) {
            cacheObject = RandomUtil.randomString(Integer.parseInt(NumberConst.SIX));
            myRedisCache.setCacheObject(redisKey, cacheObject, Integer.parseInt(NumberConst.THOUSAND), TimeUnit.MINUTES);
        }
        // 枚举构建邮件模板参数
        EmailCompolent byType = EmailCompolent.getByType(typeByCode.getCodeType());
        MailDTO send = byType.send(email, cacheObject);
        enqueue(send);
    }

    private void enqueue(MailDTO mailDTO) {
        // 发送至交换机，路由键必须与队列绑定的键一致
        rabbitTemplate.convertAndSend(
                RabbitEmailConfig.MAIL_EXCHANGE,
                RabbitEmailConfig.MAIL_ROUTING_KEY,
                mailDTO);
    }

}
