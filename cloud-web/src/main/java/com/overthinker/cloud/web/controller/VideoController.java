package com.overthinker.cloud.web.controller;

import com.overthinker.cloud.common.core.base.BaseController;
import com.overthinker.cloud.common.core.resp.ResultData;
import com.overthinker.cloud.system.starter.redis.annotation.AccessLimit;
import com.overthinker.cloud.web.entity.DTO.SearchVideoDTO;
import com.overthinker.cloud.web.entity.DTO.VideoInfoTDO;
import com.overthinker.cloud.web.entity.VO.VideoInfoVO;
import com.overthinker.cloud.web.service.VideoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * 视频控制器
 * <p>
 * 处理视频的管理接口，包括视频上传、封面上传、信息管理、列表查询等操作
 * </p>
 *
 * @author overH
 * @since 2023-12-26
 */
@Tag(name = "视频相关接口")
@RestController
@RequestMapping("/video")
@RequiredArgsConstructor
public class VideoController extends BaseController {

    private final VideoService videoService;

    /**
     * 上传视频
     *
     * @param videoFile 视频文件
     * @return 视频信息
     */
    @Operation(summary = "上传视频")
    @PostMapping("/upload/video")
    @Parameters({
            @Parameter(name = "videoFile", description = "视频文件"),
    })
    public ResultData<Map<String, Object>> uploadVideo(@NotNull(message = "视频文件不能为空") @RequestParam("VideoFile") MultipartFile videoFile) {
        return messageHandler(() -> videoService.uploadVideo(videoFile));
    }

    /**
     * 上传视频封面
     *
     * @param videoCover 视频封面文件
     * @return 封面URL
     */
    @Operation(summary = "上传封面信息")
    @PostMapping("/upload/cover")
    @Parameters({
            @Parameter(name = "videoCover", description = "视频封面"),
    })
    public ResultData<String> uploadVideoCover(@RequestParam("videocover") MultipartFile videoCover) {
        return messageHandler(() -> videoService.uploadVideoCover(videoCover));
    }

    /**
     * 添加或更新视频信息
     *
     * @param videoInfoTDO 视频信息
     * @return 操作结果
     */
    @Operation(summary = "添加或更改视频信息")
    @PostMapping("/upload/videoInfo")
    public ResultData<Void> saveVideoInfo(@Valid @RequestBody VideoInfoTDO videoInfoTDO) {
        return videoService.updateVideoInfo(videoInfoTDO);
    }

    /**
     * 获取视频列表
     *
     * @param pageNum  页码
     * @param pageSize 每页数量
     * @return 视频列表
     */
    @Operation(summary = "获取视频列表")
    @GetMapping("/list")
    @Parameters({
            @Parameter(name = "pageNum", description = "页码", required = true),
            @Parameter(name = "pageSize", description = "每页数量", required = true)
    })
    public ResultData<List<VideoInfoVO>> getVideoList(
            @NotNull @RequestParam("pageNum") Integer pageNum,
            @NotNull @RequestParam("pageSize") Integer pageSize
    ) {
        return messageHandler(() -> videoService.getUserAndPublicVideoList(pageNum, pageSize));
    }

    /**
     * 删除视频
     *
     * @param ids 视频ID列表
     * @return 操作结果
     */
    @Operation(summary = "删除视频")
    @DeleteMapping("/delete")
    public ResultData<Void> deleteVideo(@RequestParam("ids") @NotNull List<Long> ids) {
        return videoService.deleteVideo(ids);
    }

    /**
     * 发布视频
     *
     * @param videoId 视频ID
     * @return 操作结果
     */
    @Operation(summary = "发布视频")
    @PostMapping("/publish")
    public ResultData<String> publishVideo(@RequestParam("videoId") Long videoId) {
        return messageHandler(() -> videoService.publishVideo(videoId));
    }

    /**
     * 获取视频缓存地址
     *
     * @param videoId 视频ID
     * @return 缓存地址
     */
    @Operation(summary = "获取视频的缓存地址")
    @GetMapping("/cachepath")
    public ResultData<String> getVideoCachePath(@RequestParam("videoId") Integer videoId) {
        return messageHandler(videoService::getVideoCachePath);
    }

    /**
     * 视频访问量+1
     *
     * @param id 视频ID
     * @return 操作结果
     */
    @Operation(summary = "视频访问量+1")
    @Parameter(name = "id", description = "视频id", required = true)
    @AccessLimit(seconds = 60, maxCount = 60)
    @GetMapping("/visit/{id}")
    public ResultData<Void> visit(@PathVariable("id") @NotNull Long id) {
        videoService.addVisitCount(id);
        return messageHandler(() -> null);
    }

    /**
     * 获取所有视频列表（后台）
     *
     * @return 视频列表
     */
    @Operation(summary = "获取所有的视频列表")
    @AccessLimit(seconds = 60, maxCount = 30)
    @GetMapping("/back/list")
    public ResultData<List<VideoInfoVO>> listArticle() {
        return messageHandler(videoService::listVideo);
    }

    /**
     * 搜索视频信息
     *
     * @param searchVideoDTO 搜索条件
     * @return 视频列表
     */
    @Operation(summary = "获取视频信息")
    @PostMapping("/search")
    public ResultData<List<VideoInfoVO>> getVideoInfo(@RequestBody SearchVideoDTO searchVideoDTO) {
        return messageHandler(() -> videoService.searchVideoInfo(searchVideoDTO));
    }

    /**
     * 修改视频权限
     *
     * @param videoId   视频ID
     * @param permission 权限标识
     * @return 操作结果
     */
    @Operation(summary = "修改视频的权限")
    @PutMapping("/permission")
    public ResultData<Void> updateVideoPermission(@RequestParam("videoId") Long videoId,
                                                  @RequestParam("permission") boolean permission) {
        return messageHandler(() -> videoService.updateVideoPermission(videoId, permission));
    }
}