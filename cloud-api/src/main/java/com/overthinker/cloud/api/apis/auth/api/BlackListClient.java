package com.overthinker.cloud.api.apis.auth.api;

import com.overthinker.cloud.api.apis.auth.dto.AddBlackListRequest;
import com.overthinker.cloud.api.apis.auth.dto.BlackListCheckResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "cloud-auth", contextId = "blackListClient")
public interface BlackListClient {

    @GetMapping("/blackList/check")
    BlackListCheckResponse checkBlacklist(@RequestParam("ip") String ip, @RequestParam(value = "userId", required = false) Long userId);

    @PostMapping("/blackList/api-add")
    void addBlacklist(@RequestBody AddBlackListRequest request);

    @DeleteMapping("/blackList/expire")
    void expireBlacklist(@RequestParam(value = "ip", required = false) String ip, @RequestParam(value = "userId", required = false) Long userId);
}