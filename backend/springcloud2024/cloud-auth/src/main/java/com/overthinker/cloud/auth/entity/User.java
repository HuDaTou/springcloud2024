package com.overthinker.cloud.web.entity.PO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.overthinker.cloud.entity.BasecopyProperties;
import com.overthinker.cloud.web.entity.PO.base.BaseData;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;


/**
 * (User)表实体类
 *
 * @author overH
 * @since 2023-10-13 15:02:46
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Accessors(chain = true)
@TableName("sys_user")
public class User extends BaseData implements BasecopyProperties {

    // 用户id
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @Schema(description = "用户ID", example = "1234567890123456789")
    private Long id;

    // 用户昵称
    @Schema(description = "用户昵称", example = "张三")
    private String nickname;

    // 用户名
    @Schema(description = "用户名", example = "zhangsan")
    private String username;

    // 用户密码
    @Schema(description = "用户密码", example = "******")
    private String password;

    // 用户性别(0,未定义,1,男,2女)
    @Schema(description = "用户性别：0-未定义 1-男 2-女", allowableValues = {"0", "1", "2"}, example = "1")
    private Integer gender;

    // 用户头像
    @Schema(description = "用户头像URL", example = "https://example.com/avatar.jpg")
    private String avatar;

    // 个人简介
    @Schema(description = "个人简介", example = "热爱编程的技术爱好者")
    private String intro;

    // 用户邮箱
    @Schema(description = "用户邮箱", example = "zhangsan@example.com")
    private String email;

    // 用户注册方式(0邮箱/姓名 1Gitee 2Github)
    @Schema(description = "注册方式：0-邮箱/姓名 1-Gitee 2-Github", allowableValues = {"0", "1", "2"}, example = "0")
    private Integer registerType;

    // 用户注册ip
    @Schema(description = "注册IP", example = "192.168.1.1")
    private String registerIp;

    // 用户注册地址
    @Schema(description = "注册地址", example = "北京市")
    private String registerAddress;

    // 用户登录方式(0邮箱/姓名 1Gitee 2Github)
    @Schema(description = "登录方式：0-邮箱/姓名 1-Gitee 2-Github", allowableValues = {"0", "1", "2"}, example = "0")
    private Integer loginType;

    // 用户登录ip
    @Schema(description = "最后登录IP", example = "192.168.1.2")
    private String loginIp;

    // 登录地址
    @Schema(description = "最后登录地址", example = "上海市")
    private String loginAddress;

    // 是否禁用 0 否 1 是
    @Schema(description = "是否禁用：0-否 1-是", allowableValues = {"0", "1"}, example = "0")
    private Integer isDisable;

    // 用户最近登录时间
    @Schema(description = "最后登录时间", example = "2023-06-01T12:00:00Z")
    private Date loginTime;

    // 是否删除（0：未删除，1：已删除）
    @Schema(description = "是否删除（0：未删除，1：已删除）", example = "0")
    private Integer isDeleted;
}

