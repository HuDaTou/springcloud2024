package com.overthinker.cloud.web.entity.PO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.overthinker.cloud.common.entity.BasecopyProperties;
import com.overthinker.cloud.common.entity.PO.BaseData;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * (TreeHole)表实体类
 *
 * @author overH
 * @since 2023-10-30 11:14:13
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("t_tree_hole")
public class TreeHole extends BaseData implements BasecopyProperties {

    // 树洞表id
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @Schema(description = "树洞ID", example = "1234567890123456789")
    private Long id;

    // 用户id
    @Schema(description = "发布用户ID", example = "10001")
    private Long userId;

    // 内容
    @Schema(description = "树洞内容", example = "今天天气真好！")
    private String content;

    // 是否通过
    @Schema(description = "审核状态：0-未通过 1-通过", allowableValues = {"0", "1"}, example = "1")
    private Integer isCheck;

    // 是否删除（0：未删除，1：已删除）
    @Schema(description = "是否删除（0：未删除，1：已删除）", example = "0")
    private Integer isDeleted;
}
