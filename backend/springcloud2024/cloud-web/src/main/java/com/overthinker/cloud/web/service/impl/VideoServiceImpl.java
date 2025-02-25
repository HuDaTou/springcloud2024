package com.overthinker.cloud.web.service.impl;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.overthinker.cloud.web.entity.DTO.VideoInfoTDO;
import com.overthinker.cloud.web.entity.PO.Tag;
import com.overthinker.cloud.web.entity.PO.Video;
import com.overthinker.cloud.web.entity.PO.VideoTag;
import com.overthinker.cloud.web.entity.VO.VideoInfoVO;
import com.overthinker.cloud.web.entity.constants.RedisConst;
import com.overthinker.cloud.web.entity.enums.UploadEnum;
import com.overthinker.cloud.web.mapper.*;
import com.overthinker.cloud.web.service.VideoService;
import com.overthinker.cloud.web.utils.MyRedisCache;
import com.overthinker.cloud.web.utils.SecurityUtils;
import com.overthinker.cloud.web.utils.VideoUploadUtils;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Slf4j
@Service("videoService")
public class VideoServiceImpl extends ServiceImpl<VideoMapper, Video> implements VideoService {


    @Resource
    VideoUploadUtils videoUploadUtils;




    @Resource
    MyRedisCache myRedisCache;

    @Resource
    VideoMapper videoMapper;

    // ... 已有注入...
    @Resource
    private CategoryMapper categoryMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private VideoTagMapper videoTagMapper;

    @Resource
    private TagMapper tagMapper;




    private final UploadEnum videoUploadEnum = UploadEnum.VIDEO_PATH;
    private final UploadEnum videoCoverUploadEnum = UploadEnum.VEDIO_COVER;

    // 使用有界线程池防止资源耗尽
//    private final ExecutorService uploadExecutor =
//            Executors.newFixedThreadPool(2, r -> {
//                Thread t = new Thread(r);
//                t.setDaemon(true);
//                return t;
//            });


    @Override
    public Map<String, Object> uploadVideo( MultipartFile videoFile)  {

//        参数校验
        videoUploadUtils.validateFile(videoUploadEnum, videoFile);
        // 生成统一存储路径
        String Path = videoUploadUtils.buildPath(videoUploadEnum, videoFile.getOriginalFilename());
        String s = videoUploadUtils.uploadToMinio(Path, videoFile);
        return Map.of("video", s,"videoSize",videoUploadUtils.convertVideoSize(videoFile.getSize()),"videoType",videoFile.getContentType());
    }

    @Override
    public String updateVideoInfo(VideoInfoTDO videoInfoTDO) {
        Video video = BeanUtil.copyProperties(videoInfoTDO, Video.class);
        this.saveOrUpdate(video);
        if (this.saveOrUpdate(video)) {
            return "修改成功";
        } else {
            throw new RuntimeException("修改失败");
        }

    }

    @Override
    public List<VideoInfoVO> getPublicVideoList() {
        LambdaQueryWrapper<Video> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Video::getPermission, false);
        List<Video> videos = videoMapper.selectList(queryWrapper);
        return BeanUtil.copyToList(videos, VideoInfoVO.class);
    }

    @Override
    public List<VideoInfoVO> getUserVideoList(Long userId) {
        LambdaQueryWrapper<Video> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Video::getUserId, userId);
        List<Video> videos = videoMapper.selectList(queryWrapper);
        return BeanUtil.copyToList(videos, VideoInfoVO.class);
    }

    @Override
    public List<VideoInfoVO> getUserAndPublicVideoList(@NotNull Integer pageNum, @NotNull Integer pageSize) {
        LambdaQueryWrapper<Video> queryWrapper = new LambdaQueryWrapper<>();
        if (SecurityUtils.isLogin()) {
            Long userId = SecurityUtils.getUserId();
            // 组合条件：公开视频 或 (私有视频 且 属于当前用户)
            queryWrapper
                    .eq(Video::getPermission, false)
                    .or(w -> w
                            .eq(Video::getPermission, true)
                            .eq(Video::getUserId, userId)
                            .orderByDesc(Video::getCreateTime)
                    );
        } else {
            // 未登录用户只能查看公开视频
            queryWrapper.eq(Video::getPermission, false);
        }
        List<Video> videos = videoMapper.selectList(queryWrapper);

        List<VideoInfoVO> videoInfoVOS = BeanUtil.copyToList(videos, VideoInfoVO.class);

        if (!videoInfoVOS.isEmpty()) {
            videoInfoVOS.forEach(videoInfoVO -> {
                videoInfoVO.setCategoryName(categoryMapper.selectById(videoInfoVO.getCategoryId()).getCategoryName());
                videoInfoVO.setUserName(userMapper.selectById(videoInfoVO.getUserId()).getUsername());
                // 查询文章标签
                List<Long> tagIds = videoTagMapper.selectList(new LambdaQueryWrapper<VideoTag>().eq(VideoTag::getVideoId, videoInfoVO.getId())).stream().map(VideoTag::getTagId).toList();
                videoInfoVO.setTagsName(tagMapper.selectBatchIds(tagIds).stream().map(Tag::getTagName).toList());
            });
            return videoInfoVOS;
        }
        return null;
    }


    @Override
    public String deleteVideo(Long id) {
        boolean b = this.removeById(id);
        return b ? "删除成功" : "删除失败";
    }

    @Override
    public String getVideoCachePath() {
        return "";
    }

    @Override
    public String uploadVideoCover(MultipartFile videoCover) {

        videoUploadUtils.validateVideoCover(videoUploadEnum, videoCover);



        return "";
    }

    @Override
    public void addVisitCount(Long id) {
        if (myRedisCache.isHasKey(RedisConst.VIDEO_VISIT_COUNT + id))
            myRedisCache.increment(RedisConst.VIDEO_VISIT_COUNT + id, 1L);
        else myRedisCache.setCacheObject(RedisConst.VIDEO_VISIT_COUNT + id, 0);
    }











}
