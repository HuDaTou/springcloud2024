package com.overthinker.cloud.api.dto;

public record VerifyResponse(boolean success, String message, String userId) {
}
