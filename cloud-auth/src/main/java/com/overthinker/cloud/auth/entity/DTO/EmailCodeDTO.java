package com.overthinker.cloud.auth.entity.DTO;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class EmailCodeDTO {
    @Email
    private String email;
    private String type;
}
