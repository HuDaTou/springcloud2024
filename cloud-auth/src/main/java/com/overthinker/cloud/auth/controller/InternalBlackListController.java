package com.overthinker.cloud.auth.controller;

import com.overthinker.cloud.api.auth.dto.AddBlackListRequest;
import com.overthinker.cloud.api.auth.dto.BlackListCheckResponse;
import com.overthinker.cloud.auth.service.BlackListService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 内部黑名单控制器
 * <p>
 * 【设计说明】
 * 这是一个专为微服务间内部调用设计的控制器，路由前缀为 `/internal/api/`。
 * 通过网关层的路由配置，这些接口不会暴露给外部用户，只能被其他微服务通过 Feign 调用。
 * </p>
 *
 * <p>
 * 【主要职责】
 * 1. 提供黑名单查询接口，供网关层在请求进入时进行拦截检查
 * 2. 提供黑名单添加接口，供其他服务在发现异常行为时主动拉黑
 * 3. 提供黑名单过期接口，供定时任务或管理员手动解除限制
 * </p>
 *
 * <p>
 * 【典型使用场景】
 * - 网关层：调用 check 接口，在请求进入时检查 IP/用户是否被拉黑，若被拉黑则直接拒绝
 * - 用户服务：调用 add 接口，当检测到异常登录行为（如暴力破解）时，自动将 IP 加入黑名单
 * - 定时任务：调用 expire 接口，定期清理过期的黑名单记录
 * </p>
 *
 * <p>
 * 【安全注意】
 * 此接口不添加权限注解（@PreAuthorize），因为是内部服务间调用，
 * 安全保障依赖于网关层的路由隔离和服务注册中心的访问控制。
 * </p>
 */
@Tag(name = "内部黑名单管理", description = "黑名单内部管理接口（仅供其他微服务通过 Feign 调用）")
@RestController
@RequestMapping("/internal/api/blacklist")
@RequiredArgsConstructor
public class InternalBlackListController {

    private final BlackListService blackListService;

    /**
     * 检查是否在黑名单中
     * <p>
     * 【调用方】主要由网关层调用
     * 【调用时机】每个请求进入时，在网关层进行拦截检查
     * 【检查优先级】userId > ip（优先检查用户 ID，用户 ID 不存在则检查 IP）
     * </p>
     *
     * @param ip     IP 地址（必填），例如 "192.168.1.100"
     * @param userId 用户 ID（可选），例如 1001
     * @return 黑名单检查结果，包含是否拉黑、拉黑原因、过期时间等信息
     */
    @Operation(summary = "检查黑名单", description = "检查指定 IP 或用户是否在黑名单中，网关层每个请求都会调用此接口")
    @GetMapping("/check")
    public BlackListCheckResponse checkBlacklist(
            @Parameter(description = "客户端 IP 地址，如 192.168.1.100") @RequestParam String ip,
            @Parameter(description = "当前登录用户 ID，未登录时不传") @RequestParam(required = false) Long userId) {
        return blackListService.checkBlacklist(ip, userId);
    }

    /**
     * 添加黑名单
     * <p>
     * 【调用方】用户服务、风控服务等
     * 【调用时机】检测到异常行为时，如：
     * - 连续登录失败超过阈值（暴力破解）
     * - 频繁请求敏感接口（恶意爬虫）
     * - 违反平台规则（管理员手动操作）
     * </p>
     *
     * @param request 包含 IP/用户 ID、拉黑原因、过期时间等信息
     */
    @Operation(summary = "添加黑名单", description = "将 IP 或用户添加到黑名单，用于阻止异常访问")
    @PostMapping("/add")
    public void addBlacklist(
            @Parameter(description = "添加黑名单请求，包含 IP/用户 ID、原因、过期时间") @RequestBody AddBlackListRequest request) {
        blackListService.addBlacklistInternal(request);
    }

    /**
     * 使黑名单过期（移除）
     * <p>
     * 【调用方】定时任务、管理员后台
     * 【调用时机】
     * - 定时任务自动清理过期记录
     * - 管理员手动解除限制（如用户申诉成功）
     * </p>
     *
     * @param ip     IP 地址（可选，与 userId 二选一）
     * @param userId 用户 ID（可选，与 ip 二选一）
     */
    @Operation(summary = "解除黑名单", description = "移除指定 IP 或用户的黑名单限制，恢复正常访问")
    @DeleteMapping("/expire")
    public void expireBlacklist(
            @Parameter(description = "要解除限制的 IP 地址") @RequestParam(required = false) String ip,
            @Parameter(description = "要解除限制的用户 ID") @RequestParam(required = false) Long userId) {
        blackListService.expireBlacklist(ip, userId);
    }
}