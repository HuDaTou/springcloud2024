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
 * (LeaveWord)表实体类
 *
 * @author overH
 * @since 2023-11-03 15:01:10
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("t_leave_word")
public class LeaveWord extends BaseData implements BasecopyProperties {

    // id
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @Schema(description = "留言ID", example = "1234567890123456789")
    private Long id;

    // 留言用户id
    @Schema(description = "留言用户ID", example = "10001")
    private Long userId;

    // 留言内容
    @Schema(description = "留言内容", example = "这个网站真不错！")
    private String content;

    // 是否通过 (0否 1是)
    @Schema(description = "留言审核状态：0-未通过 1-通过", allowableValues = {"0", "1"}, example = "1")
    private Integer isCheck;

    // 是否删除（0：未删除，1：已删除）
    @Schema(description = "是否删除（0：未删除，1：已删除）", example = "0")
    private Integer isDeleted;
}
