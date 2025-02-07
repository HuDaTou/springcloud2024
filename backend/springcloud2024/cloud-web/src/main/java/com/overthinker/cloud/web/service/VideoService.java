package com.overthinker.cloud.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.overthinker.cloud.web.entity.DTO.VideoInfoTDO;
import com.overthinker.cloud.web.entity.PO.Video;
import com.overthinker.cloud.web.entity.VO.VideoInfoVO;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface VideoService extends IService<Video> {


    /**
     * 上传视频 图片和视频
     * @param VideoPermissions 视频权限 true 私有视频 false 公共视频
     * @param videoFile 视频文件
     * @param videoCover 视频封面
     * @return Map<String, String>
     */
    Map<String, Object> uploadVideo(MultipartFile videoFile , MultipartFile videoCover, Integer categoryId, Boolean VideoPermissions);


    /**
     * 修改视频信息
     * @param videoInfoTDO 视频信息
     * @return String
     */
    String updateVideoInfo(VideoInfoTDO videoInfoTDO);




    /**
     * 获取公共视频的视频列表
     * @return List<VideoInfoVO>
     */
    List<VideoInfoVO> getPublicVideoList();

    /**
     * 获取用户的视频列表
     *
     * @return List<Video>
     */
    List<VideoInfoVO> getUserVideoList(Integer userId);

    /**
     * 获取用户和公共视频列表
     * @return Map<String, String>
     */
    List<VideoInfoVO> getUserAndPublicVideoList(@NotNull Integer pageNum, @NotNull Integer pageSize);

    /**
     * 删除视频
     * @return String
     */
    String  deleteVideo();









}
