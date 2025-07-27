package com.overthinker.cloud.auditlog.aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.overthinker.cloud.auditlog.entity.DTO.LogDTO;
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

    public AuditLogAspect(RabbitTemplate rabbitTemplate, ObjectMapper objectMapper, String exchangeName, String routingKey) {
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
        this.exchangeName = exchangeName;
        this.routingKey = routingKey;
    }

    @Around("(@within(org.springframework.web.bind.annotation.RestController) || @within(org.springframework.stereotype.Controller)) && execution(public * *(..))")
    public Object logAudit(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long endTime = System.currentTimeMillis();

        try {
            LogDTO logDTO = buildLogDTO(joinPoint, endTime - startTime);
            String logJson = objectMapper.writeValueAsString(logDTO);
            rabbitTemplate.convertAndSend(exchangeName, routingKey, logJson);
        } catch (Exception e) {
            log.error("Failed to send audit log", e);
        }

        return result;
    }

    private LogDTO buildLogDTO(ProceedingJoinPoint joinPoint, long executionTime) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        LogDTO logDTO = new LogDTO();
        logDTO.setUserId(UserContextHolder.getUserId());
        logDTO.setIpAddress(request.getRemoteAddr());
        logDTO.setEndpoint(request.getRequestURI());
        logDTO.setMethod(request.getMethod());
        logDTO.setExecutionTime((int) executionTime);
        logDTO.setTimestamp(LocalDateTime.now());

        Operation operationAnnotation = method.getAnnotation(Operation.class);
        if (operationAnnotation != null) {
            logDTO.setDescription(operationAnnotation.summary());
        }

        return logDTO;
    }
}

