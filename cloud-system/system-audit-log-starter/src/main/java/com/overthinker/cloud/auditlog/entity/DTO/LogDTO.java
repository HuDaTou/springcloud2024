package com.overthinker.cloud.auditlog.entity.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author overH
 * <p>
 * 创建时间：2023/12/13 15:19
 */
public record LogDTO(
    //ip地址
    String ip,
    //模块名称
    String module,
    //操作人员
    String userName,
    //用户id
    String userId,
    //操作类型
    String operation,
    //操作状态(0：成功，1：失败)
    Integer state,
    LocalDateTime logTimeStart,
    LocalDateTime logTimeEnd,
    // 当前页
    @NotNull
    Long current,

    // 每页数量
    @NotNull
    Long pageSize
) {}
