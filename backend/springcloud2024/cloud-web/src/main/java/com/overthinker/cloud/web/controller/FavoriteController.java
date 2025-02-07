package com.overthinker.cloud.web.controller;

import com.overthinker.cloud.resp.ResultData;
import com.overthinker.cloud.web.annotation.AccessLimit;
import com.overthinker.cloud.web.annotation.CheckBlacklist;
import com.overthinker.cloud.web.annotation.LogAnnotation;
import com.overthinker.cloud.web.controller.base.BaseController;
import com.overthinker.cloud.web.entity.DTO.FavoriteIsCheckDTO;
import com.overthinker.cloud.web.entity.DTO.SearchFavoriteDTO;
import com.overthinker.cloud.web.entity.VO.FavoriteListVO;
import com.overthinker.cloud.web.entity.constants.LogConst;
import com.overthinker.cloud.web.service.FavoriteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author overH
 * <p>
 * 创建时间：2023/11/3 10:16
 */
@RestController
@Tag(name = "收藏相关接口")
@RequestMapping("/favorite")
@Validated
public class FavoriteController extends BaseController {

    @Resource
    private FavoriteService favoriteService;

    @CheckBlacklist
    @Operation(summary = "收藏")
    @Parameters({
            @Parameter(name = "type", description = "收藏类型", required = true),
            @Parameter(name = "typeId", description = "收藏id", required = true)
    })
    @AccessLimit(seconds = 60, maxCount = 10)
    @PostMapping("/auth/favorite")
    public ResultData<Void> favorite(
            @Valid @NotNull @RequestParam("type") Integer type,
            @RequestParam(value = "typeId", required = false) Long typeId
    ) {
        return favoriteService.userFavorite(type, typeId);
    }

    @CheckBlacklist
    @Operation(summary = "取消收藏")
    @Parameters({
            @Parameter(name = "type", description = "收藏类型", required = true),
            @Parameter(name = "typeId", description = "收藏id", required = true)
    })
    @AccessLimit(seconds = 60, maxCount = 10)
    @DeleteMapping("/auth/favorite")
    public ResultData<Void> cancelFavorite(
            @Valid @NotNull @RequestParam("type") Integer type,
            @RequestParam(value = "typeId", required = false) Integer typeId
    ) {
        return favoriteService.cancelFavorite(type, typeId);
    }

    @Operation(summary = "是否已经收藏")
    @Parameters({
            @Parameter(name = "type", description = "收藏类型", required = true),
            @Parameter(name = "typeId", description = "收藏id", required = true)
    })
    @AccessLimit(seconds = 60, maxCount = 60)
    @GetMapping("/whether/favorite")
    public ResultData<Boolean> isFavorite(
            @Valid @NotNull @RequestParam("type") Integer type,
            @RequestParam(value = "typeId", required = false) Integer typeId
    ) {
        return messageHandler((() -> favoriteService.isFavorite(type, typeId)));
    }

    @PreAuthorize("hasAnyAuthority('blog:favorite:list')")
    @Operation(summary = "后台收藏列表")
    @AccessLimit(seconds = 60, maxCount = 30)
    @LogAnnotation(module="收藏管理",operation= LogConst.GET)
    @GetMapping("/back/list")
    public ResultData<List<FavoriteListVO>> backList() {
        return messageHandler(() -> favoriteService.getBackFavoriteList(null));
    }

    @PreAuthorize("hasAnyAuthority('blog:favorite:search')")
    @Operation(summary = "搜索后台收藏列表")
    @AccessLimit(seconds = 60, maxCount = 30)
    @LogAnnotation(module="收藏管理",operation= LogConst.SEARCH)
    @PostMapping("/back/search")
    public ResultData<List<FavoriteListVO>> backList(@RequestBody SearchFavoriteDTO searchDTO) {
        return messageHandler(() -> favoriteService.getBackFavoriteList(searchDTO));
    }

    @PreAuthorize("hasAnyAuthority('blog:favorite:isCheck')")
    @Operation(summary = "修改收藏是否通过")
    @AccessLimit(seconds = 60, maxCount = 30)
    @LogAnnotation(module="收藏管理",operation= LogConst.UPDATE)
    @PostMapping("/back/isCheck")
    public ResultData<Void> isCheck(@RequestBody @Valid FavoriteIsCheckDTO favoriteIsCheckDTO) {
        return favoriteService.isCheckFavorite(favoriteIsCheckDTO);
    }

    @PreAuthorize("hasAnyAuthority('blog:favorite:delete')")
    @Operation(summary = "删除收藏")
    @AccessLimit(seconds = 60, maxCount = 30)
    @LogAnnotation(module="收藏管理",operation= LogConst.DELETE)
    @DeleteMapping("/back/delete")
    public ResultData<Void> delete(@RequestBody List<Long> ids) {
        return favoriteService.deleteFavorite(ids);
    }

}
