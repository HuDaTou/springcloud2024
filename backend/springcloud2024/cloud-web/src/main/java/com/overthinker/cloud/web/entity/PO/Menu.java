package com.overthinker.cloud.web.entity.PO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.overthinker.cloud.common.entity.BasecopyProperties;
import com.overthinker.cloud.common.entity.PO.BaseData;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * (Menu)表实体类
 *
 * @author overH
 * @since 2023-11-17 22:15:18
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_menu")
public class Menu extends BaseData implements BasecopyProperties {

    // 唯一id
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @Schema(description = "菜单ID", example = "1234567890123456789")
    private Long id;

    // 标题
    @Schema(description = "菜单标题", example = "系统管理")
    private String title;

    // 图标
    @Schema(description = "菜单图标", example = "icon-system")
    private String icon;

    // 地址
    @Schema(description = "路由路径", example = "/system")
    private String path;

    // 绑定的哪个组件，默认自带的组件类型分别是：Iframe、RouteView和ComponentError
    @Schema(description = "绑定组件类型", example = "RouteView")
    private String component;

    // 父菜单重定向地址(默认第一个子菜单)
    @Schema(description = "父菜单重定向地址", example = "/system/user")
    private String redirect;

    // 是否是固定页签(0否 1是)
    @Schema(description = "是否固定页签：0-否 1-是", allowableValues = {"0", "1"}, example = "0")
    private Integer affix;

    // 父级菜单的id
    @Schema(description = "父菜单ID，顶级菜单为null", example = "0")
    private Long parentId;

    // 同路由中的name，主要是用于保活的左右
    @Schema(description = "路由名称", example = "SystemManage")
    private String name;

    // 是否隐藏当前菜单(0否 1是)
    @Schema(description = "是否隐藏菜单：0-否 1-是", allowableValues = {"0", "1"}, example = "0")
    private Integer hideInMenu;

    // 如果当前是iframe的模式，需要有一个跳转的url支撑，其不能和path重复，path还是为路由
    @Schema(description = "iframe跳转URL", example = "https://example.com")
    private String url;

    // 是否存在于面包屑(0否 1是)
    @Schema(description = "是否显示在面包屑中：0-否 1-是", allowableValues = {"0", "1"}, example = "1")
    private Integer hideInBreadcrumb;

    // 是否需要显示所有的子菜单(0否 1是)
    @Schema(description = "是否隐藏子菜单：0-否 1-是", allowableValues = {"0", "1"}, example = "0")
    private Integer hideChildrenInMenu;

    // 是否保活(0否 1是)
    @Schema(description = "是否缓存组件：0-否 1-是", allowableValues = {"0", "1"}, example = "1")
    private Integer keepAlive;

    // 全连接跳转模式('_blank' | '_self' | '_parent')
    @Schema(description = "跳转方式", allowableValues = {"_blank", "_self", "_parent"}, example = "_self")
    private String target;

    // 是否禁用 (0否 1是)
    @Schema(description = "是否禁用：0-否 1-是", allowableValues = {"0", "1"}, example = "0")
    private Integer isDisable;

    // 排序
    @Schema(description = "菜单排序", example = "1")
    private Integer orderNum;

    // 是否删除（0：未删除，1：已删除）
    @Schema(description = "是否删除（0：未删除，1：已删除）", example = "0")
    private Integer isDeleted;
}
