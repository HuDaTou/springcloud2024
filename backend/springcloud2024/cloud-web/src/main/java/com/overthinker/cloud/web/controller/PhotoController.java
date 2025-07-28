package com.overthinker.cloud.web.controller;

import com.overthinker.cloud.annotation.LogAnnotation;
import com.overthinker.cloud.controller.base.BaseController;
import com.overthinker.cloud.resp.ResultData;
import com.overthinker.cloud.annotation.AccessLimit;

import com.overthinker.cloud.web.entity.DTO.DeletePhotoOrAlbumDTO;
import com.overthinker.cloud.web.entity.DTO.PhotoAlbumDTO;
import com.overthinker.cloud.web.entity.VO.PageVO;
import com.overthinker.cloud.web.entity.VO.PhotoAndAlbumListVO;
import com.overthinker.cloud.annotation.LogConst;
import com.overthinker.cloud.web.service.PhotoService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Validated
@RestController
@RequestMapping("photo")
public class PhotoController extends BaseController {
    /**
     * 服务对象
     */
    @Resource
    private PhotoService photoService;

    @PreAuthorize("hasAnyAuthority('blog:photo:list')")
    @Operation(summary = "后台相册或照片列表")
    @AccessLimit(seconds = 60, maxCount = 30)
    @LogAnnotation(module = "相册管理", operation = LogConst.GET)
    @GetMapping("/back/list")
    public ResultData<PageVO<List<PhotoAndAlbumListVO>>> backList(
            @RequestParam(value = "pageNum", defaultValue = "1") Long pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Long pageSize,
            @RequestParam(value = "parentId", required = false) Long parentId
    ) {
        return messageHandler(() -> photoService.getBackPhotoList(pageNum, pageSize, parentId));
    }

    @Operation(summary = "前台相册或照片列表")
    @AccessLimit(seconds = 60, maxCount = 30)
    @LogAnnotation(module = "相册管理", operation = LogConst.GET)
    @GetMapping("/list")
    public ResultData<PageVO<List<PhotoAndAlbumListVO>>> getList(
            @RequestParam(value = "pageNum", defaultValue = "1") Long pageNum,
            @RequestParam(value = "pageSize", defaultValue = "16") Long pageSize,
            @RequestParam(value = "parentId", required = false) Long parentId
    ) {
        return messageHandler(() -> photoService.getBackPhotoList(pageNum, pageSize, parentId));
    }

    @PreAuthorize("hasAnyAuthority('blog:album:create')")
    @Operation(summary = "后台创建相册")
    @AccessLimit(seconds = 60, maxCount = 30)
    @LogAnnotation(module = "相册管理", operation = LogConst.INSERT)
    @PostMapping("/album/create")
    public ResultData<Void> createAlbum(@RequestBody @Validated PhotoAlbumDTO albumDTO) {
        return photoService.createAlbum(albumDTO);
    }

    @PreAuthorize("hasAnyAuthority('blog:photo:upload')")
    @Operation(summary = "后台上传照片")
    @AccessLimit(seconds = 60, maxCount = 30)
    @LogAnnotation(module = "相册管理", operation = LogConst.UPLOAD_IMAGE)
    @PostMapping("/upload")
    public ResultData<Void> uploadPhoto(
            @RequestParam("file") MultipartFile file,
            @RequestParam("name") @Length(min = 1, max = 20, message = "照片名称长度为1-20个字符") String name,
            @RequestParam(value = "parentId", required = false) Long parentId) {
        return photoService.uploadPhoto(file, name, parentId);
    }

    @PreAuthorize("hasAnyAuthority('blog:album:update')")
    @Operation(summary = "后台修改相册")
    @AccessLimit(seconds = 60, maxCount = 30)
    @LogAnnotation(module = "相册管理", operation = LogConst.UPDATE)
    @PostMapping("/album/update")
    public ResultData<Void> updateAlbum(@RequestBody @Validated PhotoAlbumDTO albumDTO) {
        return photoService.updateAlbum(albumDTO);
    }

    @PreAuthorize("hasAnyAuthority('blog:photo:delete')")
    @Operation(summary = "后台删除相册或照片")
    @AccessLimit(seconds = 60, maxCount = 30)
    @LogAnnotation(module = "相册管理", operation = LogConst.UPDATE)
    @DeleteMapping("/delete")
    public ResultData<Void> deletePhotoOrAlbum(@RequestBody @Validated DeletePhotoOrAlbumDTO deletePhotoOrAlbum) {
        return photoService.deletePhotoOrAlbum(deletePhotoOrAlbum);
    }

}