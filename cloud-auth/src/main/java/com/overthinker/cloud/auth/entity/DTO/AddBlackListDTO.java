package com.overthinker.cloud.auth.entity.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Date;
import java.util.List;

@Schema(description = "添加黑名单请求体")
public record AddBlackListDTO(

    @Schema(description = "用户Id列表")
    @Size(min = 1, message = "用户Id不能为空")
    List<Long> userIds,

    @Schema(description = "封禁理由")
    @NotBlank(message = "封禁理由不能为空")
    String reason,

    @Schema(description = "封禁到期时间")
    @NotNull(message = "封禁到期时间不能为空")
    @Future(message = "封禁到期时间必须大于当前时间")
    Date expiresTime
) {}