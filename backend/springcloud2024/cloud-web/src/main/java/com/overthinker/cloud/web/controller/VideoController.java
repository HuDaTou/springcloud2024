package com.overthinker.cloud.web.controller;

import com.overthinker.cloud.resp.ResultData;
import com.overthinker.cloud.web.controller.base.BaseController;
import com.overthinker.cloud.web.entity.DTO.VideoInfoTDO;
import com.overthinker.cloud.web.entity.VO.VideoInfoVO;
import com.overthinker.cloud.web.service.VideoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;




/**
 * @author overH
 * <p>
 * 创建时间：2023/12/26 16:15
 */
@Tag(name = "文件上传")
@RestController
@RequestMapping("/video")
public class VideoController extends BaseController {

    @Resource
    VideoService videoService;





    @Operation(summary = "上传视频")
    @RequestMapping("/upload")
    @Parameters({
            @Parameter(name = "videoFile", description = "视频文件"),
            @Parameter(name = "VideoPremissions", description = "视频权限，如果是私有视频则为true，否则为false", required = true),
    })
    public ResultData<Map<String, Object>> uploadVideo( @NotNull(message = "视频文件不能为空") @RequestParam("VideoFile") MultipartFile videoFile,
                                                        @RequestParam("VideoPermissions") Boolean VideoPermissions

    ) {
        return messageHandler(() -> videoService.uploadVideo(videoFile, VideoPermissions));
    }





    @Operation(summary = "上传封面信息")
    @RequestMapping("/upload/videocover")
    @Parameters({
            @Parameter(name = "videoCover", description = "视频封面"),
            @Parameter(name = "videoaddress", description = "视频地址", required = true)
    })
    public ResultData<String> uploadVideoCover(
            @RequestParam("videocover") MultipartFile videoCover , @RequestParam("videoaddress") String videoaddress
    ) {
//        return messageHandler(() -> videoService.uploadVideoCover(videoCover, videoaddress));
        return messageHandler(() -> "上传成功");
    }

    @Operation(summary = "添加或更改视频信息")
    @PostMapping("/upload/videoInfo")
    public ResultData<String> saveVideoInfo(@Valid @RequestBody VideoInfoTDO videoInfoTDO){return messageHandler(() -> videoService.updateVideoInfo(videoInfoTDO));}




    @Operation(summary = "获取视频列表")
    @GetMapping("/list")
    @Parameters({
            @Parameter(name = "pageNum", description = "页码", required = true),
            @Parameter(name = "pageSize", description = "每页数量", required = true)
    })
    public ResultData<List<VideoInfoVO>> getVideoList(
            @NotNull @RequestParam(name = "pageNum") Integer pageNum,
            @NotNull @RequestParam(name = "pageSize") Integer pageSize
    ) {
        return messageHandler(() -> videoService.getUserAndPublicVideoList(pageNum, pageSize));
    }

    @Operation(summary = "删除视频")
    @DeleteMapping("/delete")
    public ResultData<String> deleteVideo() {
        return messageHandler(videoService::deleteVideo);
    }

    @Operation(summary = "修改视频信息")
    @PutMapping("/update")
    public ResultData<String> updateVideoInfo(@Valid @RequestBody VideoInfoTDO videoInfoTDO) {
        return messageHandler(() -> videoService.updateVideoInfo(videoInfoTDO));
    }

    @Operation(summary = "新增视频信息")
    @PostMapping("/add")
    public ResultData<String> addVideo(@Valid @RequestBody VideoInfoTDO videoInfoTDO) {
        return messageHandler(() -> videoService.updateVideoInfo(videoInfoTDO));
    }

    @Operation(summary = "发布视频")
    @PostMapping("/publish")
    public ResultData<String> publishVideo(@RequestParam("videoId") Integer videoId) {
        return messageHandler(() -> "发布成功");
    }

    @Operation(summary = "获取视频的缓存地址")
    @GetMapping("/cachepath")
    public ResultData<String> getVideoCachePath(@RequestParam("videoId") Integer videoId) {
        return messageHandler(() -> videoService.getVideoCachePath());
    }

}
