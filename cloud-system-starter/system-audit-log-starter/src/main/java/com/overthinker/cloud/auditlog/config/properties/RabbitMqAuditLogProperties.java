package com.overthinker.cloud.auditlog.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for RabbitMQ in the audit log starter.
 * Allows for customizing queue, exchange, and routing key names.
 */
@Data
@ConfigurationProperties(prefix = "audit.log.rabbitmq")
public class RabbitMqAuditLogProperties {

    /**
     * The name of the exchange to which audit logs are sent.
     */
    private String exchangeName = "audit.log.exchange";

    /**
     * The name of the queue that collects audit logs.
     */
    private String queueName = "audit.log.queue";

    /**
     * The routing key used to bind the queue to the exchange.
     */
    private String routingKey = "audit.log.#";

    /**
     * Whether to enable sending audit logs to RabbitMQ.
     * Defaults to true.
     */
    private boolean enable = true;
}
