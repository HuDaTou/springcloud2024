package com.overthinker.cloud.api.apis.auth.api;

import com.overthinker.cloud.common.core.resp.ResultData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@FeignClient(name = "cloud-auth", contextId = "userClient")
public interface UserClient {

    @GetMapping("/user/count")
    ResultData<Long> getUserCount();

    @GetMapping("/user/username")
    ResultData<String> getUsernameById(@RequestParam("userId") Long userId);

    @GetMapping("/user/email")
    ResultData<String> getEmailById(@RequestParam("userId") Long userId);

    @GetMapping("/user/info")
    ResultData<Map<String, Object>> getUserInfoById(@RequestParam("userId") Long userId);

    @GetMapping("/user/search")
    ResultData<List<Long>> searchUserIdsByUsername(@RequestParam("username") String username);
}