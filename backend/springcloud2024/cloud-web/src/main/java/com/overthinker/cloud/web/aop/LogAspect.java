package com.overthinker.cloud.web.aop;

import com.alibaba.fastjson.JSON;
import com.overthinker.cloud.resp.ResultData;
import com.overthinker.cloud.web.annotation.LogAnnotation;
import com.overthinker.cloud.web.constants.FunctionConst;
import com.overthinker.cloud.web.entity.PO.Log;
import com.overthinker.cloud.web.entity.PO.User;
import com.overthinker.cloud.web.mapper.UserMapper;
import com.overthinker.cloud.web.utils.AddressUtils;
import com.overthinker.cloud.web.utils.IpUtils;
import com.overthinker.cloud.web.utils.SecurityUtils;
import com.overthinker.cloud.web.utils.StringUtils;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Component
@Slf4j
@Aspect // 切面
public class LogAspect {

    @Resource
    private UserMapper userMapper;

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Value("${spring.rabbitmq.routingKey.log-system}")
    private String routingKey;

    @Value("${spring.rabbitmq.exchange.log}")
    private String exchange;

    /**
     * 切点，注解加在哪，哪就是切点
     */
    @Pointcut("@annotation(com.overthinker.cloud.web.annotation.LogAnnotation)")
    public void pt() {
    }

    // 环绕通知，在方法执行前后执行
    @Around("pt()")
    public Object log(ProceedingJoinPoint joinPoint) throws Throwable {
        long beginTime = System.currentTimeMillis();
        try {
            // 执行方法
            Object result = joinPoint.proceed();
            // 执行时长
            long time = System.currentTimeMillis() - beginTime;
            recordLog(joinPoint, time,result);
            // 打印日志
            log.info("【{}】执行方法【{}】，耗时【{}】毫秒", joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName(), time);
            return result;
        } catch (Throwable e) {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();
            LogAnnotation logAnnotation = method.getAnnotation(LogAnnotation.class);
            long time = System.currentTimeMillis() - beginTime;
            // 获取 request 设置IP地址
            HttpServletRequest request = SecurityUtils.getCurrentHttpRequest();
            // 请求的方法名
            String className = joinPoint.getTarget().getClass().getName();
            String methodName = signature.getName();
             assert request != null;
            // 是否前台
            String ipAddr = IpUtils.getIpAddr(request);
            User user = userMapper.selectById(SecurityUtils.getUserId());

            Object[] args = joinPoint.getArgs();
            List<Object> multipartFile = new ArrayList<>();
            for (Object arg : args) {
                if (arg instanceof MultipartFile) {
                    // 这个arg是MultipartFile类型
                    multipartFile.add(arg);
                }
            }
            Log logEntity = Log.builder()
                    .module(logAnnotation.module())
                    .operation(logAnnotation.operation())
                    .ip(ipAddr)
                    .exception(e.getMessage())
                    .reqMapping(request.getMethod())
                    .userName(StringUtils.isNull(user) ? FunctionConst.UNKNOWN_USER : user.getUsername())
                    .address(AddressUtils.getRealAddressByIP(ipAddr))
                    .state(2)
                    .exception(e.getMessage())
                    .method(className + "." + methodName + "()")
                    .reqParameter(!multipartFile.isEmpty() ? multipartFile.toString() : JSON.toJSONString(joinPoint.getArgs()))
                    .reqAddress(request.getRequestURI())
                    .time(time)
                    .build();
            rabbitTemplate.convertAndSend(exchange,routingKey,logEntity);
            log.error("【{}】执行方法【{}】异常", joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName(), e);
        }

        return null;
    }

    private void recordLog(ProceedingJoinPoint joinPoint, long time,Object result) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        LogAnnotation logAnnotation = method.getAnnotation(LogAnnotation.class);
        // 操作描述
        Operation operation = method.getAnnotation(Operation.class);

        // 获取 request 设置IP地址
        HttpServletRequest request = SecurityUtils.getCurrentHttpRequest();
        // 请求的方法名
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = signature.getName();
        assert request != null;
        String ipAddr = IpUtils.getIpAddr(request);
        User user = userMapper.selectById(SecurityUtils.getUserId());

        Object[] args = joinPoint.getArgs();
        List<Object> multipartFile = new ArrayList<>();
        for (Object arg : args) {
            if (arg instanceof MultipartFile) {
                // 这个arg是MultipartFile类型
                multipartFile.add(arg);
            }
        }

        Log log = Log.builder()
                .module(logAnnotation.module())
                .operation(logAnnotation.operation())
                .ip(ipAddr)
                .description(operation.summary())
                .address(AddressUtils.getRealAddressByIP(ipAddr))
                .reqMapping(request.getMethod())
                .userName(StringUtils.isNull(user) ? FunctionConst.UNKNOWN_USER : user.getUsername())
                .method(className + "." + methodName + "()")
                .reqParameter(!multipartFile.isEmpty() ? multipartFile.toString() : JSON.toJSONString(joinPoint.getArgs()))
                .returnParameter(JSON.toJSONString(result))
                .reqAddress(request.getRequestURI())
                .time(time)
                .build();
        // TODO ResultData为null
        ResultData ResultData = (ResultData)result;
        if ( ResultData != null && ResultData.getCode() == 200) {
            log.setState(0);
        }else{
            log.setState(1);
        }

        rabbitTemplate.convertAndSend(exchange,routingKey,log);
        LogAspect.log.info("耗时：{}毫秒", time);
        LogAspect.log.info("操作时间：{}", new Date());
        LogAspect.log.info("================日志结束=========================");

    }

}
