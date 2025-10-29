package com.overthinker.cloud.common.core.entity.DTO;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class AuditLogDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String module;
    private String operation;
    private String userId;
    private String ip;
    private String requestUri;
    private String requestMethod;
    private String parameters;
    private Long executionTime;
    private String status;
    private String errorMsg;
    private LocalDateTime createdAt = LocalDateTime.now();
}
