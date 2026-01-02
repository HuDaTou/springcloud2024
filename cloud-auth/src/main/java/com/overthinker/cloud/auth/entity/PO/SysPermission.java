package com.overthinker.cloud.auth.entity.PO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.ser.Serializers;
import com.overthinker.cloud.common.core.entity.BasecopyProperties;
import com.overthinker.cloud.common.db.BaseData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 系统权限表的实体。
 */
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_permission")
public class SysPermission extends BaseData implements BasecopyProperties {


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

//    权限代码
    private  String permissonCode;
}
