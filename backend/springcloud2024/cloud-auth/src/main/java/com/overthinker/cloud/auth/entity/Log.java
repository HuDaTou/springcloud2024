package com.overthinker.cloud.logconsumer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("system_log")
public class Log implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String module;
    private String operation;
    private String userId;
    private String ip;
    private String requestUri;
    private String requestMethod;
    private String parameters;
    private Long executionTime;
    private String status;
    private String errorMsg;
    private LocalDateTime createdAt;
}
