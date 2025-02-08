package com.overthinker.cloud.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.overthinker.cloud.resp.ResultData;
import com.overthinker.cloud.web.entity.DTO.DeletePhotoOrAlbumDTO;
import com.overthinker.cloud.web.entity.DTO.PhotoAlbumDTO;
import com.overthinker.cloud.web.entity.PO.Photo;
import com.overthinker.cloud.web.entity.VO.PageVO;
import com.overthinker.cloud.web.entity.VO.PhotoAndAlbumListVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PhotoService extends IService<Photo> {

    /**
     * 获取后台图片列表
     * @param pageNum 当前页码
     * @param pageSize 每页数量
     * @param parentId 父相册id
     * @return 图片列表
     */
    PageVO<List<PhotoAndAlbumListVO>> getBackPhotoList(Long pageNum, Long pageSize, Long parentId);

    /**
     * 创建相册
     * @param albumDTO 相册信息
     * @return 创建结果
     */
    ResultData<Void> createAlbum(PhotoAlbumDTO albumDTO);

    /**
     * 上传图片
     * @param file 图片文件
     * @param name 图片名称
     * @param parentId 相册id
     * @return 上传结果
     */
    ResultData<Void> uploadPhoto(MultipartFile file, String name, Long parentId);

    /**
     * 修改相册
     * @param albumDTO 相册信息
     * @return 修改结果
     */
    ResultData<Void> updateAlbum(PhotoAlbumDTO albumDTO);

    /**
     * 删除相册或照片
     * @param deletePhotoOrAlbum 相册或照片信息
     * @return 删除结果
     */
    ResultData<Void> deletePhotoOrAlbum(DeletePhotoOrAlbumDTO deletePhotoOrAlbum);
}