package com.overthinker.cloud.api.apis.media.api;

import com.overthinker.cloud.api.apis.media.VO.MediaAssetVO;
import com.overthinker.cloud.common.core.resp.ResultData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * 媒体资产 Feign 客户端
 * <p>
 * 提供媒体资产管理接口，供内部服务调用
 * </p>
 *
 * @author overthinker
 * @since 2025-08-02
 */
@FeignClient(name = "cloud-media-asset-processing", contextId = "mediaAssetClient")
public interface MediaAssetClient {

    /**
     * 分页列出媒体资产
     *
     * @param pageNo   页码
     * @param pageSize 每页数量
     * @return 媒体资产分页列表
     */
    @GetMapping("/media/assets")
    ResultData<Map<String, Object>> listFiles(
            @RequestParam(value = "pageNo", defaultValue = "1") Long pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10") Long pageSize);

    /**
     * 获取媒体资产详情
     *
     * @param id 媒体资产ID
     * @return 媒体资产详情
     */
    @GetMapping("/media/assets/{id}")
    ResultData<MediaAssetVO> getAssetDetail(@PathVariable("id") Long id);
}
