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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 黑名单控制器
 * <p>
 * 提供黑名单的增删改查管理接口，以及内部服务调用的 Feign 接口。
 * </p>
 *
 * @author overH
 * @since 2024-09-05
 */
@Slf4j
@Tag(name = "黑名单管理", description = "系统黑名单的增删改查管理接口")
@RestController
@RequestMapping("/blackList")
@RequiredArgsConstructor
public class BlackListController extends BaseController implements BlackListClient {

    private final BlackListService blackListService;

    // ==================== 管理后台接口 ====================

    @Operation(summary = "获取黑名单列表", description = "分页或条件查询黑名单列表")
    @PreAuthorize("hasAuthority('blog:black:list')")
    @PostMapping("/list")
    public ResultData<List<BlackListVO>> list(@RequestBody(required = false) SearchBlackListDTO searchDTO) {
        return messageHandler(() -> blackListService.getBlackList(searchDTO));
    }

    @Operation(summary = "添加黑名单", description = "将 IP 或用户添加到黑名单")
    @PreAuthorize("hasAuthority('blog:black:add')")
    @LogAnnotation(module = "黑名单管理", operation = LogConst.INSERT)
    @PostMapping
    public ResultData<Void> add(@Valid @RequestBody AddBlackListDTO addDTO) {
        return blackListService.addBlackList(addDTO);
    }

    @Operation(summary = "修改黑名单", description = "根据 ID 更新黑名单信息")
    @PreAuthorize("hasAuthority('blog:black:edit')")
    @LogAnnotation(module = "黑名单管理", operation = LogConst.UPDATE)
    @PutMapping("/{id}")
    public ResultData<Void> update(
            @Parameter(description = "黑名单ID") @PathVariable Long id,
            @Valid @RequestBody UpdateBlackListDTO updateDTO) {
        updateDTO.setId(id);
        return blackListService.updateBlackList(updateDTO);
    }

    @Operation(summary = "删除黑名单", description = "根据 ID 删除黑名单记录")
    @PreAuthorize("hasAuthority('blog:black:delete')")
    @LogAnnotation(module = "黑名单管理", operation = LogConst.DELETE)
    @DeleteMapping("/{ids}")
    public ResultData<Void> delete(@Parameter(description = "黑名单ID列表") @PathVariable List<Long> ids) {
        return blackListService.deleteBlackList(ids);
    }

    // ==================== 内部 Feign 接口（BlackListClient 实现） ====================

    @Operation(summary = "检查黑名单", description = "检查指定 IP 或用户是否在黑名单中（内部服务调用）")
    @GetMapping("/check")
    public BlackListCheckResponse checkBlacklist(
            @Parameter(description = "客户端 IP 地址") @RequestParam String ip, 
            @Parameter(description = "当前登录用户 ID") @RequestParam(required = false) Long userId) {
        return blackListService.checkBlacklist(ip, userId);
    }

    @Operation(summary = "添加黑名单（内部调用）", description = "将 IP 或用户添加到黑名单（内部服务调用）")
    @PostMapping("/api-add")
    @Override
    public void addBlacklist(@RequestBody AddBlackListRequest request) {
        blackListService.addBlacklistInternal(request);
    }

    @Operation(summary = "解除黑名单（内部调用）", description = "移除指定 IP 或用户的黑名单限制")
    @DeleteMapping("/expire")
    public void expireBlacklist(
            @Parameter(description = "IP 地址") @RequestParam(required = false) String ip,
            @Parameter(description = "用户 ID") @RequestParam(required = false) Long userId) {
        blackListService.expireBlacklist(ip, userId);
    }

}
