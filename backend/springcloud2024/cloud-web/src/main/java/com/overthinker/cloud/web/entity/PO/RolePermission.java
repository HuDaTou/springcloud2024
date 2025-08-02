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

/**
 * (Permission)表实体类
 *
 * @author overH
 * @since 2023-10-13 15:02:40
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("sys_role_permission")
public class RolePermission extends BaseData implements BasecopyProperties {
    // 关系表id
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @Schema(description = "关系ID", example = "1")
    private Integer id;

    // 角色id
//    @TableId
    @Schema(description = "角色ID", example = "1001")
    private Long roleId;

    // 权限id
    @Schema(description = "权限ID", example = "2001")
    private Long permissionId;
}

