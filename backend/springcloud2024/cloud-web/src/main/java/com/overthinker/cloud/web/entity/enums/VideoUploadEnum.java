package com.overthinker.cloud.web.entity.enums;

import com.overthinker.cloud.web.entity.constants.ImageConst;
import com.overthinker.cloud.web.entity.constants.VideoConst;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;

@Getter
@AllArgsConstructor
public enum VideoUploadEnum {
    VIDEO_PRIVATE("video/", "个人视频", Set.of(ImageConst.JPG, ImageConst.JPEG, ImageConst.PNG, ImageConst.WEBP),50.0 , Set.of(VideoConst.AVI,VideoConst.FLV, VideoConst.MP4, VideoConst.RM), 1024.0),
    VIDEO_PUBLIC("video/public", "公共视频", Set.of(ImageConst.JPG, ImageConst.JPEG, ImageConst.PNG, ImageConst.WEBP),50.0 , Set.of(VideoConst.AVI,VideoConst.FLV, VideoConst.MP4, VideoConst.RM), 1024.0),
    VIDEO_TEMP("video/temp", "临时视频", Set.of(ImageConst.JPG, ImageConst.JPEG, ImageConst.PNG, ImageConst.WEBP),50.0 , Set.of(VideoConst.AVI,VideoConst.FLV, VideoConst.MP4, VideoConst.RM), 1024.0);
    private final String dir;

    private final String description;

    private final Set<String> coverFormat;

    private final Double coverLimitSize;

    private final Set<String> videoFormat;

    private final Double videoLimitSize;

}
