package com.overthinker.cloud.system.auth.service;

import com.overthinker.cloud.api.dto.PermissionDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 权限扫描器
 * 负责扫描Spring MVC的Controller方法，提取@PreAuthorize权限标识和OpenAPI描述信息。
 */
@Slf4j
public class PermissionScanner {

    private final ApplicationContext applicationContext;
    private final Environment environment;

    // 正则表达式，用于从 @PreAuthorize("hasAuthority('xxx')") 中提取 'xxx'
    private static final Pattern PRE_AUTHORIZE_PATTERN = Pattern.compile("hasAuthority\\('([^']+)'\\)");

    public PermissionScanner(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        this.environment = applicationContext.getEnvironment();
    }

    /**
     * 扫描当前服务中的所有接口权限
     * @return 权限列表
     */
    public List<PermissionDTO> scanPermissions() {
        List<PermissionDTO> permissions = new ArrayList<>();
        String serviceName = environment.getProperty("spring.application.name", "unknown-service");
        // 去除 serviceName 可能存在的后缀，如 -service，保持简洁
        String cleanServiceName = serviceName.replace("-service", "");
        String servicePathPrefix = "/" + cleanServiceName;

        log.info("开始扫描服务 [{}] 的接口权限...", serviceName);

        Map<String, Object> controllers = applicationContext.getBeansWithAnnotation(RestController.class);

        for (Object controller : controllers.values()) {
            Class<?> controllerClass = controller.getClass();
            // 处理CGLIB代理类的情况，获取原始类
            if (controllerClass.getName().contains("$$")) {
                controllerClass = controllerClass.getSuperclass();
            }

            String category = getCategory(controllerClass);
            String controllerBasePath = getControllerBasePath(controllerClass);

            for (Method method : controllerClass.getDeclaredMethods()) {
                // 1. 获取HTTP方法，如果不是映射方法则跳过
                String httpMethod = getHttpMethod(method);
                if (httpMethod == null) continue;

                // 2. 获取权限标识 Code
                String code = getPermissionCode(method);
                // 如果没有@PreAuthorize注解，视为公开接口或未配置权限控制接口，暂时跳过或记录为默认
                // 这里选择：如果没有显式权限控制，不纳入权限管理表
                if (code == null) {
                    log.debug("跳过未配置 @PreAuthorize 的方法: {}.வைக்", controllerClass.getSimpleName(), method.getName());
                    continue;
                }

                // 3. 获取接口名称/描述
                String operationName = getOperationName(method);
                if (operationName == null) {
                    operationName = method.getName(); // 降级使用方法名
                }

                // 4. 组装完整路径
                String methodPath = getMethodPath(method);
                String fullPath = (servicePathPrefix + controllerBasePath + methodPath).replaceAll("//+", "/");

                PermissionDTO dto = new PermissionDTO(
                        category,
                        operationName,
                        code,
                        httpMethod,
                        fullPath,
                        serviceName
                );
                permissions.add(dto);
            }
        }
        log.info("服务 [{}] 权限扫描完成，共找到 {} 个权限点。", serviceName, permissions.size());
        return permissions;
    }

    private String getCategory(Class<?> clazz) {
        Tag tag = clazz.getAnnotation(Tag.class);
        return (tag != null && StringUtils.hasText(tag.name())) ? tag.name() : clazz.getSimpleName();
    }

    private String getOperationName(Method method) {
        Operation operation = method.getAnnotation(Operation.class);
        return (operation != null && StringUtils.hasText(operation.summary())) ? operation.summary() : null;
    }

    /**
     * 解析 @PreAuthorize 注解内容，提取权限标识
     */
    private String getPermissionCode(Method method) {
        PreAuthorize preAuthorize = method.getAnnotation(PreAuthorize.class);
        if (preAuthorize != null) {
            String expression = preAuthorize.value();
            Matcher matcher = PRE_AUTHORIZE_PATTERN.matcher(expression);
            if (matcher.find()) {
                return matcher.group(1);
            }
            // 如果正则表达式不匹配，直接返回表达式本身（可能就是简单的字符串，或者使用了其他SpEL）
            return expression;
        }
        return null;
    }

    private String getControllerBasePath(Class<?> clazz) {
        RequestMapping requestMapping = clazz.getAnnotation(RequestMapping.class);
        if (requestMapping != null && requestMapping.value().length > 0) {
            return requestMapping.value()[0];
        }
        return "";
    }

    private String getHttpMethod(Method method) {
        if (method.isAnnotationPresent(GetMapping.class)) return "GET";
        if (method.isAnnotationPresent(PostMapping.class)) return "POST";
        if (method.isAnnotationPresent(PutMapping.class)) return "PUT";
        if (method.isAnnotationPresent(DeleteMapping.class)) return "DELETE";
        if (method.isAnnotationPresent(PatchMapping.class)) return "PATCH";
        if (method.isAnnotationPresent(RequestMapping.class)) {
            RequestMapping mapping = method.getAnnotation(RequestMapping.class);
            if (mapping.method().length > 0) {
                return mapping.method()[0].name();
            }
            return "ALL"; // RequestMapping 默认匹配所有
        }
        return null;
    }

    private String getMethodPath(Method method) {
        if (method.isAnnotationPresent(GetMapping.class)) return getFirstPath(method.getAnnotation(GetMapping.class).value());
        if (method.isAnnotationPresent(PostMapping.class)) return getFirstPath(method.getAnnotation(PostMapping.class).value());
        if (method.isAnnotationPresent(PutMapping.class)) return getFirstPath(method.getAnnotation(PutMapping.class).value());
        if (method.isAnnotationPresent(DeleteMapping.class)) return getFirstPath(method.getAnnotation(DeleteMapping.class).value());
        if (method.isAnnotationPresent(PatchMapping.class)) return getFirstPath(method.getAnnotation(PatchMapping.class).value());
        if (method.isAnnotationPresent(RequestMapping.class)) return getFirstPath(method.getAnnotation(RequestMapping.class).value());
        return "";
    }

    private String getFirstPath(String[] paths) {
        return (paths != null && paths.length > 0) ? paths[0] : "";
    }
}