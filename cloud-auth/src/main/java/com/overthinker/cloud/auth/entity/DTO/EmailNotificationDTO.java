package com.overthinker.cloud.auth.entity.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Map;

@Data
public class EmailNotificationDTO {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String type;

    private Map<String, Object> content;
}
