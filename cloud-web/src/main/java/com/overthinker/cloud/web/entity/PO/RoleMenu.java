package com.overthinker.cloud.web.entity.PO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.overthinker.cloud.common.core.entity.BasecopyProperties;
import com.overthinker.cloud.common.db.BaseData;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * (RoleMenu)表实体类
 *
 * @author overH
 * @since 2023-11-28 10:23:15
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_role_menu")
public class RoleMenu extends BaseData implements BasecopyProperties {

    // 主键
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @Schema(description = "关联ID", example = "1")
    private Integer id;

    // 角色id
    @Schema(description = "角色ID", example = "1001")
    private Long roleId;

    // 菜单id
    @Schema(description = "菜单ID", example = "2001")
    private Long menuId;

    public RoleMenu(Long roleId, Long menuId) {
        this.roleId = roleId;
        this.menuId = menuId;
    }
}
