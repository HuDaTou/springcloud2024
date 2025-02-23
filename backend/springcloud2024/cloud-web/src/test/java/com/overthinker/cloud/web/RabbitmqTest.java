package com.overthinker.cloud.web;

import com.overthinker.cloud.web.entity.constants.RabbitConst;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.handler.annotation.Payload;

import java.util.Map;

@SpringBootTest
public class RabbitmqTest {

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Value("${spring.rabbitmq.routingKey.email}")
    private String routingKey;

    @Value("${spring.rabbitmq.exchange.email}")
    private String exchange;

    @Test
    public void publish() {
        Map<String, Object> senEmail = Map.of("email", "email", "code", "verifyCode", "type", "type");
        rabbitTemplate.convertAndSend(exchange, routingKey, senEmail);
    }

    @Test
    @RabbitListener(queues = RabbitConst.MAIL_QUEUE,
            errorHandler = "rabbitListenerErrorHandler",
            containerFactory = "rabbitListenerContainerFactory")
    public void handlerMapMessage(@Payload Map<String, Object> data) {
        String email = (String) data.get("email");
        String code = (String) data.get("code");
        String type = (String) data.get("type");
        System.out.printf("email: %s, code: %s, type: %s%n", email, code, type);
    }



}
