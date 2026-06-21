package com.overthinker.cloud.media.controller;

import com.overthinker.cloud.common.core.annotation.LogAnnotation;
import com.overthinker.cloud.common.core.annotation.LogConst;
import com.overthinker.cloud.common.core.base.BaseController;
import com.overthinker.cloud.common.core.resp.ResultData;
import com.overthinker.cloud.media.entity.DTO.InitiateMultipartUploadDTO;
import com.overthinker.cloud.media.entity.DTO.PresignedUploadDTO;
import com.overthinker.cloud.media.service.UploadService;
import com.overthinker.cloud.system.starter.redis.annotation.AccessLimit;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * 媒体文件操作控制器
 * <p>
 * 提供文件上传、下载和URL获取的API接口（无需权限控制）
 * </p>
 *
 * @author overthinker
 * @since 2025-08-02
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/media")
@RequiredArgsConstructor
@Tag(name = "媒体文件操作", description = "提供文件上传、下载和URL获取的API接口")
public class MediaController extends BaseController {

    private final UploadService uploadService;

    @Operation(summary = "初始化分片上传", description = "初始化一个分片上传任务，返回uploadId和所有分片的预签名上传URL")
    @PostMapping("/initiate")
    @AccessLimit(seconds = 60, maxCount = 10, msg = "上传操作过于频繁，请稍后再试")
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
    @LogAnnotation(module = "媒体服务", operation = LogConst.UPDATE)
    public ResultData<Long> completeMultipartUpload(
            @Parameter(description = "从初始化调用中获取的上传ID", required = true, in = ParameterIn.QUERY)
            @NotBlank(message = "uploadId不能为空")
            @RequestParam("uploadId") String uploadId) {
        Long assetId = uploadService.completeMultipartUpload(uploadId);
        return ResultData.success(assetId);
    }

    @Operation(summary = "获取普通上传预签名URL", description = "获取小文件直接上传的预签名URL，适用于无需分片的小文件上传")
    @PostMapping("/presign")
    @AccessLimit(seconds = 60, maxCount = 20, msg = "操作过于频繁，请稍后再试")
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
    @LogAnnotation(module = "媒体服务", operation = LogConst.UPDATE)
    public ResultData<Long> completeUpload(
            @Parameter(description = "文件对象名", required = true, in = ParameterIn.QUERY)
            @NotBlank(message = "objectName不能为空")
            @RequestParam("objectName") String objectName) {
        Long assetId = uploadService.completeUpload(objectName);
        return ResultData.success(assetId);
    }

    @Operation(summary = "删除文件", description = "删除指定的媒体文件及其元数据")
    @DeleteMapping("/delete/{objectName}")
    @AccessLimit(seconds = 60, maxCount = 10, msg = "删除操作过于频繁，请稍后再试")
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
    public ResultData<String> getFileUrl(
            @Parameter(description = "文件的唯一对象名", required = true, in = ParameterIn.PATH)
            @PathVariable("objectName") @NotBlank(message = "objectName不能为空") String objectName) {
        return ResultData.success(uploadService.getPresignedFileUrl(objectName));
    }

    @Operation(summary = "获取文件强制下载URL", description = "获取指定文件的临时下载URL，浏览器会强制下载文件而不是在线预览")
    @GetMapping("/download/{objectName}")
    @AccessLimit(seconds = 60, maxCount = 30, msg = "查询操作过于频繁，请稍后再试")
    public ResultData<String> getDownloadUrl(
            @Parameter(description = "文件的唯一对象名", required = true, in = ParameterIn.PATH)
            @PathVariable("objectName") @NotBlank(message = "objectName不能为空") String objectName) {
        return ResultData.success(uploadService.getPresignedDownloadUrl(objectName));
    }

    @Operation(summary = "直接上传文件（使用上传规则）", description = "直接上传文件到服务器，根据上传规则进行校验和存储")
    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    @AccessLimit(seconds = 60, maxCount = 10, msg = "上传操作过于频繁，请稍后再试")
    @LogAnnotation(module = "媒体服务", operation = LogConst.INSERT)
    public ResultData<Map<String, Object>> uploadFile(
            @Parameter(hidden = true) @RequestHeader("X-User-Id") Long userId,
            @Parameter(description = "上传的文件", required = true) @RequestPart("file") MultipartFile file,
            @Parameter(description = "上传规则ID", required = true) @RequestParam("ruleId") Long ruleId) {
        return ResultData.success(uploadService.uploadFileWithRule(userId, file, ruleId));
    }

    @Operation(summary = "直接上传文件（使用枚举规则）", description = "根据预定义的上传规则枚举直接上传文件到服务器，无需获取预签名URL")
    @PostMapping(value = "/upload/rule", consumes = "multipart/form-data")
    @AccessLimit(seconds = 60, maxCount = 10, msg = "上传操作过于频繁，请稍后再试")
    @LogAnnotation(module = "媒体服务", operation = LogConst.INSERT)
    public ResultData<Map<String, Object>> uploadFileWithRuleName(
            @Parameter(hidden = true) @RequestHeader("X-User-Id") Long userId,
            @Parameter(description = "上传的文件", required = true) @RequestPart("file") MultipartFile file,
            @Parameter(description = "上传规则名称（枚举值，如：ARTICLE_COVER、USER_AVATAR）", required = true)
            @RequestParam("ruleName") String ruleName) {
        return ResultData.success(uploadService.uploadFileWithRuleName(userId, file, ruleName));
    }
}
