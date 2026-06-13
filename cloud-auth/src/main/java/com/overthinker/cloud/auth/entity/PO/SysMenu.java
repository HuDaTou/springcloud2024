package com.overthinker.cloud.auth.entity.PO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.overthinker.cloud.common.core.entity.BasecopyProperties;
import com.overthinker.cloud.common.db.BaseData;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 系统菜单实体类
 * <p>
 * 对应数据库表：auth.sys_menu
 * </p>
 *
 * @author overthinker
 * @since 2025-06-13
 */
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("auth.sys_menu")
public class SysMenu extends BaseData implements BasecopyProperties {

    @Schema(description = "菜单ID，主键", example = "1")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(description = "菜单标题", example = "系统管理")
    private String title;

    @Schema(description = "菜单图标", example = "SettingTwoTone")
    private String icon;

    @Schema(description = "路由路径", example = "/system")
    private String path;

    @Schema(description = "组件路径字符串", example = "RouteView")
    private String component;

    @Schema(description = "重定向路径", example = "/system/menu")
    private String redirect;

    @Schema(description = "是否固定标签页：0-否 1-是", example = "0")
    private Integer affix;

    @Schema(description = "父菜单ID，NULL表示顶级菜单")
    private Long parentId;

    @Schema(description = "路由名称（对应前端 route name）", example = "SystemMenu")
    @TableField("name")
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
