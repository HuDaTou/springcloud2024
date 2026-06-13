package com.overthinker.cloud.auth.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.overthinker.cloud.auth.config.mq.RabbitEmailConfig;
import com.overthinker.cloud.auth.entity.DTO.MailDTO;
import com.overthinker.cloud.auth.entity.ENUMS.EmailEnum;
import com.overthinker.cloud.auth.entity.ENUMS.EmailNotificationEnum;
import com.overthinker.cloud.auth.service.EmailService;
import com.overthinker.cloud.common.core.constants.NumberConst;
import com.overthinker.cloud.common.core.utils.MyStringUtils;
import com.overthinker.cloud.system.starter.redis.utils.MyRedisCache;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final RabbitTemplate rabbitTemplate;
    private final MyRedisCache myRedisCache;

    @Value("${local-code-verify:false}")
    private boolean localCodeVerify;

    @Override
    public void sendEmailCode(String email, String type) {
        EmailEnum emailEnum = EmailEnum.getByType(type);

        String redisKey = MyStringUtils.buildRedisKey(emailEnum.getCodeType(), email);
        String cacheObject = myRedisCache.getCacheObject(redisKey);

        if (MyStringUtils.isEmpty(cacheObject)) {
            if (localCodeVerify) {
                cacheObject = "123456";
                log.info("本地验证码模式：验证码为 123456，邮箱: {}", email);
            } else {
                cacheObject = RandomUtil.randomString(Integer.parseInt(NumberConst.SIX));
            }
            myRedisCache.setCacheObject(redisKey, cacheObject, Integer.parseInt(NumberConst.THOUSAND), TimeUnit.MINUTES);
        }

        if (localCodeVerify) {
            log.info("本地验证码模式：跳过真实邮件发送，验证码已存入 Redis");
            return;
        }

        MailDTO send = emailEnum.send(email, cacheObject);
        enqueue(send);
    }

    @Override
    public void sendEmailNotification(String email, String type, Map<String, Object> content) {
        EmailNotificationEnum notificationEnum = EmailNotificationEnum.getByType(type);
        MailDTO mailDTO = notificationEnum.toMailDTO(email, content);
        enqueue(mailDTO);
        log.info("邮件通知消息已入队，类型：{}，收件人：{}", type, email);
    }

    private void enqueue(MailDTO mailDTO) {
        rabbitTemplate.convertAndSend(
                RabbitEmailConfig.MAIL_EXCHANGE,
                RabbitEmailConfig.MAIL_ROUTING_KEY,
                mailDTO);
    }

}
