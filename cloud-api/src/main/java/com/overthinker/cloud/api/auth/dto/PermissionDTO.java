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
    /**
     * HTTP请求方法，例如 "GET", "POST"
     */
    private String httpMethod;

    /**
     * 权限的完整请求路径
     */
    private String path;
    //    权限代码
    private  String permissonCode;

}