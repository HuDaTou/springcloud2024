package com.overthinker.cloud.web.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.overthinker.cloud.api.apis.auth.api.UserClient;
import com.overthinker.cloud.api.apis.media.ENUM.MediaUploadRuleEnum;
import com.overthinker.cloud.api.apis.media.api.MediaClient;
import com.overthinker.cloud.common.core.resp.ResultData;
import com.overthinker.cloud.common.core.resp.ReturnCodeEnum;
import com.overthinker.cloud.web.entity.DTO.SearchVideoDTO;
import com.overthinker.cloud.web.entity.DTO.VideoInfoTDO;
import com.overthinker.cloud.web.entity.PO.Tag;
import com.overthinker.cloud.web.entity.PO.Video;
import com.overthinker.cloud.web.entity.PO.VideoTag;
import com.overthinker.cloud.web.entity.VO.VideoInfoVO;
import com.overthinker.cloud.system.starter.redis.constants.RedisConstants;
import com.overthinker.cloud.web.mapper.*;
import com.overthinker.cloud.web.service.VideoService;
import com.overthinker.cloud.web.service.VideoTagService;
import com.overthinker.cloud.system.starter.redis.utils.MyRedisCache;
import com.overthinker.cloud.system.starter.auth.utils.SecurityUtils;
import com.overthinker.cloud.common.core.utils.MyStringUtils;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Slf4j
@Service("videoService")
@RequiredArgsConstructor
@Validated
public class VideoServiceImpl extends ServiceImpl<VideoMapper, Video> implements VideoService {

    private final MyRedisCache myRedisCache;
    private final VideoMapper videoMapper;
    private final CategoryMapper categoryMapper;
    private final UserClient userClient;
    private final VideoTagMapper videoTagMapper;
    private final TagMapper tagMapper;
    private final VideoTagService videoTagService;
    private final MediaClient mediaClient;

    @Override
    public Map<String, Object> uploadVideo(MultipartFile videoFile) {
        try {
            ResultData<Map<String, Object>> result = mediaClient.uploadFileWithRuleName(
                    SecurityUtils.getUserId(),
                    videoFile,
                    MediaUploadRuleEnum.VIDEO_PRIVATE.name()
            );

            if (result.getCode().equals(ReturnCodeEnum.SUCCESS.getCode()) && result.getData() != null) {
                String videoUrl = (String) result.getData().get("fileUrl");
                return Map.of(
                        "video", videoUrl,
                        "videoSize", videoFile.getSize() / 1024.0,
                        "videoType", videoFile.getContentType()
                );
            }
            return Map.of("error", "上传失败");
        } catch (Exception e) {
            log.error("视频上传失败", e);
            return Map.of("error", "上传失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional
    public ResultData<Void> updateVideoInfo(VideoInfoTDO videoInfoTDO) {
        Video video = BeanUtil.copyProperties(videoInfoTDO, Video.class);
        if (video.getId() == null) {
            video.setUserId(SecurityUtils.getUserId());
        }
        if (this.saveOrUpdate(video)) {
            videoTagMapper.delete(new LambdaQueryWrapper<VideoTag>().eq(VideoTag::getVideoId, video.getId()));
            List<VideoTag> tags = videoInfoTDO.getTagId().stream().map(tagId -> new VideoTag().setVideoId(video.getId()).setTagId(tagId)).toList();
            videoTagService.saveBatch(tags);
            return ResultData.success();
        }
        return ResultData.failure();
    }

    @Override
    public List<VideoInfoVO> getUserAndPublicVideoList(@NotNull Integer pageNum, @NotNull Integer pageSize) {
        LambdaQueryWrapper<Video> queryWrapper = new LambdaQueryWrapper<>();
        if (SecurityUtils.isAuthenticated()) {
            Long userId = SecurityUtils.getUserId();
            queryWrapper
                    .eq(Video::getPermission, false)
                    .or(w -> w
                            .eq(Video::getPermission, true)
                            .eq(Video::getUserId, userId)
                            .orderByDesc(Video::getCreateTime)
                    );
        } else {
            queryWrapper.eq(Video::getPermission, false);
        }
        List<Video> videos = videoMapper.selectList(queryWrapper);

        List<VideoInfoVO> videoInfoVOS = BeanUtil.copyToList(videos, VideoInfoVO.class);

        if (!videoInfoVOS.isEmpty()) {
            videoInfoVOS.forEach(videoInfoVO -> {
                videoInfoVO.setCategoryName(categoryMapper.selectById(videoInfoVO.getCategoryId()).getCategoryName());
                ResultData<String> usernameResult = userClient.getUsernameById(videoInfoVO.getUserId());
                videoInfoVO.setUserName(usernameResult.getData() != null ? usernameResult.getData() : "");
                List<Long> tagIds = videoTagMapper.selectList(new LambdaQueryWrapper<VideoTag>().eq(VideoTag::getVideoId, videoInfoVO.getId())).stream().map(VideoTag::getTagId).toList();
                videoInfoVO.setTagsName(tagMapper.selectByIds(tagIds).stream().map(Tag::getTagName).toList());
            });
            return videoInfoVOS;
        }
        return null;
    }

    @Transactional
    @Override
    public ResultData<Void> deleteVideo(List<Long> ids) {
        LambdaUpdateWrapper<Video> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(Video::isDeleted, true)
                .in(Video::getId, ids);

        boolean result = this.update(updateWrapper);
        if (result) {
            videoTagMapper.delete(new LambdaQueryWrapper<VideoTag>().in(VideoTag::getVideoId, ids));
            return ResultData.success();
        }
        return ResultData.failure();
    }

    @Override
    public String getVideoCachePath() {
        return "";
    }

    @Override
    public String uploadVideoCover(MultipartFile videoCover) {
        try {
            ResultData<Map<String, Object>> result = mediaClient.uploadFileWithRuleName(
                    SecurityUtils.getUserId(),
                    videoCover,
                    MediaUploadRuleEnum.VIDEO_COVER.name()
            );

            if (result.getCode().equals(ReturnCodeEnum.SUCCESS.getCode()) && result.getData() != null) {
                return (String) result.getData().get("fileUrl");
            }
            return null;
        } catch (Exception e) {
            log.error("视频封面上传失败", e);
            return null;
        }
    }

    @Override
    public void addVisitCount(Long id) {
        if (myRedisCache.isHasKey(RedisConstants.VIDEO_VISIT_COUNT + id))
            myRedisCache.increment(RedisConstants.VIDEO_VISIT_COUNT + id, 1L);
        else myRedisCache.setCacheObject(RedisConstants.VIDEO_VISIT_COUNT + id, 0);
    }

    @Override
    public List<VideoInfoVO> searchVideoInfo(SearchVideoDTO searchVideoDTO) {
        LambdaQueryWrapper<Video> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(MyStringUtils.isNotNull(searchVideoDTO.getVideoTitle()), Video::getVideoTitle, searchVideoDTO.getVideoTitle())
                .eq(MyStringUtils.isNotNull(searchVideoDTO.getCategoryId()), Video::getCategoryId, searchVideoDTO.getCategoryId())
                .eq(MyStringUtils.isNotNull(searchVideoDTO.getPermission()), Video::getPermission, searchVideoDTO.getPermission());
        List<VideoInfoVO> videoInfoVOS = videoMapper.selectList(wrapper).stream().map(video -> video.copyProperties(VideoInfoVO.class)).toList();
        if (!videoInfoVOS.isEmpty()) {
            videoInfoVOS.forEach(videoInfoVO -> {
                videoInfoVO.setCategoryName(categoryMapper.selectById(videoInfoVO.getCategoryId()).getCategoryName());
                ResultData<String> usernameResult = userClient.getUsernameById(videoInfoVO.getUserId());
                videoInfoVO.setUserName(usernameResult.getData() != null ? usernameResult.getData() : "");
                List<Long> tagIds = videoTagMapper.selectList(new LambdaQueryWrapper<VideoTag>().eq(VideoTag::getVideoId, videoInfoVO.getId())).stream().map(VideoTag::getTagId).toList();
                videoInfoVO.setTagsName(tagMapper.selectByIds(tagIds).stream().map(Tag::getTagName).toList());
            });
            return videoInfoVOS;
        }
        return null;
    }

    @Override
    public List<VideoInfoVO> listVideo() {
        List<VideoInfoVO> videoInfoVOS = videoMapper.selectList(new LambdaQueryWrapper<Video>()
                .orderByDesc(Video::getCreateTime)).stream().map(video ->
                BeanUtil.copyProperties(video, VideoInfoVO.class)
        ).toList();

        if (!videoInfoVOS.isEmpty()) {
            videoInfoVOS.forEach(videoInfoVO -> {
                videoInfoVO.setCategoryName(categoryMapper.selectById(videoInfoVO.getCategoryId()).getCategoryName());
                ResultData<String> usernameResult = userClient.getUsernameById(videoInfoVO.getUserId());
                videoInfoVO.setUserName(usernameResult.getData() != null ? usernameResult.getData() : "");
                List<Long> tagIds = videoTagMapper.selectList(new LambdaQueryWrapper<VideoTag>().eq(VideoTag::getVideoId, videoInfoVO.getId())).stream().map(VideoTag::getTagId).toList();
                videoInfoVO.setTagsName(tagMapper.selectByIds(tagIds).stream().map(Tag::getTagName).toList());
            });
            return videoInfoVOS;
        }
        return null;
    }

    @Override
    public Void updateVideoPermission(Long videoId, boolean permission) {
        LambdaUpdateWrapper<Video> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Video::getId, videoId)
                .set(Video::getPermission, permission);

        boolean success = this.update(updateWrapper);

        if (!success) {
            throw new RuntimeException("视频权限更新失败");
        }
        return null;
    }

    @Override
    public String publishVideo(Long videoId) {
        Video video = this.getById(videoId);
        if (video == null) {
            return "视频不存在";
        }
        video.setStatus(!video.getStatus());
        boolean result = this.updateById(video);
        return result ? "操作成功" : "操作失败";
    }
}