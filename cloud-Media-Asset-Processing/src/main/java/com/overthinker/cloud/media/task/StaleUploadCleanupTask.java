//package com.overthinker.cloud.media.task;
//
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.overthinker.cloud.media.config.MediaProperties;
//import com.overthinker.cloud.media.config.MinioProperties;
//import com.overthinker.cloud.media.constants.MediaStatus;
//import com.overthinker.cloud.media.entity.PO.MediaAsset;
//import com.overthinker.cloud.media.mapper.MediaAssetMapper;
//import io.minio.MinioClient;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
///**
// * 清理过期上传任务的定时任务。
// */
//@Slf4j
//@Component
//@RequiredArgsConstructor
//public class StaleUploadCleanupTask {
//
//    private final MediaAssetMapper mediaAssetMapper;
//    private final MinioClient minioClient;
//    private final MinioProperties minioProperties;
//    private final MediaProperties mediaProperties;
//
//    /**
//     * 每小时执行一次，清理僵尸分片上传任务。
//     * 只有在 `media.upload.cleanup.enabled` 为 true 时才会执行。
//     */
//    @Scheduled(cron = "0 0 * * * ?", zone = "Asia/Shanghai")
//    public void cleanupStaleUploads() {
//        if (!mediaProperties.getCleanup().isEnabled()) {
//            log.info("跳过清理过期上传任务，因为配置已被禁用。");
//            return;
//        }
//
//        log.info("开始执行清理过期上传任务...");
//
//        LocalDateTime threshold = LocalDateTime.now().minusHours(mediaProperties.getCleanup().getStaleThresholdHours());
//
//        // 查找所有状态为UPLOADING且超过阈值的记录
//        List<MediaAsset> staleAssets = mediaAssetMapper.selectList(
//                new QueryWrapper<MediaAsset>()
//                        .eq("status", MediaStatus.UPLOADING)
//                        .le("created_at", threshold)
//        );
//
//        if (staleAssets.isEmpty()) {
//            log.info("没有找到需要清理的过期上传任务。");
//            return;
//        }
//
//        log.warn("发现 {} 个过期的上传任务，将进行清理。", staleAssets.size());
//
//        for (MediaAsset asset : staleAssets) {
//            try {
//                // MinIO没有直接通过objectName中止分片上传的API，这是一个简化处理的演示。
//                // 在实际生产中，需要将uploadId与objectName一起缓存，并在清理时使用。
//                // 作为一个变通方案，我们在这里只删除数据库记录，并警告需要手动清理MinIO。
//                log.warn("正在中止与对象 {}相关的潜在分片上传。请注意：这需要您在MinIO中配置生命周期规则来自动清理未完成的分片。", asset.getObjectName());
//
//                // 更新数据库记录为失败状态，而不是直接删除，以便追踪
//                MediaAsset update = new MediaAsset().setStatus(MediaStatus.FAILED);
//                mediaAssetMapper.update(update, new QueryWrapper<MediaAsset>().eq("id", asset.getId()));
//
//                log.info("已将过期任务 {} 的状态更新为 FAILED。", asset.getObjectName());
//
//            } catch (Exception e) {
//                log.error("清理对象 {} 的过期任务时发生错误。", asset.getObjectName(), e);
//            }
//        }
//        log.info("过期上传任务清理完成。");
//    }
//}
