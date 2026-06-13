package com.overthinker.cloud.auth.entity.PO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.overthinker.cloud.common.core.entity.BasecopyProperties;
import com.overthinker.cloud.common.db.BaseData;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Schema(description = "系统权限实体")
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Data
@TableName("sys_permission")
public class SysPermission extends BaseData implements BasecopyProperties {

    @Schema(description = "权限ID，主键", example = "1")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(description = "菜单ID", example = "2")
    private Long menuId;

    @Schema(description = "权限类别", example = "用户管理")
    private String category;

    @Schema(description = "权限名称", example = "查询菜单")
    @TableField("permission_desc")
    private String name;

    @Schema(description = "权限代码", example = "system:menu:list")
    @TableField("permission_key")
    private String permissonCode;

    @Schema(description = "HTTP请求方法", example = "GET")
    private String httpMethod;

    @Schema(description = "权限的完整请求路径", example = "/api/menu/list")
    private String path;

    @Schema(description = "所属服务名称", example = "cloud-auth")
    private String serviceName;
}
