package com.overthinker.cloud.api.apis.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddBlackListRequest {
    private List<Long> userIds;
    private String reason;
    private LocalDateTime expiresTime;
}