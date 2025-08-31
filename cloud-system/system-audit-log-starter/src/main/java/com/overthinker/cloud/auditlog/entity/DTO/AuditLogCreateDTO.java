package com.overthinker.cloud.auditlog.entity.DTO;

import java.time.LocalDateTime;

/**
 * DTO for creating an audit log entry.
 * This record captures the state of a single audited event.
 */
public record AuditLogCreateDTO(
    String userId,
    String ipAddress,
    String endpoint,
    String method,
    String description,
    Integer executionTime,
    LocalDateTime timestamp,
    Integer state // 0 for success, 1 for failure
) {}
