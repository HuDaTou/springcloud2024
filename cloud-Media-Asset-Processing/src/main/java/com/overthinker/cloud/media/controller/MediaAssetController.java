package com.overthinker.cloud.media.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.overthinker.cloud.common.core.annotation.LogAnnotation;
import com.overthinker.cloud.common.core.annotation.LogConst;
import com.overthinker.cloud.common.core.resp.PageParams;
import com.overthinker.cloud.common.core.resp.ResultData;
import com.overthinker.cloud.media.entity.PO.MediaAsset;
import com.overthinker.cloud.media.entity.VO.MediaAssetVO;
import com.overthinker.cloud.media.service.MediaAssetService;
import com.overthinker.cloud.media.service.UploadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 媒体资产管理控制器
 * <p>
 * 提供媒体资产的管理接口（增删改查），需要权限控制
 * </p>
 *
 * @author overthinker
 * @since 2025-08-02
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/media/assets")
@RequiredArgsConstructor
@Tag(name = "媒体资产管理", description = "提供媒体资产的管理接口，需要权限控制")
public class MediaAssetController {

    private final MediaAssetService mediaAssetService;
    private final UploadService uploadService;

    @GetMapping
    @Operation(summary = "分页查询媒体资产列表", description = "根据分页参数查询媒体资产列表")
    @PreAuthorize("hasAuthority('media:asset:list')")
    public ResultData<Page<MediaAssetVO>> list(
            @Parameter(description = "分页参数") @ModelAttribute PageParams pageParams) {
        return ResultData.success(uploadService.listFiles(
                pageParams.getPageNo().intValue(),
                pageParams.getPageSize().intValue()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取媒体资产详情", description = "根据ID获取媒体资产的详细信息，包含下载URL和缩略图URL")
    @PreAuthorize("hasAuthority('media:asset:query')")
    public ResultData<MediaAssetVO> get(
            @Parameter(description = "媒体资产ID", required = true) @PathVariable @NotNull(message = "ID不能为空") Long id) {
        return ResultData.success(uploadService.getAssetById(id));
    }

    @PostMapping
    @Operation(summary = "创建媒体资产记录", description = "新增一条媒体资产记录")
    @PreAuthorize("hasAuthority('media:asset:add')")
    @LogAnnotation(module = "媒体资产管理", operation = LogConst.INSERT)
    public ResultData<Void> create(
            @Parameter(description = "媒体资产信息", required = true) @RequestBody @Valid MediaAsset asset) {
        mediaAssetService.save(asset);
        log.info("创建媒体资产成功，ID：{}，文件名：{}", asset.getId(), asset.getFileName());
        return ResultData.success();
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新媒体资产", description = "根据ID更新媒体资产的全部信息")
    @PreAuthorize("hasAuthority('media:asset:edit')")
    @LogAnnotation(module = "媒体资产管理", operation = LogConst.UPDATE)
    public ResultData<Void> update(
            @Parameter(description = "媒体资产ID", required = true) @PathVariable @NotNull(message = "ID不能为空") Long id,
            @Parameter(description = "媒体资产信息", required = true) @RequestBody @Valid MediaAsset asset) {
        asset.setId(id);
        mediaAssetService.updateById(asset);
        log.info("更新媒体资产成功，ID：{}", id);
        return ResultData.success();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除媒体资产", description = "根据ID删除媒体资产（仅删除数据库记录，不删除MinIO文件）")
    @PreAuthorize("hasAuthority('media:asset:delete')")
    @LogAnnotation(module = "媒体资产管理", operation = LogConst.DELETE)
    public ResultData<Void> delete(
            @Parameter(description = "媒体资产ID", required = true) @PathVariable @NotNull(message = "ID不能为空") Long id) {
        mediaAssetService.removeById(id);
        log.info("删除媒体资产成功，ID：{}", id);
        return ResultData.success();
    }

    @PostMapping("/batch/delete")
    @Operation(summary = "批量删除媒体资产", description = "根据ID列表批量删除媒体资产")
    @PreAuthorize("hasAuthority('media:asset:delete')")
    @LogAnnotation(module = "媒体资产管理", operation = LogConst.DELETE)
    public ResultData<Void> batchDelete(
            @Parameter(description = "媒体资产ID列表", required = true) @RequestBody @NotEmpty(message = "ID列表不能为空") List<Long> ids) {
        mediaAssetService.removeByIds(ids);
        log.info("批量删除媒体资产成功，数量：{}", ids.size());
        return ResultData.success();
    }
}
