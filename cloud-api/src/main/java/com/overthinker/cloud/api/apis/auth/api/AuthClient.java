package com.overthinker.cloud.api.apis.auth.api;

import com.overthinker.cloud.api.apis.auth.mq.PermissionDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * Feign client for the authentication service.
 */
@FeignClient(name = "cloud-auth", contextId = "authClient")
public interface AuthClient {

    @PostMapping("/permissions/register")
    void registerPermissions(@RequestBody List<PermissionDTO> permissions);
}
