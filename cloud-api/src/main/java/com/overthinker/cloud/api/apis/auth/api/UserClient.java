package com.overthinker.cloud.api.apis.auth.api;

import com.overthinker.cloud.api.config.FeignClientCredentialsConfig;
import com.overthinker.cloud.common.core.resp.ResultData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@FeignClient(name = "cloud-auth", configuration = FeignClientCredentialsConfig.class)
public interface UserClient {

    @GetMapping("/internal/api/user/count")
    ResultData<Long> getUserCount();

    @GetMapping("/internal/api/user/username")
    ResultData<String> getUsernameById(@RequestParam Long userId);

    @GetMapping("/internal/api/user/email")
    ResultData<String> getEmailById(@RequestParam Long userId);

    @GetMapping("/internal/api/user/info")
    ResultData<Map<String, Object>> getUserInfoById(@RequestParam Long userId);

    @GetMapping("/internal/api/user/search")
    ResultData<List<Long>> searchUserIdsByUsername(@RequestParam String username);
}