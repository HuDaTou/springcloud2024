package com.overthinker.cloud.api.client;

import com.overthinker.cloud.api.dto.PermissionDTO; // Assuming this DTO is also in cloud-api
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * Feign client for the authentication service.
 */
@FeignClient(name = "cloud-auth")
public interface AuthClient {

    @PostMapping("/internal/api/v1/permissions/register")
    void registerPermissions(@RequestBody List<PermissionDTO> permissions);
}
