package com.overthinker.cloud.auth.entity.PO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * Entity for the system permission table.
 */
@Data
@TableName("sys_permission")
public class SysPermission implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * Permission category, e.g., "User Management"
     */
    private String category;

    /**
     * Permission name, e.g., "Create User"
     */
    private String name;

    /**
     * HTTP request method, e.g., "GET", "POST"
     */
    private String httpMethod;

    /**
     * The full request path for the permission
     */
    private String path;
}
