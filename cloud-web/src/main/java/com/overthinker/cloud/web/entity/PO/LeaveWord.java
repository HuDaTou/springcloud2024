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
 * (LeaveWord)表实体类
 *
 * @author overH
 * @since 2023-11-03 15:01:10
 */

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@TableName("t_leave_word")
public class LeaveWord extends BaseData implements BasecopyProperties {

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @Schema(description = "留言ID", example = "1234567890123456789")
    private Long id;

    @Schema(description = "留言用户ID", example = "10001")
    private Long userId;

    @Schema(description = "留言内容", example = "这个网站真不错！")
    private String content;

    @Schema(description = "留言审核状态：0-未通过 1-通过", allowableValues = {"0", "1"}, example = "1")
    private Integer isCheck;
}
