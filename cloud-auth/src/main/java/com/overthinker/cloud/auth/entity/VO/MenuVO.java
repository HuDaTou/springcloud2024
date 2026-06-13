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
 * 菜单视图对象
 * <p>
 * 返回给前端用于动态生成路由和菜单
 * </p>
 *
 * @author overthinker
 * @since 2025-06-13
 */
@Data
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MenuVO {

    @Schema(description = "路由名称", example = "Dashboard")
    private String name;

    @Schema(description = "路由路径", example = "/dashboard")
    private String path;

    @Schema(description = "组件路径字符串", example = "BasicLayout")
    private String component;

    @Schema(description = "路由元信息")
    private MenuMetaVO meta;

    @Schema(description = "子菜单/子路由")
    private List<MenuVO> children;

    /**
     * 路由元信息
     */
    @Data
    @Accessors(chain = true)
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class MenuMetaVO {

        @Schema(description = "菜单标题", example = "仪表盘")
        private String title;

        @Schema(description = "菜单图标", example = "lucide:layout-dashboard")
        private String icon;

        @Schema(description = "排序号", example = "1")
        private Integer order;

        @Schema(description = "是否在菜单中隐藏", example = "false")
        private Boolean hideInMenu;

        @Schema(description = "所需权限标识", example = "[\"dashboard:view\"]")
        private List<String> authority;

        @Schema(description = "是否固定标签页", example = "false")
        private Boolean affixTab;

        @Schema(description = "是否开启缓存", example = "false")
        private Boolean keepAlive;
    }
}
