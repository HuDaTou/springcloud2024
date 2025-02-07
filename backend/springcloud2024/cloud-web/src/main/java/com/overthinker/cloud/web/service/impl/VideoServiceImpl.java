package com.overthinker.cloud.web.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.overthinker.cloud.web.entity.DTO.VideoInfoTDO;
import com.overthinker.cloud.web.entity.PO.Video;
import com.overthinker.cloud.web.entity.VO.VideoInfoVO;
import com.overthinker.cloud.web.entity.enums.VideoUploadEnum;
import com.overthinker.cloud.web.mapper.VideoMapper;
import com.overthinker.cloud.web.service.VideoService;
import com.overthinker.cloud.web.utils.VideoUploadUtils;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

@Slf4j
@Service("videoService")
public class VideoServiceImpl extends ServiceImpl<VideoMapper, Video> implements VideoService {


    @Resource
    VideoUploadUtils videoUploadUtils;

    @Resource
    VideoMapper videoMapper;

    // 使用有界线程池防止资源耗尽
    private final ExecutorService uploadExecutor =
            Executors.newFixedThreadPool(2, r -> {
                Thread t = new Thread(r);
                t.setDaemon(true);
                return t;
            });


    @Override
    public Map<String, Object> uploadVideo( MultipartFile videoFile, MultipartFile videoCover , Integer categoryId, Boolean VideoPermissions) {
        VideoUploadEnum videoUploadEnum = VideoUploadEnum.VIDEO_PUBLIC;
        if (VideoPermissions) {
            videoUploadEnum = VideoUploadEnum.VIDEO_PRIVATE;
        }
//        参数校验
        videoUploadUtils.validateVideo(videoUploadEnum, videoFile, videoCover);
        // 生成统一存储路径
        final String BasePath = videoUploadUtils.buildUserBasePath(videoUploadEnum);
        final String videoObjectName = BasePath + videoFile.getOriginalFilename();
        final String coverObjectName = BasePath + videoCover.getOriginalFilename();

        CountDownLatch latch = new CountDownLatch(2);
        Map<String, Future<String>> futures = new HashMap<>();
        try {
            // 提交上传任务
            futures.put("video", uploadExecutor.submit(
                    createUploadTask(videoFile, videoObjectName, latch)));
            futures.put("videoCover", uploadExecutor.submit(
                    createUploadTask(videoCover, coverObjectName, latch)));

            latch.await(3, TimeUnit.SECONDS); // 等待任务启动

            // 获取结果
            Map<String, Object> result = new HashMap<>();
            for (Map.Entry<String, Future<String>> entry : futures.entrySet()) {
                result.put(entry.getKey(),
                        entry.getValue().get());
            }
            // 保存视频信息
            String size = videoUploadUtils.convertVideoSize(videoFile.getSize());
            result.put("videoTitle", videoFile.getOriginalFilename());
            result.put("videoType", videoFile.getContentType());
            result.put("videoSize", size);



            return result;

        }catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        } finally {
//            关闭流
            closeResources(videoFile, videoCover);
        }

    }

    @Override
    public String updateVideoInfo(VideoInfoTDO videoInfoTDO) {
        


        return "";
    }

    @Override
    public List<VideoInfoVO> getPublicVideoList() {

        return List.of();
    }

    @Override
    public List<VideoInfoVO> getUserVideoList(Integer userId) {

        return List.of();
    }

    @Override
    public List<VideoInfoVO> getUserAndPublicVideoList(@NotNull Integer pageNum, @NotNull Integer pageSize) {

        return List.of();
    }


    @Override
    public String deleteVideo() {
        return "";
    }



    /**
     * 创建上传任务
     */
    private Callable<String> createUploadTask(MultipartFile file,
                                              String objectName,
                                              CountDownLatch latch) {
        return () -> {
            try (InputStream is = file.getInputStream()) {
                latch.countDown();
                return videoUploadUtils.uploadToMinio(objectName, is, file.getSize(),
                        file.getContentType());
            }
        };
    }



    /**
    * 关闭输入流资源
    */
    private void closeResources(MultipartFile... files) {
    Arrays.stream(files).forEach(file -> {
        try {
            if (!file.isEmpty()) {
                file.getInputStream().close();
            }
        } catch (Exception e) {
            log.warn("资源关闭失败", e);
        }
    });
    }




}
