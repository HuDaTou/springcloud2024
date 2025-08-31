package com.overthinker.cloud.media.service;

import com.overthinker.cloud.media.entity.DTO.InitiateMultipartUploadDTO;

import com.overthinker.cloud.media.entity.PO.MediaAsset;

import java.util.Map;

public interface UploadService {


    /**
     * 初始化分片上传任务，并返回所有分片的预签名URL。
     *
     * @param initiateMultipartUploadDTO        上传类型
     * @param userId      上传用户ID

     * @return 包含uploadId、objectName和预签名URL映射的Map
     */
    Map<String, Object> handleFirstPartAndGenerateUrls(Long userId, InitiateMultipartUploadDTO initiateMultipartUploadDTO) throws Exception;

    /**
     * 完成分片上传。
     *
     * @param uploadId 初始化时获取的上传ID
     */
    void completeMultipartUpload(String uploadId);

    /**
     * 分页列出存储桶中的文件。
     *
     * @param pageNum  页码
     * @param pageSize 每页数量
     * @return 文件资产的分页列表
     */
    com.baomidou.mybatisplus.extension.plugins.pagination.Page<MediaAsset> listFiles(int pageNum, int pageSize);

    /**
     * 从存储桶中删除一个文件。
     *
     * @param userId     执行删除操作的用户ID
     * @param objectName 要删除的文件的唯一对象名
     */
    void deleteFile(Long userId, String objectName);

    /**
     * 获取用于下载文件的预签名URL。
     *
     * @param objectName 文件的唯一对象名
     * @return 用于下载的预签名URL
     */
    String getPresignedFileUrl(String objectName);
}
