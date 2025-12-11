package com.overthinker.cloud.api.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Data Transfer Object for transporting permission information.
 * 使用 Class 而不是 Record，以确保更好的序列化兼容性（特别是 JSON 序列化）。
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class PermissionDTO {
    private String category;
    private String name;
    private String code; // 权限标识，如 "user:add"
    private String httpMethod;
    private String path;
    private String serviceName; // 所属服务名


}