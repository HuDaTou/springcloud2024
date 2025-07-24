package com.overthinker.cloud.media.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.overthinker.cloud.media.entity.MediaAsset;
import com.overthinker.cloud.media.service.UploadService;
import com.overthinker.cloud.resp.ResultData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/media")
@RequiredArgsConstructor
@Log4j2
@Tag(name = "媒体服务", description = "提供文件上传、下载和管理的API接口")
public class MediaController {

    private final UploadService uploadService;

    @Operation(summary = "初始化分片上传")
    @PostMapping("/initiate")
    public ResultData<Map<String, Object>> initiateMultipartUpload(
            @Parameter(name = "filename", description = "要上传的原始文件名", required = true, in = ParameterIn.QUERY)
            @RequestParam("filename") String filename,
            @Parameter(name = "totalParts", description = "文件被分割的总块数", required = true, in = ParameterIn.QUERY)
            @RequestParam("totalParts") int totalParts) throws Exception {
        return ResultData.success(uploadService.handleFirstPartAndGenerateUrls(filename, totalParts));
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
            @Parameter(name = "objectName", description = "文件的唯一对象名", required = true, in = ParameterIn.PATH)
            @PathVariable String objectName) {
        uploadService.deleteFile(objectName);
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
