package com.overthinker.cloud.auth.entity.PO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import com.overthinker.cloud.common.core.entity.BasecopyProperties;
import com.overthinker.cloud.common.db.BaseData;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;


/**
 * (UserRole)表实体类
 *
 * @author overH
 * @since 2023-11-17 16:33:52
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@TableName("sys_user_role")
public class UserRole extends BaseData implements BasecopyProperties {
    // 主键
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @Schema(description = "关联ID", example = "1234567890123456789")
    private Long id;

    // 用户id
    @Schema(description = "用户ID", example = "10001")
    private Long userId;

    // 角色id
    @Schema(description = "角色ID", example = "2001")
    private Long roleId;
}