package com.overthinker.cloud.media.service.service.impl;

import com.overthinker.cloud.media.model.dto.QueryMediaParamsDto;
import com.overthinker.cloud.media.model.dto.UploadFileParamsDto;
import com.overthinker.cloud.media.model.dto.UploadFileResultDto;
import com.overthinker.cloud.media.model.po.MediaFiles;
import com.overthinker.cloud.media.service.mapper.MediaFilesMapper;
import com.overthinker.cloud.media.service.mapper.MediaProcessMapper;
import com.overthinker.cloud.media.service.properties.MinioProperties;
import com.overthinker.cloud.media.service.service.MediaFileService;
import com.overthinker.cloud.resp.PageParams;
import com.overthinker.cloud.resp.PageResult;
import com.overthinker.cloud.resp.ResultData;
import io.minio.MinioClient;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class MediaFileServiceImpl implements MediaFileService {

    @Resource
    MediaFilesMapper mediaFilesMapper;

    @Resource
    MinioClient minioClient;

    @Resource
    MediaProcessMapper mediaProcessMapper;

    @Resource
    private MinioProperties.BucketName bucketName;


    @Override
    public PageResult<MediaFiles> queryMediaFiles(Long companyId, PageParams pageParams, QueryMediaParamsDto queryMediaParamsDto) {
        return null;
    }

    @Override
    public UploadFileResultDto uploadFile(Long companyId, UploadFileParamsDto uploadFileParamsDto, String localFilePath, String objectName) {
        return null;
    }

    @Override
    public MediaFiles addMediaFilesToDb(Long companyId, String fileMd5, UploadFileParamsDto uploadFileParamsDto, String bucket, String objectName) {
        return null;
    }

    @Override
    public ResultData<Boolean> checkFile(String fileMd5) {
        return null;
    }

    @Override
    public ResultData<Boolean> checkChunk(String fileMd5, int chunkIndex) {
        return null;
    }

    @Override
    public ResultData uploadChunk(String fileMd5, int chunk, String localChunkFilePath) {
        return null;
    }

    @Override
    public ResultData mergeChunks(Long companyId, String fileMd5, int chunkTotal, UploadFileParamsDto uploadFileParamsDto) {
        return null;
    }

    @Override
    public File downloadFileFromMinIO(String bucket, String objectName) {
        return null;
    }

    @Override
    public boolean addMediaFilesToMinIO(String localFilePath, String mimeType, String bucket, String objectName) {
        return false;
    }

    @Override
    public MediaFiles getFileById(String mediaId) {
        return null;
    }
}
