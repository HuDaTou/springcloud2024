package com.overthinker.cloud.system.starter.rabbitmq.config;

import com.overthinker.cloud.system.starter.rabbitmq.utils.MyRabbitMQ;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * RabbitMQ 自动配置类
 * <p>
 * 配置 RabbitTemplate 并注册 MyRabbitMQ 工具类
 * </p>
 *
 * @author overthinker
 * @since 2024-06-13
 */
@AutoConfiguration
@ConditionalOnClass(RabbitTemplate.class)
public class RabbitMQAutoConfiguration {

    /**
     * 创建 MyRabbitMQ 工具类实例
     *
     * @param rabbitTemplate RabbitTemplate 实例
     * @return MyRabbitMQ 实例
     */
    @Bean
    @ConditionalOnBean(RabbitTemplate.class)
    @ConditionalOnMissingBean
    public MyRabbitMQ myRabbitMQ(RabbitTemplate rabbitTemplate) {
        return new MyRabbitMQ(rabbitTemplate);
    }
}
