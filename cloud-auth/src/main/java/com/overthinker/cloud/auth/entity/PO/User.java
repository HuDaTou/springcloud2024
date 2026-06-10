package com.overthinker.cloud.auth.entity.PO;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.overthinker.cloud.common.core.entity.BasecopyProperties;
import com.overthinker.cloud.common.db.BaseData;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * (User)表实体类
 *
 * @author overH
 * @since 2023-10-13 15:02:46
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@TableName("sys_user")
public class User extends BaseData implements BasecopyProperties {

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @Schema(description = "用户ID", example = "1234567890123456789")
    private Long id;

    @Schema(description = "用户昵称", example = "张三")
    private String nickname;

    @Schema(description = "用户名", example = "zhangsan")
    private String username;

    @Schema(description = "用户密码", example = "******")
    private String password;

    @Schema(description = "用户性别：0-未定义 1-男 2-女", allowableValues = {"0", "1", "2"}, example = "1")
    private Integer gender;

    @Schema(description = "用户头像URL", example = "https://example.com/avatar.jpg")
    private String avatar;

    @Schema(description = "个人简介", example = "热爱编程的技术爱好者")
    private String intro;

    @Schema(description = "用户邮箱", example = "zhangsan@example.com")
    private String email;

    @Schema(description = "注册方式：0-邮箱/姓名 1-Gitee 2-Github", allowableValues = {"0", "1", "2"}, example = "0")
    private Integer registerType;

    @Schema(description = "注册IP", example = "192.168.1.1")
    private String registerIp;

    @Schema(description = "注册地址", example = "北京市")
    private String registerAddress;

    @Schema(description = "登录方式：0-邮箱/姓名 1-Gitee 2-Github", allowableValues = {"0", "1", "2"}, example = "0")
    private Integer loginType;

    @Schema(description = "最后登录IP", example = "192.168.1.2")
    private String loginIp;

    @Schema(description = "最后登录地址", example = "上海市")
    private String loginAddress;

    @Schema(description = "是否禁用：false-否 true-是", allowableValues = {"false", "true"}, example = "false")
    private Boolean isDisable;

    @Schema(description = "最后登录时间", example = "2023-06-01T12:00:00Z")
    private LocalDateTime loginTime;
}