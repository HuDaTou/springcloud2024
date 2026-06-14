package com.overthinker.cloud.auth.entity.VO;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 权限树视图对象
 * <p>
 * 按分类分组的权限树结构，用于前端树形选择组件（如角色分配权限）
 * </p>
 *
 * @author overthinker
 * @since 2025-08-02
 */
@Data
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "权限树节点")
public class PermissionTreeVO {

    @Schema(description = "节点显示名称（分类名或权限名）", example = "用户管理")
    private String label;

    @Schema(description = "节点值（分类名或权限标识码）", example = "system:user:list")
    private String value;

    @Schema(description = "权限ID（仅叶子节点有值）", example = "1")
    private Long id;

    @Schema(description = "子节点列表")
    private List<PermissionTreeVO> children;
}
