package com.overthinker.cloud.auditlog.aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.overthinker.cloud.auditlog.entity.DTO.AuditLogCreateDTO;
import com.overthinker.cloud.auditlog.util.UserContextHolder;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


import java.lang.reflect.Method;
import java.time.LocalDateTime;

@Aspect
@Slf4j
public class AuditLogAspect {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;
    private final String exchangeName;
    private final String routingKey;

    // Constants for state
    private static final Integer STATE_SUCCESS = 0;
    private static final Integer STATE_FAILURE = 1;

    public AuditLogAspect(RabbitTemplate rabbitTemplate, ObjectMapper objectMapper, String exchangeName, String routingKey) {
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
        this.exchangeName = exchangeName;
        this.routingKey = routingKey;
    }

    @Around("(@within(org.springframework.web.bind.annotation.RestController) || @within(org.springframework.stereotype.Controller)) && execution(public * *(..))")
    public Object logAudit(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result;
        Integer status = STATE_SUCCESS;
        try {
            result = joinPoint.proceed();
            return result;
        } catch (Throwable e) {
            status = STATE_FAILURE;
            throw e; // Re-throw the exception after marking status as failure
        } finally {
            long endTime = System.currentTimeMillis();
            long executionTime = endTime - startTime;

            // Asynchronously send log to avoid impacting business logic performance
            try {
                AuditLogCreateDTO logDTO = buildAuditLogCreateDTO(joinPoint, executionTime, status);
                String logJson = objectMapper.writeValueAsString(logDTO);
                rabbitTemplate.convertAndSend(exchangeName, routingKey, logJson);
            } catch (Exception e) {
                log.error("Failed to send audit log for method: {}", joinPoint.getSignature().toShortString(), e);
            }
        }
    }

    private AuditLogCreateDTO buildAuditLogCreateDTO(ProceedingJoinPoint joinPoint, long executionTime, Integer status) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            // Cannot build log if not in a request context
            return null;
        }
        HttpServletRequest request = attributes.getRequest();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        String description = "";
        Operation operationAnnotation = method.getAnnotation(Operation.class);
        if (operationAnnotation != null) {
            description = operationAnnotation.summary();
        }

        // Handle Optional return types from UserContextHolder
        String userId = UserContextHolder.getUserId().orElse(null);

        return new AuditLogCreateDTO(
                userId,
                request.getRemoteAddr(),
                request.getRequestURI(),
                request.getMethod(),
                description,
                (int) executionTime,
                LocalDateTime.now(),
                status
        );
    }
}

