package com.overthinker.cloud.web.entity.PO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.overthinker.cloud.common.core.entity.BasecopyProperties;
import com.overthinker.cloud.common.db.BaseData;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;


/**
 * (LoginLog)表实体类
 *
 * @author overH
 * @since 2023-12-08 14:38:43
 */
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@TableName("sys_login_log")
public class LoginLog extends BaseData implements Serializable, BasecopyProperties {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @Schema(description = "登录日志ID", example = "1234567890123456789")
    private Long id;

    @Schema(description = "登录用户名", example = "admin")
    private String userName;

    @Schema(description = "登录IP地址", example = "192.168.1.1")
    private String ip;

    @Schema(description = "登录地理位置", example = "北京市")
    private String address;

    @Schema(description = "浏览器信息", example = "Chrome 114.0")
    private String browser;

    @Schema(description = "操作系统信息", example = "Windows 10")
    private String os;

    @Schema(description = "登录类型：0-前台 1-后台 2-非法登录", allowableValues = {"0", "1", "2"}, example = "1")
    private Integer type;

    @Schema(description = "登录状态：0-成功 1-失败", allowableValues = {"0", "1"}, example = "0")
    private Integer state;

    @Schema(description = "登录详情信息", example = "登录成功")
    private String message;
}
