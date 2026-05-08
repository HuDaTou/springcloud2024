package com.overthinker.cloud.auth.controller;

import com.overthinker.cloud.api.auth.dto.AddBlackListRequest;
import com.overthinker.cloud.api.auth.dto.BlackListCheckResponse;
import com.overthinker.cloud.auth.service.BlackListService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/internal/api/v1/blacklist")
@RequiredArgsConstructor
public class InternalBlackListController {

    private final BlackListService blackListService;

    @GetMapping("/check")
    public BlackListCheckResponse checkBlacklist(@RequestParam String ip, @RequestParam(required = false) Long userId) {
        return blackListService.checkBlacklist(ip, userId);
    }

    @PostMapping("/add")
    public void addBlacklist(@RequestBody AddBlackListRequest request) {
        blackListService.addBlacklistInternal(request);
    }

    @DeleteMapping("/expire")
    public void expireBlacklist(@RequestParam(required = false) String ip, @RequestParam(required = false) Long userId) {
        blackListService.expireBlacklist(ip, userId);
    }
}