package com.overthinker.cloud.web.entity.PO;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.overthinker.cloud.common.entity.BasecopyProperties;
import com.overthinker.cloud.common.entity.PO.BaseData;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * (Permission)表实体类
 *
 * @author overH
 * @since 2023-12-05 19:55:11
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_permission")
public class Permission extends BaseData implements BasecopyProperties {
    // 权限表id
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @Schema(description = "权限ID", example = "1")
    private Integer id;

    // 描述
    @Schema(description = "权限描述", example = "用户管理查看权限")
    private String permissionDesc;

    // 权限字符
    @Schema(description = "权限标识", example = "system:user:view")
    private String permissionKey;

    // 菜单id
    @Schema(description = "关联菜单ID", example = "1001")
    private Long menuId;

    // 是否删除（0：未删除，1：已删除）
    @Schema(description = "是否删除（0：未删除，1：已删除）", example = "0")
    private Integer isDeleted;
}

