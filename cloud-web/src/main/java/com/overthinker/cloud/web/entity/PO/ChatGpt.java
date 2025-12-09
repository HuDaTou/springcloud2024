package com.overthinker.cloud.web.entity.PO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.overthinker.cloud.common.core.entity.BasecopyProperties;
import com.overthinker.cloud.common.db.BaseData;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * (ChatGpt)表实体类
 *
 * @author overH
 * @since 2023-11-11 12:01:59
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("t_chat_gpt")
public class ChatGpt extends BaseData implements BasecopyProperties {
    // id
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @Schema(description = "会话记录ID", example = "1234567890123456789")
    private Long id;

    // 用户id
    @Schema(description = "用户ID", example = "10001")
    private Long userId;

    // 会话记录
    @Schema(description = "会话内容", example = "用户：你好 客服：您好，有什么可以帮助您的？")
    private String conversation;

    // 是否有效
    @Schema(description = "会话是否有效（0：无效，1：有效）", example = "1")
    private Integer isCheck;

    // 是否删除（0：未删除，1：已删除）
    @Schema(description = "是否删除（0：未删除，1：已删除）", example = "0")
    private Integer isDeleted;
}

