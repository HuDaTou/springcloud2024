package com.overthinker.cloud.media.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.j256.simplemagic.ContentInfo;
import com.j256.simplemagic.ContentInfoUtil;
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
import io.minio.UploadObjectArgs;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Service
public class MediaFileServiceImpl implements MediaFileService {

    private static final Logger log = LoggerFactory.getLogger(MediaFileServiceImpl.class);
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
//        构建查询条件对象
        LambdaQueryWrapper<MediaFiles> queryWrapper = new LambdaQueryWrapper<>();
//        分页对象
        Page<MediaFiles> page = new Page<>(pageParams.getPageNo(), pageParams.getPageSize());
//        查询数据内容获得结果
        Page<MediaFiles> mediaFilesPage = mediaFilesMapper.selectPage(page, queryWrapper);
//        获取数据列表
        List<MediaFiles> list = mediaFilesPage.getRecords();
//        获取数据总数
        long total = mediaFilesPage.getTotal();
//        构建结果集
        return new PageResult<>(list, total , pageParams.getPageNo(), pageParams.getPageSize());
    }

    private String getMimeType(String extension) {
        if (extension == null) {
            extension = "";
        }
        //根据扩展名取出mimeType
        ContentInfo extensionMatch = ContentInfoUtil.findExtensionMatch(extension);
        //通用mimeType，字节流
        String mimeType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
        if (extensionMatch != null) {
            mimeType = extensionMatch.getMimeType();
        }
        return mimeType;
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
        try {
            UploadObjectArgs uploadObjectArgs = UploadObjectArgs.builder()
                    .bucket(bucket)//桶
                    .filename(localFilePath) //指定本地文件路径
                    .object(objectName)//对象名 放在子目录下
                    .contentType(mimeType)//设置媒体文件类型
                    .build();
            //上传文件
            minioClient.uploadObject(uploadObjectArgs);
            log.debug("上传文件到minio成功,bucket:{},objectName:{},错误信息:{}", bucket, objectName);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("上传文件出错,bucket:{},objectName:{},错误信息:{}", bucket, objectName, e.getMessage());
        }
        return false;
    }

    @Override
    public MediaFiles getFileById(String mediaId) {
        return null;
    }
}
