package com.overthinker.cloud.system.auth.service;

import com.overthinker.cloud.api.dto.PermissionDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Scans for @RestController beans and extracts permission metadata from their methods.
 */
public class PermissionScanner {

    private final ApplicationContext applicationContext;
    private final Environment environment;

    public PermissionScanner(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        this.environment = applicationContext.getEnvironment();
    }

    public List<PermissionDTO> scanPermissions() {
        List<PermissionDTO> permissions = new ArrayList<>();
        String serviceName = environment.getProperty("spring.application.name", "");
        String servicePathPrefix = "/" + serviceName.replace("-service", "");

        Map<String, Object> controllers = applicationContext.getBeansWithAnnotation(RestController.class);

        for (Object controller : controllers.values()) {
            Class<?> controllerClass = controller.getClass();
            String category = getCategory(controllerClass);
            String controllerBasePath = getControllerBasePath(controllerClass);

            for (Method method : controllerClass.getDeclaredMethods()) {
                String httpMethod = getHttpMethod(method);
                if (httpMethod == null) continue; // Not a request mapping method

                String operationName = getOperationName(method);
                if (operationName == null) continue; // No @Operation, skip

                String methodPath = getMethodPath(method);
                String fullPath = (servicePathPrefix + controllerBasePath + methodPath).replaceAll("//", "/");

                permissions.add(new PermissionDTO(category, operationName, httpMethod, fullPath));
            }
        }
        return permissions;
    }

    private String getCategory(Class<?> clazz) {
        Tag tag = clazz.getAnnotation(Tag.class);
        return (tag != null) ? tag.name() : "Uncategorized";
    }

    private String getOperationName(Method method) {
        Operation operation = method.getAnnotation(Operation.class);
        return (operation != null) ? operation.summary() : null;
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
        return null;
    }

    private String getMethodPath(Method method) {
        String[] path;
        if (method.isAnnotationPresent(GetMapping.class)) {
            path = method.getAnnotation(GetMapping.class).value();
            return (path.length > 0) ? path[0] : "";
        }
        if (method.isAnnotationPresent(PostMapping.class)) {
            path = method.getAnnotation(PostMapping.class).value();
            return (path.length > 0) ? path[0] : "";
        }
        if (method.isAnnotationPresent(PutMapping.class)) {
            path = method.getAnnotation(PutMapping.class).value();
            return (path.length > 0) ? path[0] : "";
        }
        if (method.isAnnotationPresent(DeleteMapping.class)) {
            path = method.getAnnotation(DeleteMapping.class).value();
            return (path.length > 0) ? path[0] : "";
        }
        if (method.isAnnotationPresent(PatchMapping.class)) {
            path = method.getAnnotation(PatchMapping.class).value();
            return (path.length > 0) ? path[0] : "";
        }
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        if (requestMapping != null && requestMapping.value().length > 0) {
            return requestMapping.value()[0];
        }
        return "";
    }
}
