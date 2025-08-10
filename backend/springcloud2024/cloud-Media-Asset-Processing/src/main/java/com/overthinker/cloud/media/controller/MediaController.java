package com.overthinker.cloud.media.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.overthinker.cloud.common.base.BaseController;
import com.overthinker.cloud.media.entity.PO.MediaAsset;
import com.overthinker.cloud.media.service.UploadService;
import com.overthinker.cloud.common.resp.ResultData;
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
            @Parameter(name = "fileType", description = "文件类型", required = true, in = ParameterIn.QUERY)
            @RequestParam("fileType") String fileType,
            @Parameter(name = "filename", description = "要上传的原始文件名", required = true, in = ParameterIn.QUERY)
            @RequestParam("filename") String filename,
            @Parameter(name = "totalParts", description = "文件被分割的总块数", required = true, in = ParameterIn.QUERY)
            @RequestParam("totalParts") int totalParts,
            @Parameter(name = "fileSize", description = "文件总大小（以字节为单位）", required = true, in = ParameterIn.QUERY)
            @RequestParam("fileSize") long fileSize,
            @Parameter(name = "contentType", description = "文件的MIME类型", required = true, in = ParameterIn.QUERY)
            @RequestParam("contentType") String contentType,
            @Parameter(name = "fileMd5", description = "文件的MD5哈希值", required = true, in = ParameterIn.QUERY)
            @RequestParam("fileMd5") String fileMd5) throws Exception {
        return ResultData.success(uploadService.handleFirstPartAndGenerateUrls( userId, fileType, filename, totalParts, fileSize, contentType, fileMd5));
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
            @RequestParam(defaultValue = "1") int pageNum,
            @Parameter(name = "pageSize", description = "每页数量", in = ParameterIn.QUERY)
            @RequestParam(defaultValue = "10") int pageSize) {
        return ResultData.success(uploadService.listFiles(pageNum, pageSize));
    }

    @Operation(summary = "删除文件")
    @DeleteMapping("/delete/{objectName}")
    public ResultData<Void> deleteFile(
            @Parameter(hidden = true) @RequestHeader("X-User-Id") Long userId,
            @Parameter(name = "objectName", description = "文件的唯一对象名", required = true, in = ParameterIn.PATH)
            @PathVariable String objectName) {
        uploadService.deleteFile(userId, objectName);
        return ResultData.success();
    }

    @Operation(summary = "获取文件临时下载URL")
    @GetMapping("/url/{objectName}")
    public ResultData<String> getFileUrl(
            @Parameter(name = "objectName", description = "文件的唯一对象名", required = true, in = ParameterIn.PATH)
            @PathVariable String objectName) {
        return ResultData.success(uploadService.getPresignedFileUrl(objectName));
    }
}
