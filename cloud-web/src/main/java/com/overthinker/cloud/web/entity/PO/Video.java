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


@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@TableName("t_video")
public class Video extends BaseData implements BasecopyProperties {
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @Schema(description = "视频ID", example = "1234567890123456789")
    private Long id;

    @Schema(description = "上传用户ID", example = "10001")
    private Long userId;

    @Schema(description = "分类ID", example = "2001")
    private Long categoryId;

    @Schema(description = "视频封面URL", example = "https://example.com/cover.jpg")
    private String videoCover;

    @Schema(description = "视频文件URL", example = "https://example.com/video.mp4")
    private String video;

    @Schema(description = "视频标题", example = "Java编程教程")
    private String videoTitle;

    @Schema(description = "视频描述", example = "这是一个Java基础入门教程")
    private String videoDescription;

    @Schema(description = "视频格式", example = "mp4")
    private String videoType;

    @Schema(description = "权限设置：true-公开 false-私有", example = "true")
    private Boolean permission;

    @Schema(description = "状态：true-已发布 false-草稿", example = "true")
    private Boolean status;

    @Schema(description = "视频大小", example = "1024MB")
    private String videoSize;

    @Schema(description = "视频访问量", example = "1000")
    private Long videoVisit;

    // 是否删除（0：未删除，1：已删除）
    @Schema(description = "是否删除（0：未删除，1：已删除）", example = "0")
    private Integer isDeleted;
}
