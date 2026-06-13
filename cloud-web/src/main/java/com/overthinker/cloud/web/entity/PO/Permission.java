package com.overthinker.cloud.web.entity.PO;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.overthinker.cloud.common.core.entity.BasecopyProperties;
import com.overthinker.cloud.common.db.BaseData;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;


/**
 * (Permission)表实体类
 *
 * @author overH
 * @since 2023-12-05 19:55:11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@TableName("sys_permission")
public class Permission extends BaseData implements BasecopyProperties {
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @Schema(description = "权限ID", example = "1")
    private Integer id;

    @Schema(description = "权限描述", example = "用户管理查看权限")
    private String permissionDesc;

    @Schema(description = "权限标识", example = "system:user:view")
    private String permissionKey;

    @Schema(description = "关联菜单ID", example = "1001")
    private Long menuId;
}
