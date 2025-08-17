package com.overthinker.cloud.auth.entity.PO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 系统权限表的实体。
 */
@Data
@TableName("sys_permission")
public class SysPermission implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 权限类别，例如 "用户管理"
     */
    private String category;

    /**
     * 权限名称，例如 "创建用户"
     */
    private String name;

    /**
     * HTTP请求方法，例如 "GET", "POST"
     */
    private String httpMethod;

    /**
     * 权限的完整请求路径
     */
    private String path;
}
