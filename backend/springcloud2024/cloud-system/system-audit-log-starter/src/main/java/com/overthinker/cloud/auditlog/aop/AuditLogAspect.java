package com.overthinker.cloud.auditlog.aop;

import com.alibaba.fastjson.JSON;
import com.overthinker.cloud.common.dto.AuditLogDTO;
import com.overthinker.cloud.common.util.IpUtils;
import com.overthinker.cloud.common.util.SecurityUtils; // Assuming SecurityUtils is in common
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;

@Component
@Slf4j
@Aspect
public class AuditLogAspect {

    private final RabbitTemplate rabbitTemplate;

    @Value("${spring.rabbitmq.exchange.audit-log:audit.log.exchange}")
    private String exchange;

    @Value("${spring.rabbitmq.routingKey.audit-log:audit.log.routingKey}")
    private String routingKey;

    public AuditLogAspect(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Pointcut("execution(public * com.overthinker.cloud..controller..*.*(..))")
    public void controllerLogPointcut() {}

    @Around("controllerLogPointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        long beginTime = System.currentTimeMillis();
        Object result = null;
        Exception exception = null;

        try {
            result = joinPoint.proceed();
            return result;
        } catch (Exception e) {
            exception = e;
            throw e;
        } finally {
            long time = System.currentTimeMillis() - beginTime;
            recordLog(joinPoint, time, result, exception);
        }
    }

    private void recordLog(ProceedingJoinPoint joinPoint, long time, Object result, Exception exception) {
        try {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();

            Tag tag = joinPoint.getTarget().getClass().getAnnotation(Tag.class);
            Operation operation = method.getAnnotation(Operation.class);

            // Only log methods annotated with @Operation
            if (operation == null) {
                return;
            }

            String module = (tag != null) ? tag.name() : "未分类模块";
            String operationName = operation.summary();

            HttpServletRequest request = SecurityUtils.getCurrentHttpRequest();
            String ip = (request != null) ? IpUtils.getIpAddr(request) : "N/A";
            String requestUri = (request != null) ? request.getRequestURI() : "N/A";
            String httpMethod = (request != null) ? request.getMethod() : "N/A";

            String userId = SecurityUtils.getUserId() != null ? String.valueOf(SecurityUtils.getUserId()) : "anonymous";

            Object[] args = joinPoint.getArgs();
            String params = Arrays.stream(args)
                    .filter(arg -> !(arg instanceof MultipartFile))
                    .map(JSON::toJSONString)
                    .collect(Collectors.joining(", "));

            AuditLogDTO logDTO = new AuditLogDTO();
            logDTO.setModule(module);
            logDTO.setOperation(operationName);
            logDTO.setUserId(userId);
            logDTO.setIp(ip);
            logDTO.setRequestUri(requestUri);
            logDTO.setRequestMethod(httpMethod);
            logDTO.setExecutionTime(time);
            logDTO.setParameters(params);

            if (exception != null) {
                logDTO.setStatus("FAILED");
                logDTO.setErrorMsg(exception.getMessage());
            } else {
                logDTO.setStatus("SUCCESS");
            }

            rabbitTemplate.convertAndSend(exchange, routingKey, logDTO);

        } catch (Exception e) {
            log.error("审计日志切面自身发生异常", e);
        }
    }
}
