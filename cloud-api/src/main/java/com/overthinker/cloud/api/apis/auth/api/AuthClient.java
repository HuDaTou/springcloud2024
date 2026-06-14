package com.overthinker.cloud.api.apis.auth.api;

import com.overthinker.cloud.api.apis.auth.mq.PermissionDTO;
import com.overthinker.cloud.api.config.FeignClientCredentialsConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * Feign client for the authentication service.
 */
@FeignClient(name = "cloud-auth", configuration = FeignClientCredentialsConfig.class)
public interface AuthClient {

    @PostMapping("/permissions/register")
    void registerPermissions(@RequestBody List<PermissionDTO> permissions);
}
