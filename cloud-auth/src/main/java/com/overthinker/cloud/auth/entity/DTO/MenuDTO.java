package com.overthinker.cloud.auth.entity.DTO;

import com.overthinker.cloud.common.core.entity.BasecopyProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 菜单创建/更新请求DTO
 * <p>
 * 用于接收前端传递的菜单参数
 * </p>
 *
 * @author overthinker
 * @since 2025-06-13
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Schema(description = "菜单创建/更新请求")
public class MenuDTO implements BasecopyProperties {

    @NotBlank(message = "菜单名称不能为空")
    @Schema(description = "菜单标题", example = "系统管理", requiredMode = Schema.RequiredMode.REQUIRED)
    private String title;

    @Schema(description = "菜单图标", example = "lucide:setting")
    private String icon;

    @Schema(description = "路由路径", example = "/system")
    private String path;

    @Schema(description = "组件路径字符串", example = "system/menu/list")
    private String component;

    @Schema(description = "重定向路径", example = "/system/menu")
    private String redirect;

    @Schema(description = "是否固定标签页：0-否 1-是", example = "0")
    private Integer affix;

    @Schema(description = "父菜单ID，NULL或空字符串表示顶级菜单")
    private Long parentId;

    @Schema(description = "路由名称（对应前端 route name）", example = "SystemMenu")
    private String name;

    @Schema(description = "是否在菜单中隐藏：0-显示 1-隐藏", example = "0")
    private Integer hideInMenu;

    @Schema(description = "外链URL", example = "https://example.com")
    private String url;

    @Schema(description = "是否在面包屑中隐藏：0-显示 1-隐藏", example = "0")
    private Integer hideInBreadcrumb;

    @Schema(description = "是否隐藏子菜单：0-显示 1-隐藏", example = "0")
    private Integer hideChildrenInMenu;

    @Schema(description = "是否开启KeepAlive缓存：0-否 1-是", example = "1")
    private Integer keepAlive;

    @Schema(description = "打开方式：_self _blank", example = "_self")
    private String target;

    @Schema(description = "是否禁用", example = "false")
    private Boolean isDisable;

    @Schema(description = "排序号，越小越靠前", example = "1")
    private Integer orderNum;
}