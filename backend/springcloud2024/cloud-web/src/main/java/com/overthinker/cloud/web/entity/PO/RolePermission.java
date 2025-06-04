package com.overthinker.cloud.web.entity.PO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.overthinker.cloud.entity.BaseData;
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
public class RolePermission implements BaseData {
    //关系表id
    @TableId(value = "id" ,type = IdType.ASSIGN_ID)
    private Integer id;
    //角色id
    @TableId
    private Long roleId;
    //权限id
    private Long permissionId;
}

