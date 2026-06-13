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
 * (TreeHole)表实体类
 *
 * @author overH
 * @since 2023-10-30 11:14:13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@TableName("t_tree_hole")
public class TreeHole extends BaseData implements BasecopyProperties {

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @Schema(description = "树洞ID", example = "1234567890123456789")
    private Long id;

    @Schema(description = "发布用户ID", example = "10001")
    private Long userId;

    @Schema(description = "树洞内容", example = "今天天气真好！")
    private String content;

    @Schema(description = "审核状态：0-未通过 1-通过", allowableValues = {"0", "1"}, example = "1")
    private Integer isCheck;
}
