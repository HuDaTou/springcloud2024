package com.overthinker.cloud.api.apis.media.api;

import com.overthinker.cloud.api.apis.media.DTO.InitiateMultipartUploadRequest;
import com.overthinker.cloud.api.apis.media.DTO.PresignedUploadRequest;
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
    @PostMapping("/media/initiate")
    ResultData<Map<String, Object>> initiateMultipartUpload(
            @RequestHeader("X-User-Id") Long userId,
            @RequestBody InitiateMultipartUploadRequest request);

    /**
     * 完成分片上传
     *
     * @param uploadId 初始化时获取的上传ID
     * @return 操作结果
     */
    @PostMapping("/media/complete")
    ResultData<Long> completeMultipartUpload(@RequestParam("uploadId") String uploadId);

    /**
     * 获取普通上传预签名URL
     *
     * @param userId  上传用户ID
     * @param request 上传参数
     * @return 包含 objectName、assetId 和 presignedUrl 的结果
     */
    @PostMapping("/media/presign")
    ResultData<Map<String, Object>> getPresignedUploadUrl(
            @RequestHeader("X-User-Id") Long userId,
            @RequestBody PresignedUploadRequest request);

    /**
     * 完成普通上传
     *
     * @param objectName 文件对象名
     * @return 操作结果
     */
    @PostMapping("/media/presign/complete")
    ResultData<Long> completeUpload(@RequestParam("objectName") String objectName);

    /**
     * 删除文件
     *
     * @param userId     操作用户ID
     * @param objectName 文件对象名
     * @return 操作结果
     */
    @DeleteMapping("/media/delete/{objectName}")
    ResultData<Void> deleteFile(
            @RequestHeader("X-User-Id") Long userId,
            @PathVariable("objectName") String objectName);

    /**
     * 获取文件浏览URL
     *
     * @param objectName 文件对象名
     * @return 文件浏览URL
     */
    @GetMapping("/media/url/{objectName}")
    ResultData<String> getFileUrl(@PathVariable("objectName") String objectName);

    /**
     * 获取文件强制下载URL
     *
     * @param objectName 文件对象名
     * @return 文件下载URL
     */
    @GetMapping("/media/download/{objectName}")
    ResultData<String> getDownloadUrl(@PathVariable("objectName") String objectName);

    /**
     * 直接上传文件（使用枚举规则）
     * <p>
     * 根据预定义的上传规则枚举直接上传文件到服务器，无需获取预签名URL
     * </p>
     *
     * @param userId     上传用户ID
     * @param file       上传的文件
     * @param ruleName   上传规则名称（枚举值）
     * @return 包含 assetId、objectName、fileUrl 等信息的Map
     */
    @PostMapping(value = "/media/upload/rule", consumes = "multipart/form-data")
    ResultData<Map<String, Object>> uploadFileWithRuleName(
            @RequestHeader("X-User-Id") Long userId,
            @RequestPart("file") org.springframework.web.multipart.MultipartFile file,
            @RequestParam("ruleName") String ruleName);
}
