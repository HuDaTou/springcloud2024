package com.overthinker.cloud.auth.entity.PO;

import com.baomidou.mybatisplus.annotation.TableName;
import com.overthinker.cloud.common.db.BaseData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_role_permission")
public class RolePermission extends BaseData {

    private Long roleId;
    private Long permissionId;
}