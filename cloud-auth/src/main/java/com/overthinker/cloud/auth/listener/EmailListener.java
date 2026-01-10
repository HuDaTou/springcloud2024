package com.overthinker.cloud.auth.listener;


import com.overthinker.cloud.auth.entity.DTO.MailDTO;
import com.overthinker.cloud.auth.utils.MailUtils;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
* 权限注册监听器
 * 监听RabbitMQ队列，接收各微服务上报的接口权限信息，并持久化到数据库。
 */
@Slf4j
@Component
@RabbitListener(queues = "mail.queue") // 指定监听的队列名
public class EmailListener {

    @Autowired
    private MailUtils mailUtils;


    /**
     * @RabbitHandler 会根据消息的类型自动匹配处理方法
     * 我们这里直接接收 MailDTO 对象（Spring 会自动反序列化）
     */
    @RabbitHandler
    public void handleMailMessage(MailDTO mailDTO, Channel channel, Message message) throws IOException {
        // 1. 获取消息的唯一标识，用于手动确认
        long deliveryTag = message.getMessageProperties().getDeliveryTag();

        log.info("【邮件监听器】收到待发送邮件，收件人：{}", mailDTO.getTo());

        try {
            // 2. 调用你之前写的 MailUtils 发送 HTML 邮件

            mailUtils.sendHtmlMail(mailDTO);

            // 3. 业务处理成功，手动确认消息（通知 MQ 从队列中删除该消息）
            // 参数2为 false 表示不批量确认
            channel.basicAck(deliveryTag, false);
            log.info("【邮件监听器】邮件发送成功，已确认消息");

        } catch (Exception e) {
            log.error("【邮件监听器】处理邮件消息异常：{}", e.getMessage());

            // 4. 出现异常时的处理策略：
            // requeue = true: 重新放回队列头部，稍后重试。
            // requeue = false: 直接丢弃或进入死信队列（建议配合死信队列使用）。
            // 这里我们选择重新放回队列重试一次（注意：如果代码逻辑错误，这会导致无限死循环）
            channel.basicNack(deliveryTag, false, true);
        }
    }
}
