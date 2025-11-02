package com.overthinker.cloud.auth.controller;

import com.overthinker.cloud.auth.service.AuthService;
import com.overthinker.cloud.common.core.base.BaseController;
import com.overthinker.cloud.common.resp.ResultData;
import com.overthinker.cloud.auth.entity.DTO.*;
import com.overthinker.cloud.auth.entity.VO.UserAccountVO;
import com.overthinker.cloud.auth.entity.VO.UserDetailsVO;
import com.overthinker.cloud.auth.entity.VO.UserListVO;

import com.overthinker.cloud.auth.service.UserService;
import com.overthinker.cloud.auth.utils.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@Tag(name = "用户相关接口")
@RequestMapping("/user")
public class UserController extends BaseController {

    @Resource
    private UserService userService;

    @Resource
    private AuthService authService;

    @Operation(summary = "获取当前登录用户信息")
    @GetMapping("/auth/info")
    public ResultData<UserAccountVO> getInfo() {
        return messageHandler(() -> userService.findAccountById(SecurityUtils.getUserId()));
    }

    @Operation(summary = "修改用户信息")
    @PostMapping("/auth/update")
    public ResultData<Void> updateUser(@RequestBody @Valid UserUpdateDTO userUpdateDTO) {
        return userService.updateUser(userUpdateDTO);
    }

    @Operation(summary = "用户头像上传")
    @PostMapping("/auth/upload/avatar")
    public ResultData<String> uploadAvatar(@RequestParam("avatarFile") MultipartFile avatarFile) throws Exception {
        return userService.uploadAvatar(avatarFile);
    }

    @Operation(summary = "修改用户绑定邮箱")
    @PostMapping("/auth/update/email")
    public ResultData<Void> updateEmail(@RequestBody @Valid UpdateEmailDTO updateEmailDTO) {
        return userService.updateEmailAndVerify(updateEmailDTO);
    }

    @Operation(summary = "第三方登录用户绑定邮箱")
    @PostMapping("/auth/third/update/email")
    public ResultData<Void> thirdUpdateEmail(@RequestBody @Valid UpdateEmailDTO updateEmailDTO) {
        return userService.thirdUpdateEmail(updateEmailDTO);
    }

    @Operation(summary = "用户注册")
    @PostMapping("/register")
    public ResultData<Void> register(@RequestBody @Valid UserRegisterDTO userRegisterDTO) {
        return authService.register(userRegisterDTO);
    }

    @Operation(summary = "重置密码-确认邮件")
    @PostMapping("/reset-confirm")
    public ResultData<Void> resetConfirm(@RequestBody @Valid UserResetConfirmDTO userResetDTO) {
        return authService.resetConfirm(userResetDTO);
    }

    @Operation(summary = "重置密码")
    @PostMapping("/reset-password")
    public ResultData<Void> resetPassword(@RequestBody @Valid UserResetPasswordDTO userResetDTO) {
        return authService.resetPassword(userResetDTO);
    }

    @Operation(summary = "获取用户列表")
    @GetMapping("/list")
    public ResultData<List<UserListVO>> getUserList() {
        return messageHandler(() -> userService.getUserOrSearch(null));
    }

    @Operation(summary = "搜索用户列表")
    @PostMapping("/search")
    public ResultData<List<UserListVO>> searchUserList(@RequestBody UserSearchDTO userSearchDTO) {
        return messageHandler(() -> userService.getUserOrSearch(userSearchDTO));
    }

    @Operation(summary = "更新用户状态")
    @PostMapping("/update/status")
    public ResultData<Void> updateStatus(@RequestBody @Valid UpdateRoleStatusDTO updateRoleStatusDTO) {
        return userService.updateStatus(updateRoleStatusDTO.getId(), updateRoleStatusDTO.getStatus());
    }

    @Operation(summary = "获取用户详细信息")
    @GetMapping("/details/{id}")
    public ResultData<UserDetailsVO> getUserDetails(@PathVariable("id") Long id) {
        return messageHandler(() -> userService.findUserDetailsById(id));
    }

    @Operation(summary = "删除用户")
    @DeleteMapping("/delete")
    public ResultData<Void> deleteUser(@RequestBody UserDeleteDTO userDeleteDTO) {
        return userService.deleteUser(userDeleteDTO.getIds());
    }
}
