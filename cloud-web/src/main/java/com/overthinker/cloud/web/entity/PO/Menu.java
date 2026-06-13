package com.overthinker.cloud.web.entity.PO;

import com.baomidou.mybatisplus.annotation.IdType;
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
 * (Menu)表实体类
 *
 * @author overH
 * @since 2023-11-17 22:15:18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@TableName("sys_menu")
public class Menu extends BaseData implements BasecopyProperties {

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @Schema(description = "菜单ID", example = "1234567890123456789")
    private Long id;

    @Schema(description = "菜单标题", example = "系统管理")
    private String title;

    @Schema(description = "菜单图标", example = "icon-system")
    private String icon;

    @Schema(description = "路由路径", example = "/system")
    private String path;

    @Schema(description = "绑定组件类型", example = "RouteView")
    private String component;

    @Schema(description = "父菜单重定向地址", example = "/system/user")
    private String redirect;

    @Schema(description = "是否固定页签：0-否 1-是", allowableValues = {"0", "1"}, example = "0")
    private Integer affix;

    @Schema(description = "父菜单ID，顶级菜单为null", example = "0")
    private Long parentId;

    @Schema(description = "路由名称", example = "SystemManage")
    private String name;

    @Schema(description = "是否隐藏菜单：0-否 1-是", allowableValues = {"0", "1"}, example = "0")
    private Integer hideInMenu;

    @Schema(description = "iframe跳转URL", example = "https://example.com")
    private String url;

    @Schema(description = "是否显示在面包屑中：0-否 1-是", allowableValues = {"0", "1"}, example = "1")
    private Integer hideInBreadcrumb;

    @Schema(description = "是否隐藏子菜单：0-否 1-是", allowableValues = {"0", "1"}, example = "0")
    private Integer hideChildrenInMenu;

    @Schema(description = "是否缓存组件：0-否 1-是", allowableValues = {"0", "1"}, example = "1")
    private Integer keepAlive;

    @Schema(description = "跳转方式", allowableValues = {"_blank", "_self", "_parent"}, example = "_self")
    private String target;

    @Schema(description = "是否禁用：0-否 1-是", allowableValues = {"0", "1"}, example = "0")
    private Integer isDisable;

    @Schema(description = "菜单排序", example = "1")
    private Integer orderNum;
}
