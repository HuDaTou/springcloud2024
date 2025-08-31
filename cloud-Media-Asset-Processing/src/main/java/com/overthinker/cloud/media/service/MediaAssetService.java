package com.overthinker.cloud.media.service;

import com.overthinker.cloud.media.entity.PO.MediaAsset;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author overh
* @description 针对表【media_asset(媒体资产表)】的数据库操作Service
* @createDate 2025-08-02 17:21:17
*/
public interface MediaAssetService extends IService<MediaAsset> {

    /**
     * 异步处理上传成功的媒体文件。
     * <p>
     * 根据文件类型，此方法可以触发不同的处理流程，
     * 例如为图片生成缩略图或为视频进行转码。
     *
     * @param asset 刚刚上传成功的媒体资产对象
     */
    void processMediaAsset(MediaAsset asset);

}
