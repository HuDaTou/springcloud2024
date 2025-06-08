package com.overthinker.cloud.web.entity.PO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.overthinker.cloud.entity.BasecopyProperties;
import com.overthinker.cloud.web.entity.PO.base.BaseData;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * (Link)表实体类
 *
 * @author overH
 * @since 2023-11-14 08:43:34
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("t_link")
public class Link extends BaseData implements BasecopyProperties {
    // 友链表id
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @Schema(description = "友链ID", example = "1234567890123456789")
    private Long id;

    // 用户id
    @Schema(description = "提交用户ID", example = "10001")
    private Long userId;

    // 网站名称
    @Schema(description = "网站名称", example = "示例网站")
    private String name;

    // 网站地址
    @Schema(description = "网站URL", example = "https://example.com")
    private String url;

    // 网站描述
    @Schema(description = "网站描述", example = "这是一个示例网站")
    private String description;

    // 网站背景
    @Schema(description = "网站背景图URL", example = "https://example.com/background.jpg")
    private String background;

    // 邮箱地址
    @Schema(description = "联系邮箱", example = "contact@example.com")
    private String email;

    // 审核状态（0：未通过，1：已通过）
    @Schema(description = "审核状态：0-未通过 1-已通过", allowableValues = {"0", "1"}, example = "1")
    private Integer isCheck;

    // 是否删除（0：未删除，1：已删除）
    @Schema(description = "是否删除（0：未删除，1：已删除）", example = "0")
    private Integer isDeleted;
}
