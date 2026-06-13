package com.overthinker.cloud.api.apis.media.api;

import com.overthinker.cloud.api.apis.media.dto.InitiateMultipartUploadRequest;
import com.overthinker.cloud.api.apis.media.dto.PresignedUploadRequest;
import com.overthinker.cloud.api.config.FeignClientCredentialsConfig;
import com.overthinker.cloud.common.core.resp.ResultData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 媒体服务 Feign 客户端
 * <p>
 * 提供文件上传、下载和管理接口，供内部服务调用
 * </p>
 *
 * @author overthinker
 * @since 2025-08-02
 */
@FeignClient(name = "cloud-media-asset-processing", configuration = FeignClientCredentialsConfig.class)
public interface MediaClient {

    /**
     * 初始化分片上传
     *
     * @param userId  上传用户ID
     * @param request 初始化上传参数
     * @return 包含 uploadId、objectName 和预签名URL映射的结果
     */
    @PostMapping("/api/media/initiate")
    ResultData<Map<String, Object>> initiateMultipartUpload(
            @RequestHeader("X-User-Id") Long userId,
            @RequestBody InitiateMultipartUploadRequest request);

    /**
     * 完成分片上传
     *
     * @param uploadId 初始化时获取的上传ID
     * @return 操作结果
     */
    @PostMapping("/api/media/complete")
    ResultData<Void> completeMultipartUpload(@RequestParam("uploadId") String uploadId);

    /**
     * 获取普通上传预签名URL
     *
     * @param userId  上传用户ID
     * @param request 上传参数
     * @return 包含 objectName、assetId 和 presignedUrl 的结果
     */
    @PostMapping("/api/media/presign")
    ResultData<Map<String, Object>> getPresignedUploadUrl(
            @RequestHeader("X-User-Id") Long userId,
            @RequestBody PresignedUploadRequest request);

    /**
     * 完成普通上传
     *
     * @param objectName 文件对象名
     * @return 操作结果
     */
    @PostMapping("/api/media/presign/complete")
    ResultData<Void> completeUpload(@RequestParam("objectName") String objectName);

    /**
     * 分页列出媒体文件
     *
     * @param pageNo   页码
     * @param pageSize 每页数量
     * @return 媒体资产分页列表
     */
    @GetMapping("/api/media/list")
    ResultData<Map<String, Object>> listFiles(
            @RequestParam(value = "pageNo", defaultValue = "1") Long pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10") Long pageSize);

    /**
     * 获取媒体资产详情
     *
     * @param id 媒体资产ID
     * @return 媒体资产详情
     */
    @GetMapping("/api/media/{id}")
    ResultData<Map<String, Object>> getAssetDetail(@PathVariable("id") Long id);

    /**
     * 删除文件
     *
     * @param userId     操作用户ID
     * @param objectName 文件对象名
     * @return 操作结果
     */
    @DeleteMapping("/api/media/delete/{objectName}")
    ResultData<Void> deleteFile(
            @RequestHeader("X-User-Id") Long userId,
            @PathVariable("objectName") String objectName);

    /**
     * 获取文件浏览URL
     *
     * @param objectName 文件对象名
     * @return 文件浏览URL
     */
    @GetMapping("/api/media/url/{objectName}")
    ResultData<String> getFileUrl(@PathVariable("objectName") String objectName);

    /**
     * 获取文件强制下载URL
     *
     * @param objectName 文件对象名
     * @return 文件下载URL
     */
    @GetMapping("/api/media/download/{objectName}")
    ResultData<String> getDownloadUrl(@PathVariable("objectName") String objectName);
}
