package com.overthinker.cloud.auth.controller;


import com.overthinker.cloud.common.core.base.BaseController;

import com.overthinker.cloud.auth.entity.DTO.*;
import com.overthinker.cloud.auth.entity.DTO.AdminCreateUserDTO;
import com.overthinker.cloud.auth.entity.VO.UserAccountVO;
import com.overthinker.cloud.auth.entity.VO.UserDetailsVO;
import com.overthinker.cloud.auth.entity.VO.UserListVO;

import com.overthinker.cloud.auth.service.UserService;
import com.overthinker.cloud.common.core.resp.ResultData;
import com.overthinker.cloud.system.starter.auth.utils.SecurityUtils;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Tag(name = "用户管理", description = "用户账户管理、信息修改、注册及管理后台用户操作接口")
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController extends BaseController {

    private final UserService userService;

    // ==================== 认证用户接口 ====================

    @Operation(summary = "获取当前用户信息", description = "获取当前登录用户的账号详细信息")
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/auth/info")
    public ResultData<UserAccountVO> getInfo() {
        return messageHandler(() -> userService.findAccountById(SecurityUtils.getUserId()));
    }

    @Operation(summary = "修改用户信息", description = "修改当前登录用户的昵称、简介等基本信息")
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/auth/update")
    public ResultData<Void> updateUser(@RequestBody @Valid UserUpdateDTO userUpdateDTO) {
        return userService.updateUser(userUpdateDTO);
    }

    @Operation(summary = "上传用户头像", description = "更新当前用户的头像路径")
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/auth/upload/avatar")
    public ResultData<String> uploadAvatar(
            @Parameter(description = "头像URL") @RequestParam("avatarUrl") String avatarUrl) {
        return userService.uploadAvatar(avatarUrl);
    }

    @Operation(summary = "修改用户绑定邮箱", description = "验证旧邮箱并绑定新邮箱")
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/auth/update/email")
    public ResultData<Void> updateEmail(@RequestBody @Valid UpdateEmailDTO updateEmailDTO) {
        return userService.updateEmailAndVerify(updateEmailDTO);
    }

    @Operation(summary = "第三方登录用户绑定邮箱", description = "第三方账号首次登录绑定邮箱")
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/auth/third/update/email")
    public ResultData<Void> thirdUpdateEmail(@RequestBody @Valid UpdateEmailDTO updateEmailDTO) {
        return userService.thirdUpdateEmail(updateEmailDTO);
    }

    // ==================== 管理员用户接口 ====================

    @Operation(summary = "获取用户列表", description = "【管理员】获取系统所有用户列表，支持条件搜索")
    @PreAuthorize("hasAuthority('auth:user:list')")
    @PostMapping("/list")
    public ResultData<List<UserListVO>> list(@RequestBody(required = false) UserSearchDTO searchDTO) {
        return messageHandler(() -> userService.getUserOrSearch(searchDTO));
    }

    @Operation(summary = "更新用户状态", description = "【管理员】启用或禁用用户")
    @PreAuthorize("hasAuthority('auth:user:edit')")
    @PostMapping("/update/status")
    public ResultData<Void> updateStatus(@RequestBody @Valid UpdateRoleStatusDTO updateRoleStatusDTO) {
        return userService.updateStatus(updateRoleStatusDTO.getId(), updateRoleStatusDTO.getStatus());
    }

    @Operation(summary = "获取用户详细信息", description = "【管理员】根据ID获取用户详细信息")
    @PreAuthorize("hasAuthority('auth:user:query')")
    @GetMapping("/details/{id}")
    public ResultData<UserDetailsVO> getUserDetails(
            @Parameter(description = "用户ID", required = true) @PathVariable("id") Long id) {
        return messageHandler(() -> userService.findUserDetailsById(id));
    }

    @Operation(summary = "删除用户", description = "【管理员】批量删除用户")
    @PreAuthorize("hasAuthority('auth:user:delete')")
    @DeleteMapping("/delete")
    public ResultData<Void> deleteUser(@RequestBody @Valid UserDeleteDTO userDeleteDTO) {
        return userService.deleteUser(userDeleteDTO.getIds());
    }

    @Operation(summary = "管理员创建用户", description = "【管理员】直接创建新用户，无需验证码")
    @PreAuthorize("hasAuthority('auth:user:add')")
    @PostMapping("/admin/create")
    public ResultData<Void> adminCreateUser(@RequestBody @Valid AdminCreateUserDTO adminCreateUserDTO) {
        return userService.adminCreateUser(adminCreateUserDTO);
    }

    // ==================== 内部服务调用接口 ====================

    @Operation(summary = "获取用户总数", description = "获取系统用户总数")
    @PreAuthorize("hasAuthority('auth:user:query')")
    @GetMapping("/count")
    public ResultData<Long> getUserCount() {
        return messageHandler(() -> userService.count());
    }

    @Operation(summary = "根据ID获取用户名", description = "根据用户ID获取用户名")
    @PreAuthorize("hasAuthority('auth:user:query')")
    @GetMapping("/username")
    public ResultData<String> getUsernameById(
            @Parameter(description = "用户ID") @RequestParam Long userId) {
        return messageHandler(() -> userService.findAccountById(userId).getUsername());
    }

    @Operation(summary = "根据ID获取用户邮箱", description = "根据用户ID获取用户邮箱")
    @PreAuthorize("hasAuthority('auth:user:query')")
    @GetMapping("/email")
    public ResultData<String> getEmailById(
            @Parameter(description = "用户ID") @RequestParam Long userId) {
        return messageHandler(() -> userService.findAccountById(userId).getEmail());
    }

    @Operation(summary = "根据ID获取用户基本信息", description = "根据用户ID获取用户名、邮箱、昵称、头像")
    @PreAuthorize("hasAuthority('auth:user:query')")
    @GetMapping("/info")
    public ResultData<Map<String, Object>> getUserInfoById(
            @Parameter(description = "用户ID") @RequestParam Long userId) {
        UserAccountVO account = userService.findAccountById(userId);
        Map<String, Object> info = new HashMap<>();
        info.put("username", account.getUsername());
        info.put("email", account.getEmail());
        info.put("nickname", account.getNickname());
        info.put("avatar", account.getAvatar());
        return ResultData.success(info);
    }

    @Operation(summary = "根据用户名搜索用户ID列表", description = "根据用户名模糊搜索获取匹配的用户ID列表")
    @PreAuthorize("hasAuthority('auth:user:query')")
    @GetMapping("/search")
    public ResultData<List<Long>> searchUserIdsByUsername(
            @Parameter(description = "用户名") @RequestParam String username) {
        UserSearchDTO searchDTO = new UserSearchDTO();
        searchDTO.setUsername(username);
        return messageHandler(() -> userService.getUserOrSearch(searchDTO)
                .stream().map(UserListVO::getId).collect(Collectors.toList()));
    }
}
