package com.overthinker.cloud.web.entity.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LogDTO {

    @Schema(description = "IP地址")
    private String ip;

    @Schema(description = "模块名称")
    private String module;

    @Schema(description = "用户名")
    private String userName;

    @Schema(description = "操作类型")
    private String operation;

    @Schema(description = "状态")
    private Integer state;

    @Schema(description = "开始时间")
    private LocalDateTime logTimeStart;

    @Schema(description = "结束时间")
    private LocalDateTime logTimeEnd;

    @Schema(description = "当前页码")
    private Long current;

    @Schema(description = "每页数量")
    private Long pageSize;
}