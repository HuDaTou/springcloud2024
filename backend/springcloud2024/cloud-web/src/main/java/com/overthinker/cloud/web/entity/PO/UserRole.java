package com.overthinker.cloud.web.entity.PO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.overthinker.cloud.entity.BasecopyProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * (UserRole)表实体类
 *
 * @author overH
 * @since 2023-11-17 16:33:52
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("sys_user_role")
public class UserRole implements BasecopyProperties {
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

