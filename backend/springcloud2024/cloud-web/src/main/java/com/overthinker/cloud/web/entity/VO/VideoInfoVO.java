package com.overthinker.cloud.web.entity.VO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "视频信息")
public class VideoInfoVO {

    @Schema(description = "视频id")
    private Long id;

    @Schema(description = "上传用户id")
    private Long userId;

    @Schema(description = "上传用户名称")
    private String userName;


    @Schema(description = "视频分类")
    private int categoryId;

    @Schema(description = "视频分类标签")
    private String categoryName;

    @Schema(description = "视频标签")
    private List<String> tagsName;


    @Schema(description = "视频封面地址")
    private String videoCover;

    @Schema(description = "视频地址")
    private String video;

    @Schema(description = "视频标题")
    private String videoTitle;

    @Schema(description = "视频描述")
    private String description;

    @Schema(description = "视频观看数量")
    private Long videoVisit;

//    @Schema(description = "视频文件格式")
//    private String videoType;
//
//    @Schema(description = "视频大小")
//    private int videoSize;

    @Schema(description = "视频权限")
    private boolean permission;


}
