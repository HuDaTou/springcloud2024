package com.overthinker.cloud.system.starter.rabbitmq.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

/**
 * RabbitMQ 工具类
 * <p>
 * 封装 RabbitTemplate 的常用操作，简化消息发送
 * </p>
 *
 * @author overthinker
 * @since 2024-06-13
 */
@Slf4j
@RequiredArgsConstructor
public class MyRabbitMQ {

    private final RabbitTemplate rabbitTemplate;

    /**
     * 发送消息到默认交换机
     *
     * @param routingKey 路由键
     * @param message    消息内容
     */
    public void send(String routingKey, Object message) {
        send(null, routingKey, message);
    }

    /**
     * 发送消息到指定交换机
     *
     * @param exchange   交换机名称
     * @param routingKey 路由键
     * @param message    消息内容
     */
    public void send(String exchange, String routingKey, Object message) {
        try {
            rabbitTemplate.convertAndSend(exchange, routingKey, message);
            log.debug("Message sent successfully. exchange: {}, routingKey: {}", exchange, routingKey);
        } catch (Exception e) {
            log.error("Failed to send message. exchange: {}, routingKey: {}", exchange, routingKey, e);
            throw e;
        }
    }

    /**
     * 发送消息并设置过期时间
     *
     * @param exchange   交换机名称
     * @param routingKey 路由键
     * @param message    消息内容
     * @param expiration 过期时间（毫秒）
     */
    public void sendWithExpiration(String exchange, String routingKey, Object message, long expiration) {
        MessagePostProcessor messagePostProcessor = msg -> {
            msg.getMessageProperties().setExpiration(String.valueOf(expiration));
            return msg;
        };
        try {
            rabbitTemplate.convertAndSend(exchange, routingKey, message, messagePostProcessor);
            log.debug("Message sent with expiration. exchange: {}, routingKey: {}, expiration: {}ms",
                    exchange, routingKey, expiration);
        } catch (Exception e) {
            log.error("Failed to send message with expiration. exchange: {}, routingKey: {}", exchange, routingKey, e);
            throw e;
        }
    }

    /**
     * 发送延迟消息（需要配置延迟队列插件）
     *
     * @param exchange   交换机名称
     * @param routingKey 路由键
     * @param message    消息内容
     * @param delay      延迟时间（毫秒）
     */
    public void sendWithDelay(String exchange, String routingKey, Object message, long delay) {
        MessagePostProcessor messagePostProcessor = msg -> {
            msg.getMessageProperties().setHeader("x-delay", delay);
            return msg;
        };
        try {
            rabbitTemplate.convertAndSend(exchange, routingKey, message, messagePostProcessor);
            log.debug("Delayed message sent. exchange: {}, routingKey: {}, delay: {}ms",
                    exchange, routingKey, delay);
        } catch (Exception e) {
            log.error("Failed to send delayed message. exchange: {}, routingKey: {}", exchange, routingKey, e);
            throw e;
        }
    }

    /**
     * 发送消息并设置消息属性
     *
     * @param exchange           交换机名称
     * @param routingKey         路由键
     * @param message            消息内容
     * @param messageProperties  消息属性
     */
    public void sendWithProperties(String exchange, String routingKey, Object message,
                                    MessageProperties messageProperties) {
        MessagePostProcessor messagePostProcessor = msg -> {
            MessageProperties props = msg.getMessageProperties();
            if (messageProperties != null) {
                if (messageProperties.getContentType() != null) {
                    props.setContentType(messageProperties.getContentType());
                }
                if (messageProperties.getContentEncoding() != null) {
                    props.setContentEncoding(messageProperties.getContentEncoding());
                }
                if (messageProperties.getMessageId() != null) {
                    props.setMessageId(messageProperties.getMessageId());
                }
                if (messageProperties.getCorrelationId() != null) {
                    props.setCorrelationId(messageProperties.getCorrelationId());
                }
                if (messageProperties.getReplyTo() != null) {
                    props.setReplyTo(messageProperties.getReplyTo());
                }
                if (messageProperties.getExpiration() != null) {
                    props.setExpiration(messageProperties.getExpiration());
                }
                if (messageProperties.getHeaders() != null) {
                    props.getHeaders().putAll(messageProperties.getHeaders());
                }
            }
            return msg;
        };
        try {
            rabbitTemplate.convertAndSend(exchange, routingKey, message, messagePostProcessor);
            log.debug("Message sent with properties. exchange: {}, routingKey: {}", exchange, routingKey);
        } catch (Exception e) {
            log.error("Failed to send message with properties. exchange: {}, routingKey: {}", exchange, routingKey, e);
            throw e;
        }
    }

    /**
     * 发送消息并获取响应（RPC模式）
     *
     * @param exchange   交换机名称
     * @param routingKey 路由键
     * @param message    消息内容
     * @return 响应消息
     */
    public Object sendAndReceive(String exchange, String routingKey, Object message) {
        try {
            Object response = rabbitTemplate.convertSendAndReceive(exchange, routingKey, message);
            log.debug("RPC message sent and received. exchange: {}, routingKey: {}", exchange, routingKey);
            return response;
        } catch (Exception e) {
            log.error("Failed to send RPC message. exchange: {}, routingKey: {}", exchange, routingKey, e);
            throw e;
        }
    }

    /**
     * 转换并发送对象消息
     *
     * @param exchange   交换机名称
     * @param routingKey 路由键
     * @param message    消息对象
     */
    public void convertAndSend(String exchange, String routingKey, Object message) {
        send(exchange, routingKey, message);
    }

    /**
     * 获取底层的 RabbitTemplate 实例
     *
     * @return RabbitTemplate 实例
     */
    public RabbitTemplate getRabbitTemplate() {
        return rabbitTemplate;
    }
}
