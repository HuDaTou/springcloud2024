package com.overthinker.cloud.system.rabbitmq.handler;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.RabbitListenerErrorHandler;
import org.springframework.amqp.rabbit.retry.MessageRecoverer;
import org.springframework.amqp.rabbit.support.ListenerExecutionFailedException;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RabbitMQListenerErrorHandler implements RabbitListenerErrorHandler, MessageRecoverer {
    @Override
    public Object handleError(Message message, Channel channel, org.springframework.messaging.Message<?> message1, ListenerExecutionFailedException e) throws Exception {
        // 处理重试失败的情况，例如记录日志、发送告警等
        log.error("消息处理失败，即将重新发送", e);
        throw e;
    }

    @Override
    public void recover(Message message, Throwable throwable) {
        // 恢复消息，例如将消息发送到死信队列
        log.error("消息重试失败，即将丢弃", throwable);
    }
}
