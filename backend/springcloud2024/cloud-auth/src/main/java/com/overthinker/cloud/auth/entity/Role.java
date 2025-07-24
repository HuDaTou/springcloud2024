package com.overthinker.cloud.web.entity.PO;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.overthinker.cloud.entity.BasecopyProperties;
import com.overthinker.cloud.web.entity.PO.base.BaseData;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;


/**
 * (Role)表实体类
 *
 * @author overH
 * @since 2023-10-13 15:02:40
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@TableName("sys_role")
public class Role extends BaseData implements BasecopyProperties {

    // 角色id
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @Schema(description = "角色ID", example = "1234567890123456789")
    private Long id;

    // 角色名称
    @Schema(description = "角色名称", example = "管理员")
    private String roleName;

    // 角色字符
    @Schema(description = "角色标识", example = "admin")
    private String roleKey;

    // 是否删除（0：未删除，1：已删除）
    @Schema(description = "是否删除（0：未删除，1：已删除）", example = "0")
    private Integer isDeleted;

    // 状态（0：正常，1：停用）
    @Schema(description = "角色状态：0-正常 1-停用", allowableValues = {"0", "1"}, example = "0")
    private Integer status;

    // 顺序
    @Schema(description = "角色排序", example = "1")
    private Long orderNum;

    // 备注
    @Schema(description = "角色备注", example = "系统管理员")
    private String remark;
}

