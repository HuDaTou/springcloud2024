package com.overthinker.cloud.media.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.overthinker.cloud.media.entity.DTO.InitiateMultipartUploadDTO;
import com.overthinker.cloud.media.entity.VO.MediaAssetVO;

import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传服务接口
 *
 * @author overthinker
 * @since 2025-08-02
 */
public interface UploadService {

    /**
     * 初始化分片上传任务，并返回所有分片的预签名URL
     * <p>
     * 此方法包含以下步骤：
     * 1. 校验上传参数（文件大小、类型等）
     * 2. 检查是否可以秒传（基于MD5）
     * 3. 初始化MinIO分片上传任务
     * 4. 生成所有分片的预签名上传URL
     * </p>
     *
     * @param userId                   上传用户ID
     * @param initiateMultipartUploadDTO 初始化上传参数
     * @return 包含 uploadId、objectName 和预签名URL映射的Map
     * @throws Exception 如果初始化失败
     */
    Map<String, Object> handleFirstPartAndGenerateUrls(Long userId, InitiateMultipartUploadDTO initiateMultipartUploadDTO) throws Exception;

    /**
     * 完成分片上传
     * <p>
     * 此方法包含以下步骤：
     * 1. 校验上传会话是否有效
     * 2. 查询已上传的分片
     * 3. 合并分片
     * 4. 完整性校验（MD5校验）
     * 5. 更新数据库状态
     * 6. 触发异步媒体处理
     * </p>
     *
     * @param uploadId 初始化时获取的上传ID
     * @return 完成的媒体资产ID
     */
    Long completeMultipartUpload(String uploadId);

    /**
     * 分页列出媒体文件
     *
     * @param pageNum  页码
     * @param pageSize 每页数量
     * @return 媒体资产的分页列表
     */
    Page<MediaAssetVO> listFiles(int pageNum, int pageSize);

    /**
     * 根据ID获取媒体资产详情
     *
     * @param id 媒体资产ID
     * @return 媒体资产VO
     */
    MediaAssetVO getAssetById(Long id);

    /**
     * 从存储桶中删除一个文件
     * <p>
     * 此方法会检查是否还有其他数据库记录引用此MinIO对象，
     * 如果没有其他引用，则同时删除物理文件。
     * </p>
     *
     * @param userId     执行删除操作的用户ID
     * @param objectName 要删除的文件的唯一对象名
     */
    void deleteFile(Long userId, String objectName);

    /**
     * 获取用于下载文件的预签名URL
     *
     * @param objectName 文件的唯一对象名
     * @return 用于下载的预签名URL
     */
    String getPresignedFileUrl(String objectName);

    /**
     * 获取缩略图的预签名URL
     *
     * @param thumbnailName 缩略图对象名
     * @return 用于下载缩略图的预签名URL，如果没有缩略图则返回null
     */
    String getPresignedThumbnailUrl(String thumbnailName);

    /**
     * 获取用于强制下载文件的预签名URL
     * <p>
     * 此URL会强制浏览器下载文件而不是在浏览器中预览
     * </p>
     *
     * @param objectName 文件的唯一对象名
     * @return 用于强制下载的预签名URL
     */
    String getPresignedDownloadUrl(String objectName);

    /**
     * 获取普通上传的预签名URL
     * <p>
     * 适用于小文件直接上传，无需分片
     * </p>
     *
     * @param userId      上传用户ID
     * @param fileName    文件名
     * @param fileSize    文件大小
     * @param contentType 文件MIME类型
     * @param fileMd5     文件MD5
     * @param fileType    文件类型ID
     * @return 包含 objectName、assetId 和 presignedUrl 的Map
     */
    Map<String, Object> getPresignedUploadUrl(Long userId, String fileName, long fileSize, String contentType, String fileMd5, Long fileType);

    /**
     * 完成普通上传
     * <p>
     * 上传完成后调用此方法更新文件状态并触发后续处理
     * </p>
     *
     * @param objectName 文件对象名
     * @return 完成的媒体资产ID
     */
    Long completeUpload(String objectName);

    /**
     * 使用上传规则直接上传文件
     * <p>
     * 根据指定的上传规则校验文件并上传到MinIO，同时创建媒体资产记录
     * </p>
     *
     * @param userId  上传用户ID
     * @param file    上传的文件
     * @param ruleId  上传规则ID
     * @return 包含 assetId、objectName、fileUrl 等信息的Map
     */
    Map<String, Object> uploadFileWithRule(Long userId, MultipartFile file, Long ruleId);

    /**
     * 使用枚举规则直接上传文件
     * <p>
     * 根据预定义的上传规则枚举校验文件并上传到MinIO，同时创建媒体资产记录
     * </p>
     *
     * @param userId   上传用户ID
     * @param file     上传的文件
     * @param ruleName 上传规则名称（枚举值，如：ARTICLE_COVER、USER_AVATAR）
     * @return 包含 assetId、objectName、fileUrl 等信息的Map
     */
    Map<String, Object> uploadFileWithRuleName(Long userId, MultipartFile file, String ruleName);
}
