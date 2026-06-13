package com.overthinker.cloud.api.apis.auth.mq;

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

    private String serviceName;

    private String category;

    private String name;

    private String httpMethod;

    private String path;

    private String permissonCode;

}