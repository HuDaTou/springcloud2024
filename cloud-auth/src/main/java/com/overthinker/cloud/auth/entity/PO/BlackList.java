package com.overthinker.cloud.auth.entity.PO;


import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;

import com.overthinker.cloud.auth.entity.ip.BlackListIpInfo;
import com.overthinker.cloud.common.entity.BasecopyProperties;

import com.overthinker.cloud.common.entity.PO.BaseData;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


/**
 * (BlackList)表实体类
 *
 * @author overH
 * @since 2024-09-05 16:13:19
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "t_black_list", autoResultMap = true)
public class BlackList extends BaseData implements BasecopyProperties {
    // 表id
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @Schema(description = "黑名单记录ID", example = "1234567890123456789")
    private Long id;

    // 用户id
    @Schema(description = "用户ID", example = "10001")
    private Long userId;

    // 封禁理由
    @Schema(description = "封禁原因描述", example = "恶意攻击行为")
    private String reason;

    // 封禁时间
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "封禁开始时间", example = "2023-06-01T12:00:00Z")
    private Date bannedTime;

    // 到期时间
    @Schema(description = "封禁到期时间，null表示永久封禁", example = "2023-07-01T12:00:00Z")
    private Date expiresTime;

    // 类型（1：用户，2：路人/攻击者）
    @Schema(description = "黑名单类型：1-用户 2-路人/攻击者", allowableValues = {"1", "2"}, example = "1")
    private Integer type;

    // ip信息，如果type=2，则需要有ip信息
    @TableField(value = "ip_info", typeHandler = JacksonTypeHandler.class)
    @Schema(description = "IP相关信息，type=2时必填", implementation = BlackListIpInfo.class)
    private BlackListIpInfo ipInfo;

    // 是否删除（0：未删除，1：已删除）默认：0
    @Schema(description = "逻辑删除标记", allowableValues = {"0", "1"}, example = "0")
    private Integer isDeleted;
}

