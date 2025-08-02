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
 * (Log)表实体类
 *
 * @author overH
 * @since 2023-12-12 09:12:31
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName(value = "sys_log")
public class Log extends BaseData implements Serializable, BasecopyProperties {

    @Serial
    private static final long serialVersionUID = 1L;

    // 编号
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @Schema(description = "操作日志ID", example = "1234567890123456789")
    private Long id;

    // 模块名称
    @Schema(description = "操作模块", example = "用户管理")
    private String module;

    // 操作
    @Schema(description = "操作类型", example = "登录")
    private String operation;

    // 操作人员
    @Schema(description = "操作人用户名", example = "admin")
    private String userName;

    // ip地址
    @Schema(description = "操作IP地址", example = "192.168.1.1")
    private String ip;

    // 操作地点
    @Schema(description = "操作地点", example = "北京市")
    private String address;

    // 操作状态(0：成功，1：失败，2：异常)
    @Schema(description = "操作状态：0-成功 1-失败 2-异常", allowableValues = {"0", "1", "2"}, example = "0")
    private Integer state;

    // 操作方法
    @Schema(description = "操作方法", example = "UserController.login")
    private String method;

    // 请求方式
    @Schema(description = "HTTP请求方式", example = "POST")
    private String reqMapping;

    // 请求参数
    @Schema(description = "请求参数", example = "{\"username\":\"admin\",\"password\":\"123456\"}")
    private String reqParameter;

    // 异常信息
    @Schema(description = "异常信息，无异常时为空", example = "NullPointerException")
    private String exception;

    // 返回参数
    @Schema(description = "返回结果", example = "{\"code\":200,\"message\":\"操作成功\"}")
    private String returnParameter;

    // 请求地址
    @Schema(description = "请求URL", example = "http://localhost:8080/api/login")
    private String reqAddress;

    // 消耗时间(ms)
    @Schema(description = "操作耗时(毫秒)", example = "500")
    private Long time;

    // 操作描述
    @Schema(description = "操作描述", example = "用户登录系统")
    private String description;

    // 是否删除（0：未删除，1：已删除）
    @Schema(description = "是否删除（0：未删除，1：已删除）", example = "0")
    private Integer isDeleted;
}

