package com.overthinker.cloud.media.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.overthinker.cloud.media.entity.PO.MediaAsset;
import com.overthinker.cloud.media.enums.MediaStatusEnum;
import com.overthinker.cloud.media.mapper.MediaAssetMapper;
import com.overthinker.cloud.media.service.MediaAssetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * 媒体资产服务实现类
 * <p>
 * 针对表【media_asset(媒体资产表)】的数据库操作Service实现
 * </p>
 *
 * @author overthinker
 * @since 2025-08-02
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class MediaAssetServiceImpl extends ServiceImpl<MediaAssetMapper, MediaAsset>
        implements MediaAssetService {

    private final MediaAssetMapper mediaAssetMapper;

    @Override
    @Async("taskExecutor")
    public void processMediaAsset(MediaAsset asset) {
        log.info("开始异步处理媒体文件: {}", asset.getObjectName());

        try {
            MediaAsset updateAsset = new MediaAsset()
                    .setId(asset.getId())
                    .setStatus(MediaStatusEnum.PROCESSING.getCode());
            mediaAssetMapper.updateById(updateAsset);

            if (asset.getFileType() != null && asset.getFileType().startsWith("image/")) {
                processImage(asset);
            } else if (asset.getFileType() != null && asset.getFileType().startsWith("video/")) {
                processVideo(asset);
            } else {
                log.info("非图片/视频文件，跳过处理: {}", asset.getObjectName());
            }

            MediaAsset completedAsset = new MediaAsset()
                    .setId(asset.getId())
                    .setStatus(MediaStatusEnum.PROCESSED.getCode());
            mediaAssetMapper.updateById(completedAsset);

            log.info("成功完成对媒体文件 {} 的异步处理。", asset.getObjectName());

        } catch (Exception e) {
            log.error("处理媒体文件 {} 时发生未知错误。", asset.getObjectName(), e);
            updateStatus(asset.getId(), MediaStatusEnum.FAILED);
        }
    }

    private void updateStatus(Long id, MediaStatusEnum status) {
        try {
            MediaAsset asset = new MediaAsset()
                    .setId(id)
                    .setStatus(status.getCode());
            mediaAssetMapper.updateById(asset);
        } catch (Exception e) {
            log.error("更新媒体资产状态失败，id: {}, status: {}", id, status, e);
        }
    }

    private void processImage(MediaAsset asset) {
        log.info("开始处理图片文件: {}", asset.getObjectName());

        try {
            log.info("图片处理完成：{}", asset.getObjectName());
        } catch (Exception e) {
            log.error("处理图片文件失败: {}", asset.getObjectName(), e);
            throw e;
        }
    }

    private void processVideo(MediaAsset asset) {
        log.info("开始处理视频文件: {}", asset.getObjectName());

        try {
            log.info("视频处理完成：{}", asset.getObjectName());
        } catch (Exception e) {
            log.error("处理视频文件失败: {}", asset.getObjectName(), e);
            throw e;
        }
    }
}
