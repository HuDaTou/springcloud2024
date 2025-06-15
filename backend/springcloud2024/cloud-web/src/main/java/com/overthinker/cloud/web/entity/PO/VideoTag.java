package com.overthinker.cloud.web.entity.PO;

import com.baomidou.mybatisplus.annotation.*;
import com.overthinker.cloud.entity.BasecopyProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("t_video_tag")
public class VideoTag implements BasecopyProperties {
    // 关系表id
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @Schema(description = "关联ID", example = "1234567890123456789")
    private Long id;

    // 文章id
//    @TableId
    @Schema(description = "视频ID", example = "10001")
    private Long videoId;

    // 标签id
    @Schema(description = "标签ID", example = "2001")
    private Long tagId;

    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建时间", example = "2023-06-01T12:00:00Z")
    private Date createTime;

    // 是否删除（0：未删除，1：已删除）
    @Schema(description = "是否删除（0：未删除，1：已删除）", example = "0")
    private Integer isDeleted;
}
