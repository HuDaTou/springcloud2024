package com.overthinker.cloud.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.overthinker.cloud.api.apis.media.ENUM.MediaUploadRuleEnum;
import com.overthinker.cloud.api.apis.media.api.MediaClient;
import com.overthinker.cloud.common.core.resp.ResultData;
import com.overthinker.cloud.common.core.resp.ReturnCodeEnum;
import com.overthinker.cloud.web.entity.DTO.DeletePhotoOrAlbumDTO;
import com.overthinker.cloud.web.entity.DTO.PhotoAlbumDTO;
import com.overthinker.cloud.web.entity.PO.Photo;
import com.overthinker.cloud.web.entity.VO.PageVO;
import com.overthinker.cloud.web.entity.VO.PhotoAndAlbumListVO;
import com.overthinker.cloud.web.entity.enums.AlbumOrPhotoEnum;
import com.overthinker.cloud.web.mapper.PhotoMapper;
import com.overthinker.cloud.web.service.PhotoService;
import com.overthinker.cloud.system.starter.auth.utils.SecurityUtils;
import com.overthinker.cloud.common.core.utils.MyStringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.overthinker.cloud.web.entity.constants.SQLConst.LIMIT_ONE_SQL;
import static com.overthinker.cloud.web.entity.constants.SQLConst.ORDER_BY_CREATE_TIME_DESC;

@Log4j2
@Service("photosService")
@RequiredArgsConstructor
public class PhotoServiceImpl extends ServiceImpl<PhotoMapper, Photo> implements PhotoService {

    private final PhotoMapper photoMapper;
    private final MediaClient mediaClient;

    @Override
    public PageVO<List<PhotoAndAlbumListVO>> getBackPhotoList(Long pageNum, Long pageSize, Long parentId) {
        Page<Photo> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Photo> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (null != parentId) {
            lambdaQueryWrapper.eq(Photo::getParentId, parentId);
        } else {
            lambdaQueryWrapper.isNull(Photo::getParentId);
        }
        lambdaQueryWrapper.last(ORDER_BY_CREATE_TIME_DESC);
        photoMapper.selectPage(page, lambdaQueryWrapper);
        if (page.getRecords().isEmpty()) return new PageVO<>(List.of(), page.getTotal());

        for (Photo photo : page.getRecords()) {
            if (Objects.equals(photo.getType(), AlbumOrPhotoEnum.ALBUM.getCode())) {
                Photo photoOne = photoMapper.selectOne(
                        new LambdaQueryWrapper<Photo>()
                                .eq(Photo::getParentId, photo.getId())
                                .eq(Photo::getType, AlbumOrPhotoEnum.PHOTO.getCode())
                                .last(ORDER_BY_CREATE_TIME_DESC).last(LIMIT_ONE_SQL)
                );
                if (null != photoOne) {
                    photo.setUrl(photoOne.getUrl());
                    page.getRecords().get(page.getRecords().indexOf(photo)).setUrl(photoOne.getUrl());
                }
            }
        }

        List<PhotoAndAlbumListVO> photoAndAlbumListVOS = page.getRecords().stream().map(photo -> photo.copyProperties(PhotoAndAlbumListVO.class)).toList();
        return new PageVO<>(photoAndAlbumListVOS, page.getTotal());
    }

    @Override
    public ResultData<Void> createAlbum(PhotoAlbumDTO albumDTO) {
        if (photoMapper.selectCount(
                new LambdaQueryWrapper<Photo>()
                        .eq(Photo::getName, albumDTO.getName())
                        .eq(Photo::getType, AlbumOrPhotoEnum.ALBUM.getCode())
                        .eq(Photo::getParentId, albumDTO.getParentId())
        ) > 0) {
            return ResultData.failure("相册名称存在重复");
        }
        if (photoMapper.insert(new Photo()
                .setUserId(SecurityUtils.getUserId())
                .setParentId(albumDTO.getParentId())
                .setName(albumDTO.getName())
                .setDescription(albumDTO.getDescription())
                .setType(AlbumOrPhotoEnum.ALBUM.getCode())
        ) > 0) {
            return ResultData.success();
        }
        return ResultData.failure();
    }

    @Override
    public ResultData<Void> uploadPhoto(MultipartFile file, String name, Long parentId) {
        try {
            if (photoMapper.selectCount(
                    new LambdaQueryWrapper<Photo>()
                            .eq(Photo::getName, name)
                            .eq(Photo::getType, AlbumOrPhotoEnum.PHOTO.getCode())
                            .eq(Photo::getParentId, parentId)
            ) > 0) {
                return ResultData.failure("照片名称存在重复");
            }

            ResultData<Map<String, Object>> result = mediaClient.uploadFileWithRuleName(
                    SecurityUtils.getUserId(),
                    file,
                    MediaUploadRuleEnum.PHOTO_ALBUM.name()
            );

            if (result.getCode().equals(ReturnCodeEnum.SUCCESS.getCode()) && result.getData() != null) {
                String bannerUrl = (String) result.getData().get("fileUrl");
                if (MyStringUtils.isNotNull(bannerUrl)) {
                    photoMapper.insert(new Photo()
                            .setUserId(SecurityUtils.getUserId())
                            .setParentId(parentId)
                            .setName(name)
                            .setUrl(bannerUrl)
                            .setType(AlbumOrPhotoEnum.PHOTO.getCode())
                            .setSize(file.getSize() / 1024.0));
                    return ResultData.success();
                }
            }
            return ResultData.failure("上传失败");
        } catch (Exception e) {
            log.error("{}上传失败", MediaUploadRuleEnum.PHOTO_ALBUM.getDescription(), e);
            return ResultData.failure(e.getMessage());
        }
    }

    @Override
    public ResultData<Void> updateAlbum(PhotoAlbumDTO albumDTO) {
        if (photoMapper.updateById(new Photo()
                .setId(albumDTO.getId())
                .setName(albumDTO.getName())
                .setDescription(albumDTO.getDescription())
        ) > 0) {
            return ResultData.success();
        }
        return ResultData.failure();
    }

    @Override
    public ResultData<Void> deletePhotoOrAlbum(DeletePhotoOrAlbumDTO deletePhotoOrAlbum) {
        if (Objects.equals(deletePhotoOrAlbum.getType(), AlbumOrPhotoEnum.ALBUM.getCode())) {
            if (photoMapper.selectCount(new LambdaQueryWrapper<Photo>().eq(Photo::getParentId, deletePhotoOrAlbum.getId())) > 0) {
                return ResultData.failure("删除失败，该相册下存在子相册或照片");
            }
            if (photoMapper.deleteById(deletePhotoOrAlbum.getId()) > 0) {
                return ResultData.success();
            }
            return ResultData.failure();
        } else {
            Photo photo = photoMapper.selectById(deletePhotoOrAlbum.getId());
            String objectName = extractObjectName(photo.getUrl());
            ResultData<Void> result = mediaClient.deleteFile(SecurityUtils.getUserId(), objectName);
            if (result.getCode().equals(ReturnCodeEnum.SUCCESS.getCode()) && photoMapper.deleteById(deletePhotoOrAlbum.getId()) > 0) {
                return ResultData.success();
            }
            return ResultData.failure(result.getMsg());
        }
    }

    private String extractObjectName(String fileUrl) {
        if (MyStringUtils.isNull(fileUrl)) {
            return "";
        }
        try {
            java.net.URI uri = java.net.URI.create(fileUrl);
            String path = uri.getPath();
            if (path != null && path.startsWith("/")) {
                int secondSlash = path.indexOf('/', 1);
                if (secondSlash != -1) {
                    return path.substring(secondSlash + 1);
                }
            }
        } catch (Exception ignored) {
        }
        return fileUrl;
    }
}