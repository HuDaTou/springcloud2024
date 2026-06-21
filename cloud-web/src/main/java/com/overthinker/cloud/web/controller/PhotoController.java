package com.overthinker.cloud.web.controller;

import com.overthinker.cloud.common.core.annotation.LogAnnotation;
import com.overthinker.cloud.common.core.annotation.LogConst;
import com.overthinker.cloud.common.core.base.BaseController;
import com.overthinker.cloud.common.core.resp.ResultData;
import com.overthinker.cloud.system.starter.redis.annotation.AccessLimit;
import com.overthinker.cloud.web.entity.DTO.DeletePhotoOrAlbumDTO;
import com.overthinker.cloud.web.entity.DTO.PhotoAlbumDTO;
import com.overthinker.cloud.web.entity.VO.PageVO;
import com.overthinker.cloud.web.entity.VO.PhotoAndAlbumListVO;
import com.overthinker.cloud.web.service.PhotoService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 照片控制器
 * <p>
 * 处理照片和相册的管理接口，包括上传、删除、列表查询等操作
 * </p>
 *
 * @author overH
 * @since 2024-08-28
 */
@Validated
@RestController
@RequestMapping("photo")
@RequiredArgsConstructor
public class PhotoController extends BaseController {

    private final PhotoService photoService;

    /**
     * 后台相册或照片列表
     *
     * @param pageNum  页码，默认1
     * @param pageSize 每页大小，默认10
     * @param parentId 父相册ID，不传则查询顶层相册
     * @return 分页相册/照片列表
     */
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

    /**
     * 前台相册或照片列表
     *
     * @param pageNum  页码，默认1
     * @param pageSize 每页大小，默认16
     * @param parentId 父相册ID，不传则查询顶层相册
     * @return 分页相册/照片列表
     */
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

    /**
     * 后台创建相册
     *
     * @param albumDTO 相册信息
     * @return 操作结果
     */
    @PreAuthorize("hasAnyAuthority('blog:album:create')")
    @Operation(summary = "后台创建相册")
    @AccessLimit(seconds = 60, maxCount = 30)
    @LogAnnotation(module = "相册管理", operation = LogConst.INSERT)
    @PostMapping("/album/create")
    public ResultData<Void> createAlbum(@RequestBody @Validated PhotoAlbumDTO albumDTO) {
        return photoService.createAlbum(albumDTO);
    }

    /**
     * 后台上传照片
     *
     * @param file     照片文件
     * @param name     照片名称（1-20个字符）
     * @param parentId 父相册ID，不传则上传到顶层
     * @return 操作结果
     */
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

    /**
     * 后台修改相册
     *
     * @param albumDTO 相册信息
     * @return 操作结果
     */
    @PreAuthorize("hasAnyAuthority('blog:album:update')")
    @Operation(summary = "后台修改相册")
    @AccessLimit(seconds = 60, maxCount = 30)
    @LogAnnotation(module = "相册管理", operation = LogConst.UPDATE)
    @PostMapping("/album/update")
    public ResultData<Void> updateAlbum(@RequestBody @Validated PhotoAlbumDTO albumDTO) {
        return photoService.updateAlbum(albumDTO);
    }

    /**
     * 后台删除相册或照片
     *
     * @param deletePhotoOrAlbum 删除参数（类型和ID）
     * @return 操作结果
     */
    @PreAuthorize("hasAnyAuthority('blog:photo:delete')")
    @Operation(summary = "后台删除相册或照片")
    @AccessLimit(seconds = 60, maxCount = 30)
    @LogAnnotation(module = "相册管理", operation = LogConst.DELETE)
    @DeleteMapping("/delete")
    public ResultData<Void> deletePhotoOrAlbum(@RequestBody @Validated DeletePhotoOrAlbumDTO deletePhotoOrAlbum) {
        return photoService.deletePhotoOrAlbum(deletePhotoOrAlbum);
    }

}