package com.overthinker.cloud.web.controller;

import com.overthinker.cloud.common.core.annotation.LogAnnotation;
import com.overthinker.cloud.common.core.annotation.LogConst;
import com.overthinker.cloud.common.core.base.BaseController;
import com.overthinker.cloud.common.core.resp.ResultData;
import com.overthinker.cloud.system.starter.redis.annotation.AccessLimit;
import com.overthinker.cloud.web.entity.PO.Banners;
import com.overthinker.cloud.web.service.BannersService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Banner 控制器
 * <p>
 * 处理 Banner 图片的管理接口，包括上传、删除、列表查询和排序更新等操作
 * </p>
 *
 * @author overH
 * @since 2024-08-28
 */
@RestController
@RequestMapping("banners")
@RequiredArgsConstructor
public class BannersController extends BaseController {

    private final BannersService bannersService;

    /**
     * 前台查询首页 Banner 列表
     *
     * @return Banner 图片URL列表
     */
    @Operation(summary = "前台获取所有前台首页Banner图片")
    @LogAnnotation(module = "信息管理", operation = LogConst.GET)
    @AccessLimit(seconds = 60, maxCount = 25)
    @GetMapping("/list")
    public ResultData<List<String>> getBanners() {
        return messageHandler(() -> bannersService.getBanners());
    }

    /**
     * 后台查询首页 Banner 列表
     *
     * @return Banner 列表
     */
    @PreAuthorize("hasAnyAuthority('blog:banner:list')")
    @Operation(summary = "后台获取所有前台首页Banner图片")
    @LogAnnotation(module = "信息管理", operation = LogConst.GET)
    @AccessLimit(seconds = 60, maxCount = 60)
    @GetMapping("/back/list")
    public ResultData<List<Banners>> backGetBanners() {
        return messageHandler(() -> bannersService.backGetBanners());
    }

    /**
     * 删除首页 Banner
     *
     * @param id Banner ID
     * @return 操作结果
     */
    @PreAuthorize("hasAnyAuthority('blog:banner:delete')")
    @Operation(summary = "删除前台首页Banner图片")
    @LogAnnotation(module = "信息管理", operation = LogConst.DELETE)
    @Parameter(name = "id", description = "Banner ID", required = true)
    @AccessLimit(seconds = 60, maxCount = 30)
    @DeleteMapping("/{id}")
    public ResultData<String> delete(@PathVariable("id") Long id) {
        return bannersService.removeBannerById(id);
    }

    /**
     * 添加 Banner 图片
     *
     * @param bannerImage Banner 图片文件
     * @return Banner 信息
     */
    @PreAuthorize("hasAnyAuthority('blog:banner:add')")
    @Operation(summary = "添加前台首页Banner图片")
    @LogAnnotation(module = "信息管理", operation = LogConst.INSERT)
    @Parameter(name = "bannerImage", description = "Banner图片", required = true)
    @AccessLimit(seconds = 60, maxCount = 30)
    @PostMapping("/upload/banner")
    public ResultData<Banners> uploadArticleImage(
            @RequestParam("bannerImage") MultipartFile bannerImage
    ) {
        return bannersService.uploadBannerImage(bannerImage);
    }

    /**
     * 更新 Banner 顺序
     *
     * @param banners Banner 列表（包含排序信息）
     * @return 操作结果
     */
    @PreAuthorize("hasAnyAuthority('blog:banner:update')")
    @Operation(summary = "更新前台首页Banner图片顺序")
    @LogAnnotation(module = "信息管理", operation = LogConst.UPDATE)
    @Parameter(name = "SortOrders", description = "顺序", required = true)
    @AccessLimit(seconds = 60, maxCount = 30)
    @PutMapping("/update/sort/order")
    public ResultData<String> updateSortOrder(@RequestBody List<Banners> banners) {
        return bannersService.updateSortOrder(banners);
    }
}