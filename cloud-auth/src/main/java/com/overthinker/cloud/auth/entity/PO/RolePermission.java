package com.overthinker.cloud.auth.entity.PO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.overthinker.cloud.common.core.entity.BasecopyProperties;
import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
@TableName("sys_role_permission")
public class RolePermission implements BasecopyProperties {
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    private Long roleId;

    private Long permissionId;

}