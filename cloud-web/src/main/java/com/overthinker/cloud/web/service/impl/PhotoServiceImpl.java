package com.overthinker.cloud.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.overthinker.cloud.common.core.resp.ResultData;
import com.overthinker.cloud.web.entity.DTO.DeletePhotoOrAlbumDTO;
import com.overthinker.cloud.web.entity.DTO.PhotoAlbumDTO;
import com.overthinker.cloud.web.entity.PO.Photo;
import com.overthinker.cloud.web.entity.VO.PageVO;
import com.overthinker.cloud.web.entity.VO.PhotoAndAlbumListVO;
import com.overthinker.cloud.web.entity.enums.AlbumOrPhotoEnum;
import com.overthinker.cloud.web.entity.enums.UploadEnum;
import com.overthinker.cloud.web.exception.FileUploadException;
import com.overthinker.cloud.web.mapper.PhotoMapper;
import com.overthinker.cloud.web.service.PhotoService;
import com.overthinker.cloud.web.utils.PhotoUploudUtils;
import com.overthinker.cloud.web.utils.SecurityUtils;
import com.overthinker.cloud.web.utils.StringUtils;
import jakarta.annotation.Resource;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

import static com.overthinker.cloud.web.entity.constants.SQLConst.LIMIT_ONE_SQL;
import static com.overthinker.cloud.web.entity.constants.SQLConst.ORDER_BY_CREATE_TIME_DESC;

@Log4j2
@Service("photosService")
public class PhotoServiceImpl extends ServiceImpl<PhotoMapper, Photo> implements PhotoService {

    @Resource
    private PhotoMapper photoMapper;

    @Resource
    private PhotoUploudUtils photoUploudUtils;

    @Override
    public PageVO<List<PhotoAndAlbumListVO>> getBackPhotoList(Long pageNum, Long pageSize, Long parentId) {
        // 分页
        Page<Photo> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Photo> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (null != parentId) {
            lambdaQueryWrapper.eq(Photo::getParentId, parentId);
        } else {
            lambdaQueryWrapper.isNull(Photo::getParentId);
        }
        // 优先显示相册，再显示照片，时间倒序
        lambdaQueryWrapper.last(ORDER_BY_CREATE_TIME_DESC);
        photoMapper.selectPage(page, lambdaQueryWrapper);
        if (page.getRecords().isEmpty()) return new PageVO<>(List.of(), page.getTotal());
        // 查询每个相册的封面
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
        // 是否存在相同名称的相册
        if (
                photoMapper.selectCount(
                        new LambdaQueryWrapper<Photo>()
                                .eq(Photo::getName, albumDTO.getName())
                                .eq(Photo::getType, AlbumOrPhotoEnum.ALBUM.getCode())
                                .eq(Photo::getParentId, albumDTO.getParentId())
                ) > 0) {
            return ResultData.failure("相册名称存在重复");
        }
        if (
                photoMapper.insert(Photo.builder()
                        .userId(SecurityUtils.getUserId())
                        .parentId(albumDTO.getParentId())
                        .name(albumDTO.getName())
                        .description(albumDTO.getDescription())
                        .type(AlbumOrPhotoEnum.ALBUM.getCode())
                        .build()
                ) > 0) {
            return ResultData.success();
        }
        return ResultData.failure();
    }

    @Transactional
    @Override
    public ResultData<Void> uploadPhoto(MultipartFile file, String name, Long parentId) {
        try {
            // TODO 注意：如minio地址配置的是nginx代理域名，则需要配置nginx的文件上传大小
            // 当前相册是否存在相同名称照片
            if (
                    photoMapper.selectCount(
                            new LambdaQueryWrapper<Photo>()
                                    .eq(Photo::getName, name)
                                    .eq(Photo::getType, AlbumOrPhotoEnum.PHOTO.getCode())
                                    .eq(Photo::getParentId, parentId)
                    ) > 0) {
                return ResultData.failure("照片名称存在重复");
            }

            String bannerUrl;
            // 查询父相册的名称
            if (StringUtils.isNotNull(parentId)) {
                // 递归查询父相册并组合路径
                String albumPath = buildAlbumPath(photoMapper, parentId);
                bannerUrl = photoUploudUtils.upload(UploadEnum.PHOTO_ALBUM, file, name, albumPath);
            } else {
                bannerUrl = photoUploudUtils.upload(UploadEnum.PHOTO_ALBUM, file, name);
            }

            //添加数据库
            photoMapper.insert(Photo.builder()
                    .userId(SecurityUtils.getUserId())
                    .parentId(parentId)
                    .name(name)
                    .url(bannerUrl)
                    .type(AlbumOrPhotoEnum.PHOTO.getCode())
                    .size(photoUploudUtils.convertFileSizeToMB(file.getSize()))
                    .build());

            return ResultData.success();
        } catch (FileUploadException e) {
            log.error("{}上传失败", UploadEnum.PHOTO_ALBUM.getDescription(), e);
            return ResultData.failure(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 递归查询父相册并组合文件路径
     *
     * @param photoMapper 相册 Mapper
     * @param parentId    当前相册的父相册 ID
     * @return 组合后的文件路径（如 "顶层相册名称/父相册名称/当前相册名称"）
     */
    private String buildAlbumPath(PhotoMapper photoMapper, Long parentId) {
        if (parentId == null) {
            return ""; // 顶层相册，返回空路径
        }

        // 查询当前父相册
        Photo parentAlbum = photoMapper.selectById(parentId);
        if (parentAlbum == null) {
            throw new RuntimeException("父相册不存在，ID: " + parentId);
        }

        // 递归查询父相册的父相册
        String parentPath = buildAlbumPath(photoMapper, parentAlbum.getParentId());

        // 组合路径
        return parentPath.isEmpty()
                ? parentAlbum.getName()
                : parentPath + "/" + parentAlbum.getName();
    }

    @Override
    public ResultData<Void> updateAlbum(PhotoAlbumDTO albumDTO) {
        if (
                photoMapper.updateById(Photo.builder()
                        .id(albumDTO.getId())
                        .name(albumDTO.getName())
                        .description(albumDTO.getDescription())
                        .build()
                ) > 0) {
            return ResultData.success();
        }
        return ResultData.failure();
    }

    @Transactional
    @Override
    public ResultData<Void> deletePhotoOrAlbum(DeletePhotoOrAlbumDTO deletePhotoOrAlbum) {
        if (Objects.equals(deletePhotoOrAlbum.getType(), AlbumOrPhotoEnum.ALBUM.getCode())) {
            // 是否存在子相册
            if (photoMapper.selectCount(new LambdaQueryWrapper<Photo>().eq(Photo::getParentId, deletePhotoOrAlbum.getId())) > 0) {
                return ResultData.failure("删除失败，该相册下存在子相册或照片");
            }
            // 删除相册
            if (photoMapper.deleteById(deletePhotoOrAlbum.getId()) > 0) {
                return ResultData.success();
            }
            return ResultData.failure();
        } else {
            // 查询照片名称
            Photo photo = photoMapper.selectById(deletePhotoOrAlbum.getId());
            // 查询父相册
            if (StringUtils.isNotNull(photo.getParentId())) {
                Photo album = photoMapper.selectById(deletePhotoOrAlbum.getParentId());
                photoUploudUtils.deleteFile(UploadEnum.PHOTO_ALBUM.getDir() + album.getName() + "/", photoUploudUtils.getFileName(photo.getUrl()));
            } else {
                photoUploudUtils.deleteFile(UploadEnum.PHOTO_ALBUM.getDir(), photoUploudUtils.getFileName(photo.getUrl()));
            }
            if (photoMapper.deleteById(deletePhotoOrAlbum.getId()) > 0) {
                return ResultData.success();
            }
            return ResultData.failure();
        }
    }
}
