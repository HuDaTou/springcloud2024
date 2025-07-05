package com.overthinker.cloud.media.controller;


import com.overthinker.cloud.media.service.UploadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/media")
@RequiredArgsConstructor
@Log4j2
@Tag(name = "媒体文件上传接口", description = "提供分片上传相关接口")
public class MediaController {

    private final UploadService uploadService;

    /**
     * 初始化分片上传任务，并返回第一个分片的 Presigned URL
     */
    @Operation(
            summary = "初始化分片上传并生成所有分片的预签名URL",
            description = "前端上传第一个分片后，系统将自动生成后续分片的预签名URL，用于浏览器直传MinIO。",
            tags = {"Media Upload"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "成功获取预签名URL列表", content = @Content(schema = @Schema(implementation = Map.class))),
                    @ApiResponse(responseCode = "500", description = "内部服务器错误")
            }
    )
    @PostMapping("/initiate")
    public ResponseEntity<Map<String, Object>> initiateMultipartUpload(
            @Parameter(name = "filename", description = "上传文件的原始名称（如：video.mp4）", required = true, in = ParameterIn.QUERY)
            @RequestParam("filename") String filename,

            @Parameter(name = "totalParts", description = "文件被切分的总分片数", example = "5", required = true, in = ParameterIn.QUERY)
            @RequestParam("totalParts") int totalParts) throws Exception {

        Map<String, Object> result = uploadService.handleFirstPartAndGenerateUrls(filename, totalParts);
        return ResponseEntity.ok(result);
    }
}
