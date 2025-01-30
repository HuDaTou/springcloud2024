package com.overthinker.cloud.media_api.controller;

import com.overthinker.cloud.media.model.dto.QueryMediaParamsDto;
import com.overthinker.cloud.media.model.dto.UploadFileParamsDto;
import com.overthinker.cloud.media.model.dto.UploadFileResultDto;
import com.overthinker.cloud.media.model.po.MediaFiles;
import com.overthinker.cloud.media.service.service.MediaFileService;
import com.overthinker.cloud.resp.PageParams;
import com.overthinker.cloud.resp.PageResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@Tag(name = "媒资文件管理接口")
public class MediaFilesController {

    @Resource
    MediaFileService mediaFileService;

    @Operation(summary = "媒资列表查询接口")
    @PostMapping("/files")
    public PageResult<MediaFiles> list(PageParams pageParams, @RequestBody QueryMediaParamsDto queryMediaParamsDto) {
        Long companyId = 1232141425L;
        return mediaFileService.queryMediaFiles(companyId, pageParams, queryMediaParamsDto);
    }

    @Operation(summary = "上传图片")
    @PostMapping("/upload/coursefile")
    public UploadFileResultDto upload(@RequestPart("filedata") MultipartFile filedata,
                                      @RequestParam(value = "objectName", required = false) String objectName) throws IOException {

        //准备上传文件的信息
        UploadFileParamsDto uploadFileParamsDto = new UploadFileParamsDto();
        //原始文件名称
        uploadFileParamsDto.setFilename(filedata.getOriginalFilename());
        //文件大小
        uploadFileParamsDto.setFileSize(filedata.getSize());
        //文件类型
        uploadFileParamsDto.setFileType("001001");
        //创建一个临时文件
        File tempFile = File.createTempFile("minio", ".temp");
        filedata.transferTo(tempFile);
        Long companyId = 1232141425L;
        //文件路径
        String localFilePath = tempFile.getAbsolutePath();

        //调用service上传图片
        UploadFileResultDto uploadFileResultDto = mediaFileService.uploadFile(companyId, uploadFileParamsDto, localFilePath, objectName);

        return uploadFileResultDto;
    }

//    @PostMapping("/init/fileData")
//    public ResultData initFileData(@RequestBody @Valid InitSliceDataReq initSliceDataReq){
//
//
//
//    }
}
