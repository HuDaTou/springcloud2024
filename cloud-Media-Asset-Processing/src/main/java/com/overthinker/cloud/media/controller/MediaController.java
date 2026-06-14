package com.overthinker.cloud.media.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.overthinker.cloud.common.core.annotation.LogAnnotation;
import com.overthinker.cloud.common.core.annotation.LogConst;
import com.overthinker.cloud.common.core.base.BaseController;
import com.overthinker.cloud.common.core.resp.ResultData;
import com.overthinker.cloud.common.core.resp.PageParams;
import com.overthinker.cloud.media.entity.DTO.InitiateMultipartUploadDTO;
import com.overthinker.cloud.media.entity.DTO.PresignedUploadDTO;
import com.overthinker.cloud.media.entity.VO.MediaAssetVO;
import com.overthinker.cloud.media.service.UploadService;
import com.overthinker.cloud.system.starter.redis.annotation.AccessLimit;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 媒体服务控制器
 * <p>
 * 提供文件上传、下载和管理的API接口
 * </p>
 *
 * @author overthinker
 * @since 2025-08-02
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/api/media")
@RequiredArgsConstructor
@Tag(name = "媒体上传", description = "提供文件上传、下载和管理的API接口")
public class MediaController extends BaseController {

    private final UploadService uploadService;

    @Operation(summary = "初始化分片上传", description = "初始化一个分片上传任务，返回uploadId和所有分片的预签名上传URL")
    @PostMapping("/initiate")
    @AccessLimit(seconds = 60, maxCount = 10, msg = "上传操作过于频繁，请稍后再试")
    @PreAuthorize("hasAuthority('media:asset:upload')")
    @LogAnnotation(module = "媒体服务", operation = LogConst.INSERT)
    public ResultData<Map<String, Object>> initiateMultipartUpload(
            @Parameter(hidden = true) @RequestHeader("X-User-Id") Long userId,
            @RequestBody @Valid @Parameter(description = "初始化分片上传的参数", required = true) 
            InitiateMultipartUploadDTO initiateMultipartUploadDTO) throws Exception {
        return ResultData.success(uploadService.handleFirstPartAndGenerateUrls(userId, initiateMultipartUploadDTO));
    }

    @Operation(summary = "完成分片上传", description = "在所有分片上传完成后，通知服务器合并分片")
    @PostMapping("/complete")
    @AccessLimit(seconds = 60, maxCount = 20, msg = "操作过于频繁，请稍后再试")
    @PreAuthorize("hasAuthority('media:asset:upload')")
    @LogAnnotation(module = "媒体服务", operation = LogConst.UPDATE)
    public ResultData<Void> completeMultipartUpload(
            @Parameter(description = "从初始化调用中获取的上传ID", required = true, in = ParameterIn.QUERY)
            @NotBlank(message = "uploadId不能为空")
            @RequestParam("uploadId") String uploadId) {
        uploadService.completeMultipartUpload(uploadId);
        return ResultData.success();
    }

    @Operation(summary = "获取普通上传预签名URL", description = "获取小文件直接上传的预签名URL，适用于无需分片的小文件上传")
    @PostMapping("/presign")
    @AccessLimit(seconds = 60, maxCount = 20, msg = "操作过于频繁，请稍后再试")
    @PreAuthorize("hasAuthority('media:asset:upload')")
    @LogAnnotation(module = "媒体服务", operation = LogConst.INSERT)
    public ResultData<Map<String, Object>> getPresignedUploadUrl(
            @Parameter(hidden = true) @RequestHeader("X-User-Id") Long userId,
            @RequestBody @Valid @Parameter(description = "普通上传参数", required = true)
            PresignedUploadDTO presignedUploadDTO) {
        return ResultData.success(uploadService.getPresignedUploadUrl(
                userId,
                presignedUploadDTO.fileName(),
                presignedUploadDTO.fileSize(),
                presignedUploadDTO.contentType(),
                presignedUploadDTO.fileMd5(),
                presignedUploadDTO.fileType()
        ));
    }

    @Operation(summary = "完成普通上传", description = "在普通上传完成后，通知服务器更新文件状态并触发后续处理")
    @PostMapping("/presign/complete")
    @AccessLimit(seconds = 60, maxCount = 20, msg = "操作过于频繁，请稍后再试")
    @PreAuthorize("hasAuthority('media:asset:upload')")
    @LogAnnotation(module = "媒体服务", operation = LogConst.UPDATE)
    public ResultData<Void> completeUpload(
            @Parameter(description = "文件对象名", required = true, in = ParameterIn.QUERY)
            @NotBlank(message = "objectName不能为空")
            @RequestParam("objectName") String objectName) {
        uploadService.completeUpload(objectName);
        return ResultData.success();
    }

    @Operation(summary = "分页列出媒体文件", description = "分页查询已上传的媒体资产列表")
    @GetMapping("/list")
    @AccessLimit(seconds = 60, maxCount = 30, msg = "查询操作过于频繁，请稍后再试")
    @PreAuthorize("hasAuthority('media:asset:list')")
    public ResultData<Page<MediaAssetVO>> listFiles(
            @Parameter(description = "分页参数") @ModelAttribute PageParams pageParams) {
        return ResultData.success(uploadService.listFiles(pageParams.getPageNo().intValue(), pageParams.getPageSize().intValue()));
    }

    @Operation(summary = "获取媒体资产详情", description = "根据ID获取媒体资产的详细信息，包含下载URL")
    @GetMapping("/{id}")
    @AccessLimit(seconds = 60, maxCount = 30, msg = "查询操作过于频繁，请稍后再试")
    @PreAuthorize("hasAuthority('media:asset:query')")
    public ResultData<MediaAssetVO> getAssetDetail(
            @Parameter(description = "媒体资产ID", required = true, in = ParameterIn.PATH)
            @PathVariable("id") @NotNull(message = "ID不能为空") Long id) {
        return ResultData.success(uploadService.getAssetById(id));
    }

    @Operation(summary = "删除文件", description = "删除指定的媒体文件及其元数据")
    @DeleteMapping("/delete/{objectName}")
    @AccessLimit(seconds = 60, maxCount = 10, msg = "删除操作过于频繁，请稍后再试")
    @PreAuthorize("hasAuthority('media:asset:delete')")
    @LogAnnotation(module = "媒体服务", operation = LogConst.DELETE)
    public ResultData<Void> deleteFile(
            @Parameter(hidden = true) @RequestHeader("X-User-Id") Long userId,
            @Parameter(description = "文件的唯一对象名", required = true, in = ParameterIn.PATH)
            @PathVariable("objectName") @NotBlank(message = "objectName不能为空") String objectName) {
        uploadService.deleteFile(userId, objectName);
        return ResultData.success();
    }

    @Operation(summary = "获取文件浏览URL", description = "获取指定文件的临时浏览URL，浏览器会尝试在线预览（适用于图片、视频、PDF等）")
    @GetMapping("/url/{objectName}")
    @AccessLimit(seconds = 60, maxCount = 30, msg = "查询操作过于频繁，请稍后再试")
    @PreAuthorize("hasAuthority('media:asset:query')")
    public ResultData<String> getFileUrl(
            @Parameter(description = "文件的唯一对象名", required = true, in = ParameterIn.PATH)
            @PathVariable("objectName") @NotBlank(message = "objectName不能为空") String objectName) {
        return ResultData.success(uploadService.getPresignedFileUrl(objectName));
    }

    @Operation(summary = "获取文件强制下载URL", description = "获取指定文件的临时下载URL，浏览器会强制下载文件而不是在线预览")
    @GetMapping("/download/{objectName}")
    @AccessLimit(seconds = 60, maxCount = 30, msg = "查询操作过于频繁，请稍后再试")
    @PreAuthorize("hasAuthority('media:asset:query')")
    public ResultData<String> getDownloadUrl(
            @Parameter(description = "文件的唯一对象名", required = true, in = ParameterIn.PATH)
            @PathVariable("objectName") @NotBlank(message = "objectName不能为空") String objectName) {
        return ResultData.success(uploadService.getPresignedDownloadUrl(objectName));
    }
}
