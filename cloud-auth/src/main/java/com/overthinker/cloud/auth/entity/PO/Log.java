package com.overthinker.cloud.auth.entity.PO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.overthinker.cloud.common.db.BaseData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("system_log")
public class Log extends BaseData {


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
