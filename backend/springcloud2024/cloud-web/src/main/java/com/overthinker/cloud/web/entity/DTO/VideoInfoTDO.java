package com.overthinker.cloud.web.entity.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor  // 必须添加这个
@AllArgsConstructor
@Schema(description = "视频信息")
@Builder
public class VideoInfoTDO {


    @Schema(description = "视频id")
    private Long id;

    @Schema(description = "视频作者")
    private Long userId;

    @Schema(description = "视频分类")
    private Long categoryId;

    @Schema(description = "视频标签")
    private List<Long> tagId;

    @Schema(description = "视频封面地址")
    private String videoCover;

    @Schema(description = "视频地址")
    private String video;

    @Schema(description = "视频标题")
    private String videoTitle;

    @Schema(description = "视频描述")
    private String videoDescription;

    @Schema(description = "视频文件格式")
    private String videoType;

    @Schema(description = "视频大小")
    private String videoSize;

    @Schema(description = "视频权限")
    private boolean permission;














}
