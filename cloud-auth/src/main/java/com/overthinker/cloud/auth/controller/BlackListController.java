package com.overthinker.cloud.auth.controller;


import com.overthinker.cloud.api.apis.auth.dto.AddBlackListRequest;
import com.overthinker.cloud.api.apis.auth.dto.BlackListCheckResponse;
import com.overthinker.cloud.api.apis.auth.api.BlackListClient;
import com.overthinker.cloud.auth.entity.DTO.AddBlackListDTO;
import com.overthinker.cloud.auth.entity.DTO.SearchBlackListDTO;
import com.overthinker.cloud.auth.entity.DTO.UpdateBlackListDTO;
import com.overthinker.cloud.auth.entity.VO.BlackListVO;
import com.overthinker.cloud.auth.service.BlackListService;
import com.overthinker.cloud.common.core.annotation.LogAnnotation;
import com.overthinker.cloud.common.core.annotation.LogConst;
import com.overthinker.cloud.common.core.base.BaseController;


import com.overthinker.cloud.common.core.resp.ResultData;
import com.overthinker.cloud.system.starter.redis.annotation.AccessLimit;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import com.overthinker.cloud.auth.exception.BlackListException;
import lombok.RequiredArgsConstructor;

/**
 * 黑名单控制器
 * <p>
 * 同时提供：
 * <ul>
 *   <li>管理后台接口（@PreAuthorize 权限控制）</li>
 *   <li>内部 Feign 接口（服务 Token 全放行，本类实现 BlackListClient）</li>
 * </ul>
 * </p>
 *
 * @author overH
 * @since 2024-09-05 16:13:19
 */
@RestController
@RequestMapping("blackList")
@RequiredArgsConstructor
public class BlackListController extends BaseController implements BlackListClient {

    private final BlackListService blackListService;

    // ==================== 管理后台接口 ====================

    @PreAuthorize("hasAnyAuthority('blog:black:add')")
    @Operation(summary = "添加黑名单")
    @Parameter(name = "addBlackListDTO", description = "添加黑名单DTO")
    @LogAnnotation(module = "黑名单管理", operation = LogConst.INSERT)
    @AccessLimit(seconds = 60, maxCount = 30)
    @PostMapping("/add")
    public ResultData<Void> addBlackList(@RequestBody @Valid AddBlackListDTO addBlackListDTO) throws BlackListException {
        return blackListService.addBlackList(addBlackListDTO);
    }

    @PreAuthorize("hasAnyAuthority('blog:black:update')")
    @Operation(summary = "修改黑名单")
    @Parameter(name = "updateBlackListDTO", description = "修改黑名单")
    @LogAnnotation(module = "黑名单管理", operation = LogConst.UPDATE)
    @AccessLimit(seconds = 60, maxCount = 30)
    @PutMapping("/update")
    public ResultData<Void> updateBlackList(@RequestBody @Valid UpdateBlackListDTO updateBlackListDTO) {
        return blackListService.updateBlackList(updateBlackListDTO);
    }

    @PreAuthorize("hasAnyAuthority('blog:black:select')")
    @Operation(summary = "查询黑名单")
    @LogAnnotation(module = "黑名单管理", operation = LogConst.GET)
    @AccessLimit(seconds = 60, maxCount = 30)
    @PostMapping("/getBlackListing")
    public ResultData<List<BlackListVO>> getBlackList(@RequestBody(required = false) SearchBlackListDTO searchBlackListDTO) {
        return messageHandler(() -> blackListService.getBlackList(searchBlackListDTO));
    }

    @PreAuthorize("hasAnyAuthority('blog:black:delete')")
    @Operation(summary = "删除黑名单")
    @Parameter(name = "id", description = "id")
    @LogAnnotation(module = "黑名单管理", operation = LogConst.DELETE)
    @AccessLimit(seconds = 60, maxCount = 30)
    @DeleteMapping("/delete")
    public ResultData<Void> deleteBlackList(@RequestBody List<Long> ids) {
        return blackListService.deleteBlackList(ids);
    }

    // ==================== 内部 Feign 接口（BlackListClient 实现） ====================

    @Operation(summary = "检查黑名单", description = "检查指定 IP 或用户是否在黑名单中")
    @GetMapping("/check")
    public BlackListCheckResponse checkBlacklist(
            @Parameter(description = "客户端 IP 地址") @RequestParam String ip,
            @Parameter(description = "当前登录用户 ID") @RequestParam(required = false) Long userId) {
        return blackListService.checkBlacklist(ip, userId);
    }

    @Operation(summary = "添加黑名单（内部调用）", description = "将 IP 或用户添加到黑名单")
    @PostMapping("/api-add")
    public void addBlacklist(
            @Parameter(description = "添加黑名单请求") @RequestBody AddBlackListRequest request) {
        blackListService.addBlacklistInternal(request);
    }

    @Operation(summary = "解除黑名单", description = "移除指定 IP 或用户的黑名单限制")
    @DeleteMapping("/expire")
    public void expireBlacklist(
            @Parameter(description = "IP 地址") @RequestParam(required = false) String ip,
            @Parameter(description = "用户 ID") @RequestParam(required = false) Long userId) {
        blackListService.expireBlacklist(ip, userId);
    }
}
