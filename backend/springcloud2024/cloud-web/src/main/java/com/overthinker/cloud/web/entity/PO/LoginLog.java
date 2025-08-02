package com.overthinker.cloud.web.entity.PO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.overthinker.cloud.entity.BasecopyProperties;
import com.overthinker.cloud.entity.PO.BaseData;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;


/**
 * (LoginLog)表实体类
 *
 * @author overH
 * @since 2023-12-08 14:38:43
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_login_log")
public class LoginLog extends BaseData implements Serializable, BasecopyProperties {

    @Serial
    private static final long serialVersionUID = 1L;

    // 日志编号
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @Schema(description = "登录日志ID", example = "1234567890123456789")
    private Long id;

    // 用户名称
    @Schema(description = "登录用户名", example = "admin")
    private String userName;

    // 登录ip
    @Schema(description = "登录IP地址", example = "192.168.1.1")
    private String ip;

    // 登录地址
    @Schema(description = "登录地理位置", example = "北京市")
    private String address;

    // 浏览器
    @Schema(description = "浏览器信息", example = "Chrome 114.0")
    private String browser;

    // 操作系统
    @Schema(description = "操作系统信息", example = "Windows 10")
    private String os;

    // 登录类型(0：前台，1：后台，2：非法登录)
    @Schema(description = "登录类型：0-前台 1-后台 2-非法登录", allowableValues = {"0", "1", "2"}, example = "1")
    private Integer type;

    // 登录状态(0：成功，1：失败)
    @Schema(description = "登录状态：0-成功 1-失败", allowableValues = {"0", "1"}, example = "0")
    private Integer state;

    // 登录信息
    @Schema(description = "登录详情信息", example = "登录成功")
    private String message;

    // 是否删除（0：未删除，1：已删除）
    @Schema(description = "是否删除（0：未删除，1：已删除）", example = "0")
    private Integer isDeleted;
}

