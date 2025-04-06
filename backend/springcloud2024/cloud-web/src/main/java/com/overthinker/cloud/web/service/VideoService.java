package com.overthinker.cloud.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.overthinker.cloud.resp.ResultData;
import com.overthinker.cloud.web.entity.DTO.SearchVideoDTO;
import com.overthinker.cloud.web.entity.DTO.VideoInfoTDO;
import com.overthinker.cloud.web.entity.PO.Video;
import com.overthinker.cloud.web.entity.VO.VideoInfoVO;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface VideoService extends IService<Video> {


    /**
     * 上传视频
     * @param videoFile 视频文件
     * @return Map<String, String>
     */
    Map<String, Object> uploadVideo(MultipartFile videoFile) ;


    /**
     * 修改视频信息
     * @param videoInfoTDO 视频信息
     * @return String
     */
    ResultData<Void> updateVideoInfo(VideoInfoTDO videoInfoTDO);



    /**
     * 获取用户和公共视频列表
     * @return Map<String, String>
     */
    List<VideoInfoVO> getUserAndPublicVideoList(@NotNull Integer pageNum, @NotNull Integer pageSize);

    /**
     * 删除视频
     * @return String
     */
    ResultData<Void>  deleteVideo(List<Long> ids);


    /**
     * 获取上传视频缓存路径, 根据用户id 生成 uuid 临时文件，将视频存储在本地后，在将视频上传到云存储
     * 后面改成切片上传
     * @return String
     */
    String getVideoCachePath();



    /**
     * 上传视频封面
     * @param videoCover 视频封面
     * @return String
     */
    String uploadVideoCover(MultipartFile videoCover);

    void addVisitCount(Long videoId);

    List<VideoInfoVO> searchVideoInfo(SearchVideoDTO searchVideoDTO);


    List<VideoInfoVO> listVideo();


    Void updateVideoPermission(Long videoId, boolean permission);

    String publishVideo(Long videoId);
}
