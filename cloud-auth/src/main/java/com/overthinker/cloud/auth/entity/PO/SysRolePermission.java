package com.overthinker.cloud.auth.entity.PO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.overthinker.cloud.common.db.BaseData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;



/**
 * 角色权限关系表
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_role_permission")
public class SysRolePermission extends BaseData  {


    /**
     * 关系表id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 角色id
     */
    private Long roleId;

    /**
     * 权限id
     */
    private Long permissionId;

    /**
     * 权限表
     */
    @EqualsAndHashCode(callSuper = true)
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @TableName("sys_permission")
    public static class SysPermission extends BaseData {



        /**
         * 权限表id
         */
        @TableId(value = "id", type = IdType.AUTO)
        private Long id;

        /**
         * 描述
         */
        private String permissionDesc;

        /**
         * 权限字符
         */
        private String permissionKey;

        /**
         * 菜单id
         */
        private Long menuId;


    }
}
