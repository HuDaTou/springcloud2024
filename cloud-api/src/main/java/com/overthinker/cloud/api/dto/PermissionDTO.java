package com.overthinker.cloud.api.dto;

import java.io.Serializable;

/**
 * Data Transfer Object for transporting permission information.
 * 使用 Class 而不是 Record，以确保更好的序列化兼容性（特别是 JSON 序列化）。
 */
public class PermissionDTO implements Serializable {
    private String category;
    private String name;
    private String code; // 权限标识，如 "user:add"
    private String httpMethod;
    private String path;
    private String serviceName; // 所属服务名

    public PermissionDTO() {
    }

    public PermissionDTO(String category, String name, String code, String httpMethod, String path, String serviceName) {
        this.category = category;
        this.name = name;
        this.code = code;
        this.httpMethod = httpMethod;
        this.path = path;
        this.serviceName = serviceName;
    }

    // Getters and Setters

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
}