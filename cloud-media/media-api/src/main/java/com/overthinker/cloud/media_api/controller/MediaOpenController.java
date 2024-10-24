package com.overthinker.cloud.media_api.controller;


import com.overthinker.cloud.media.model.po.MediaFiles;
import com.overthinker.cloud.media.service.service.MediaFileService;
import com.overthinker.cloud.resp.ResultData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "媒资文件管理接口")
@RestController
@RequestMapping("/open")
public class MediaOpenController {
    @Resource
    MediaFileService mediaFileService;

    @Operation(summary = "预览文件")
    @RequestMapping("/preview/{mediaId}")
    public ResultData<String> getPlayUrlByMediaId(@PathVariable String mediaId) {
        //查询媒资文件信息
        MediaFiles mediaFiles = mediaFileService.getFileById(mediaId);

        if (mediaFiles == null) {
            return ResultData.fail("找不到视频");
        }
        //取出视频播放地址
        String url = mediaFiles.getUrl();
        if (StringUtils.isEmpty(url)) {
            return ResultData.fail("该视频正在处理中");
        }
        return ResultData.success(mediaFiles.getUrl());
    }


}
