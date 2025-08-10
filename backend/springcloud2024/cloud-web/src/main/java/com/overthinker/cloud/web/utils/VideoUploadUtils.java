package com.overthinker.cloud.web.utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileNameUtil;
import com.overthinker.cloud.common.resp.ReturnCodeEnum;
import com.overthinker.cloud.web.entity.enums.UploadEnum;
import com.overthinker.cloud.web.exception.FileUploadException;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Component
public class VideoUploadUtils {
    // 封面支持格式

    private static final int MAX_COVER_SIZE_MB = 5;

    @Resource
    private MinioClient client;

    @Value("${minio.bucketName}")
    private String bucketName;

    @Value("${minio.endpoint}")
    private String endpoint;


    /**
     * 统一上传到MinIO
     */
    public String uploadToMinio(String objectName, MultipartFile file) {
        try (InputStream inputStream = file.getInputStream()) {
            client.putObject(PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .stream(inputStream, file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build());
            return objectName;
        } catch (Exception e) {
            log.error("MinIO上传失败: {}", objectName, e);
            throw new FileUploadException(ReturnCodeEnum.FILE_UPLOAD_ERROR);
        }
    }

    /**
     * 生成用户基础路径如果为公共视频则不需要用户id
     */
    public String buildPath(UploadEnum config, String fileName) {
        String name = UUID.randomUUID().toString();
        return config.getDir() + name + "." + FileNameUtil.extName(fileName);
    }


    /**
     * 文件校验
     *
     * @param config 配置
     * @param file   文件
     * @throws FileUploadException 文件上传异常
     */
    public void validateFile(UploadEnum config, MultipartFile file) {
        // 校验
        if (file.isEmpty()) throw new FileUploadException(ReturnCodeEnum.FILE_VIDEO_ERROR);
        if (file.getSize() > config.getLimitSize() * 1024 * 1024) {
            throw new FileUploadException(ReturnCodeEnum.FILE_VIDEO_SIZE_ERROR);
        }
        if (!isValidFormat(file, config.getFormat())) {
            throw new FileUploadException(ReturnCodeEnum.FILE_VIDEO_TYPE_ERROR);
        }

    }

    /**
     * 封面校验
     *
     * @param config 配置
     * @param cover  封面
     * @throws FileUploadException 文件上传异常
     */
    public void validateVideoCover(UploadEnum config, MultipartFile cover) throws FileUploadException {
        if (cover.isEmpty()) throw new FileUploadException(ReturnCodeEnum.FILE_IMAGE_ERROR);
        if (cover.getSize() > config.getLimitSize() * 1024 * 1024) {
            throw new FileUploadException(ReturnCodeEnum.FILE_IMAGE_SIZE_ERROR);
        }
        if (!isValidFormat(cover, config.getFormat())) {
            throw new FileUploadException(ReturnCodeEnum.FILE_IMAGE_TYPE_ERROR);
        }
    }

    /**
     * 增强格式校验 校验文件格式是否合法 合法返回true 不合法返回false
     *
     * @param file 文件
     */
    private boolean isValidFormat(MultipartFile file, Set<String> allowedExtensions) {

        String fileName = file.getOriginalFilename();
        String fileFormat = FileUtil.extName(fileName);
        if (fileFormat == null) {
            return false;
        }
        return allowedExtensions.contains(fileFormat);
    }

    /**
     * 清理部分上传的文件
     */
    public void cleanupPartialUploads(String... objectNames) {
        Arrays.stream(objectNames).forEach(name -> {
            try {
                client.removeObject(RemoveObjectArgs.builder()
                        .bucket(bucketName)
                        .object(name)
                        .build());
            } catch (Exception e) {
                log.warn("文件清理失败: {}", name, e);
            }
        });
    }


    /**
     * 将视频文件大小转换成合适的单位
     */
    public String convertVideoSize(long size) {
        if (size < 0) {
            throw new IllegalArgumentException("文件大小不能为负数: " + size);
        }

        String[] units = {"B", "KB", "MB", "GB", "TB", "PB", "EB"};
        if (size == 0) {
            return "0B";
        }

        int unitIndex = 0;
        double formattedSize = size;

        // 找到适合的单位
        while (formattedSize >= 1024 && unitIndex < units.length - 1) {
            formattedSize /= 1024;
            unitIndex++;
        }

        // 格式化数值，移除不必要的零
        String sizeString;
        if (formattedSize == (long) formattedSize) {
            sizeString = String.format("%d", (long) formattedSize); // 整数部分无小数
        } else {
            sizeString = String.format("%.1f", formattedSize).replace(".0", ""); // 保留一位小数并去除.0
        }

        return sizeString + units[unitIndex];
    }


    /**
     * 给地址加上域名
     */
    public String addEndpoint(String objectName) {
        return endpoint + "/" + bucketName + "/" + objectName;
    }
}