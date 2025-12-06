package com.overthinker.cloud.media.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.overthinker.cloud.common.core.base.BaseController;
import com.overthinker.cloud.common.core.resp.ResultData;
import com.overthinker.cloud.media.entity.DTO.InitiateMultipartUploadDTO;
import com.overthinker.cloud.media.entity.PO.MediaAsset;
import com.overthinker.cloud.media.service.UploadService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/media")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "媒体服务", description = "提供文件上传、下载和管理的API接口")
public class MediaController extends BaseController {

    private final UploadService uploadService;

    @Operation(summary = "初始化分片上传")
    @PostMapping("/initiate")
    public ResultData<Map<String, Object>> initiateMultipartUpload(
            @Parameter(hidden = true) @RequestHeader("X-User-Id") Long userId,
            @RequestBody @Parameter(name = "initiateMultipartUploadDTO", description = "初始化分片上传的参数", required = true) InitiateMultipartUploadDTO initiateMultipartUploadDTO) throws Exception {
        return ResultData.success(uploadService.handleFirstPartAndGenerateUrls( userId, initiateMultipartUploadDTO));
    }

    @Operation(summary = "完成分片上传")
    @PostMapping("/complete")
    public ResultData<Void> completeMultipartUpload(
            @Parameter(name = "uploadId", description = "从初始化调用中获取的上传ID", required = true, in = ParameterIn.QUERY)
            @RequestParam("uploadId") String uploadId) {
        uploadService.completeMultipartUpload(uploadId);
        return ResultData.success();
    }

    @Operation(summary = "分页列出文件")
    @GetMapping("/list")
    public ResultData<Page<MediaAsset>> listFiles(
            @Parameter(name = "pageNum", description = "页码", in = ParameterIn.QUERY)
            @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
            @Parameter(name = "pageSize", description = "每页数量", in = ParameterIn.QUERY)
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        return ResultData.success(uploadService.listFiles(pageNum, pageSize));
    }

    @Operation(summary = "删除文件")
    @DeleteMapping("/delete/{objectName}")
    public ResultData<Void> deleteFile(
            @Parameter(hidden = true) @RequestHeader("X-User-Id") Long userId,
            @Parameter(name = "objectName", description = "文件的唯一对象名", required = true, in = ParameterIn.PATH)
            @PathVariable("objectName") String objectName) {
        uploadService.deleteFile(userId, objectName);
        return ResultData.success();
    }

    @Operation(summary = "获取文件临时下载URL")
    @GetMapping("/url/{objectName}")
    public ResultData<String> getFileUrl(
            @Parameter(name = "objectName", description = "文件的唯一对象名", required = true, in = ParameterIn.PATH)
            @PathVariable("objectName") String objectName) {
        return ResultData.success(uploadService.getPresignedFileUrl(objectName));
    }
}
