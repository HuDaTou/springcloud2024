package com.overthinker.cloud.auth.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户表
 */
@Data
@TableName("sys_user")
public class SysUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户性别(0,未定义,1,男,2女)
     */
    private Integer gender;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 个人简介
     */
    private String intro;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 注册ip
     */
    private String registerIp;

    /**
     * 注册方式(0邮箱/姓名 1Gitee 2Github)
     */
    private Integer registerType;

    /**
     * 注册地址
     */
    private String registerAddress;

    /**
     * 最近登录ip
     */
    private String loginIp;

    /**
     * 最近登录地址
     */
    private String loginAddress;

    /**
     * 最近登录类型(0邮箱/姓名 1Gitee 2Github)
     */
    private Integer loginType;

    /**
     * 用户最近登录时间
     */
    private LocalDateTime loginTime;

    /**
     * 是否禁用 (0否 1是)
     */
    private Integer isDisable;

    /**
     * 用户创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 用户更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 是否删除（0：未删除，1：已删除）
     */
    @TableLogic
    private Integer isDeleted;
}
