package com.overthinker.cloud.api.auth.api;

import com.overthinker.cloud.api.auth.dto.AddBlackListRequest;
import com.overthinker.cloud.api.auth.dto.BlackListCheckResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "cloud-auth")
public interface BlackListClient {

    @GetMapping("/internal/api/blacklist/check")
    BlackListCheckResponse checkBlacklist(@RequestParam String ip, @RequestParam(required = false) Long userId);

    @PostMapping("/internal/api/blacklist/add")
    void addBlacklist(@RequestBody AddBlackListRequest request);

    @DeleteMapping("/internal/api/blacklist/expire")
    void expireBlacklist(@RequestParam(required = false) String ip, @RequestParam(required = false) Long userId);
}