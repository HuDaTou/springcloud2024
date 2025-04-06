package com.overthinker.cloud.web.config.rabbit;


import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {


    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        // 在注入Bean的时候必须将连接工厂装配进来，因为这是自己定义的一个Bean
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        // setMessageConverter 方法使用jackson2去实现序列化
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        return rabbitTemplate;
    }

}
