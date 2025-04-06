package com.overthinker.cloud.web.service.impl;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.overthinker.cloud.resp.ResultData;
import com.overthinker.cloud.web.entity.DTO.SearchVideoDTO;
import com.overthinker.cloud.web.entity.DTO.VideoInfoTDO;
import com.overthinker.cloud.web.entity.PO.Tag;
import com.overthinker.cloud.web.entity.PO.Video;
import com.overthinker.cloud.web.entity.PO.VideoTag;
import com.overthinker.cloud.web.entity.VO.VideoInfoVO;
import com.overthinker.cloud.web.entity.constants.RedisConst;
import com.overthinker.cloud.web.entity.enums.UploadEnum;
import com.overthinker.cloud.web.mapper.*;
import com.overthinker.cloud.web.service.VideoService;
import com.overthinker.cloud.web.service.VideoTagService;
import com.overthinker.cloud.web.utils.MyRedisCache;
import com.overthinker.cloud.web.utils.SecurityUtils;
import com.overthinker.cloud.web.utils.StringUtils;
import com.overthinker.cloud.web.utils.VideoUploadUtils;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    @Resource
    private VideoTagService videoTagService;

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
    @Transactional
    public ResultData<Void> updateVideoInfo(VideoInfoTDO videoInfoTDO) {
        Video video = BeanUtil.copyProperties(videoInfoTDO, Video.class);
        if (video.getId()==null) {
            video.setUserId(SecurityUtils.getUserId());
        }
        if (this.saveOrUpdate(video)) {
            // 处理标签
            videoTagMapper.delete(new LambdaQueryWrapper<VideoTag>().eq(VideoTag::getVideoId, video.getId()));
//            新增标签
            List<VideoTag> tags = videoInfoTDO.getTagId().stream().map(tagId -> VideoTag.builder().videoId(video.getId()).tagId(tagId).build()).toList();
            videoTagService.saveBatch(tags);
            return ResultData.success();
        }
        return ResultData.failure();
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
    public ResultData<Void> deleteVideo(List<Long> ids) {
        // 删除视频
        // 创建更新条件
        LambdaUpdateWrapper<Video> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(Video::getIsDeleted, true)  // 将isDeleted设置为true
                .in(Video::getId, ids);          // 条件：id在传入的ids列表中

        // 执行更新操作
        boolean result = this.update(updateWrapper);
        if (result) {
            // 删除标签关系
            videoTagMapper.delete(new LambdaQueryWrapper<VideoTag>().in(VideoTag::getVideoId, ids));
            // 删除点赞、收藏、评论
//            likeMapper.delete(new LambdaQueryWrapper<Like>().eq(Like::getType, LikeEnum.LIKE_TYPE_ARTICLE.getType()).and(a -> a.in(Like::getTypeId, ids)));
//            favoriteMapper.delete(new LambdaQueryWrapper<Favorite>().eq(Favorite::getType, FavoriteEnum.FAVORITE_TYPE_ARTICLE.getType()).and(a -> a.in(Favorite::getTypeId, ids)));
//            commentMapper.delete(new LambdaQueryWrapper<Comment>().eq(Comment::getType, CommentEnum.COMMENT_TYPE_ARTICLE.getType()).and(a -> a.in(Comment::getTypeId, ids)));
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
        videoUploadUtils.validateFile(videoCoverUploadEnum, videoCover);
        String Path = videoUploadUtils.buildPath(videoCoverUploadEnum, videoCover.getOriginalFilename());
        return videoUploadUtils.uploadToMinio(Path, videoCover);
    }

    @Override
    public void addVisitCount(Long id) {
        if (myRedisCache.isHasKey(RedisConst.VIDEO_VISIT_COUNT + id))
            myRedisCache.increment(RedisConst.VIDEO_VISIT_COUNT + id, 1L);
        else myRedisCache.setCacheObject(RedisConst.VIDEO_VISIT_COUNT + id, 0);
    }

    @Override
    public List<VideoInfoVO> searchVideoInfo(SearchVideoDTO searchVideoDTO) {
            LambdaQueryWrapper<Video> wrapper = new LambdaQueryWrapper<>();
            wrapper.like(StringUtils.isNotNull(searchVideoDTO.getVideoTitle()), Video::getVideoTitle, searchVideoDTO.getVideoTitle())
                    .eq(StringUtils.isNotNull(searchVideoDTO.getCategoryId()), Video::getCategoryId, searchVideoDTO.getCategoryId())
                    .eq(StringUtils.isNotNull(searchVideoDTO.getPermission()), Video::getPermission, searchVideoDTO.getPermission());
            List<VideoInfoVO> videoInfoVOS = videoMapper.selectList(wrapper).stream().map(video -> video.asViewObject(VideoInfoVO.class)).toList();
            if (!videoInfoVOS.isEmpty()) {
                videoInfoVOS.forEach(videoInfoVO -> {
                    videoInfoVO.setCategoryName(categoryMapper.selectById(videoInfoVO.getCategoryId()).getCategoryName());
                    videoInfoVO.setUserName(userMapper.selectById(videoInfoVO.getUserId()).getUsername());
                    // 查询视频标签
                    List<Long> tagIds = videoTagMapper.selectList(new LambdaQueryWrapper<VideoTag>().eq(VideoTag::getVideoId, videoInfoVO.getId())).stream().map(VideoTag::getTagId).toList();
                    videoInfoVO.setTagsName(tagMapper.selectBatchIds(tagIds).stream().map(Tag::getTagName).toList());
                });
                return videoInfoVOS;
            }
            return null;
        }
    @Override
    public List<VideoInfoVO> listVideo() {
        List<VideoInfoVO> videoInfoVOS = videoMapper.selectList(new LambdaQueryWrapper<Video>()
                .orderByDesc(Video::getCreateTime)).stream().map(video ->
//                video.asViewObject(VideoInfoVO.class
                BeanUtil.copyProperties(video, VideoInfoVO.class)
        ).toList();

        if (!videoInfoVOS.isEmpty()) {
            videoInfoVOS.forEach(videoInfoVO -> {
                videoInfoVO.setCategoryName(categoryMapper.selectById(videoInfoVO.getCategoryId()).getCategoryName());
                videoInfoVO.setUserName(userMapper.selectById(videoInfoVO.getUserId()).getUsername());
                // 查询视频标签
                List<Long> tagIds = videoTagMapper.selectList(new LambdaQueryWrapper<VideoTag>().eq(VideoTag::getVideoId, videoInfoVO.getId())).stream().map(VideoTag::getTagId).toList();
                videoInfoVO.setTagsName(tagMapper.selectBatchIds(tagIds).stream().map(Tag::getTagName).toList());
            });
            return videoInfoVOS;
        }
        return null;
    }

    @Override
    @Transactional
    public Void updateVideoPermission(Long videoId, boolean permission) {
        // 构建更新条件
        LambdaUpdateWrapper<Video> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Video::getId, videoId)
                .set(Video::getPermission, permission);

        // 执行更新
        boolean success = this.update(updateWrapper);

        if (!success) {
            throw new RuntimeException("视频权限更新失败");
        }
        return null;
    }


    @Override
    public String publishVideo(Long videoId) {
        // 根据videoId获取视频
        Video video = this.getById(videoId);
        if (video == null) {
            return "视频不存在";
        }
        // 取反status状态
        video.setStatus(!video.getStatus());
        // 更新数据库
        boolean result = this.updateById(video);
        return result ? "操作成功" : "操作失败";
    }


}
