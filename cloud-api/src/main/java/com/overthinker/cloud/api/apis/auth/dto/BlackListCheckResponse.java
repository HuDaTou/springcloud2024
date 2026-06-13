package com.overthinker.cloud.api.apis.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BlackListCheckResponse {
    private boolean blocked;
    private String message;
    private Long expiresTime;
}