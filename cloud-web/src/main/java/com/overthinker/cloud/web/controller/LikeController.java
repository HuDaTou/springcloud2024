package com.overthinker.cloud.web.controller;

import com.overthinker.cloud.common.core.annotation.CheckBlacklist;
import com.overthinker.cloud.common.core.base.BaseController;
import com.overthinker.cloud.common.core.resp.ResultData;
import com.overthinker.cloud.system.starter.redis.annotation.AccessLimit;
import com.overthinker.cloud.web.entity.PO.Like;
import com.overthinker.cloud.web.service.LikeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 点赞控制器
 * <p>
 * 处理点赞的管理接口，包括点赞、取消点赞和查询状态等操作
 * </p>
 *
 * @author overH
 * @since 2023-11-03
 */
@RestController
@Tag(name = "点赞相关接口")
@RequestMapping("/like")
@Validated
@RequiredArgsConstructor
public class LikeController extends BaseController {

    private final LikeService likeService;

    /**
     * 添加点赞
     *
     * @param type   点赞类型
     * @param typeId 点赞关联ID
     * @return 操作结果
     */
    @CheckBlacklist
    @Operation(summary = "点赞")
    @Parameters({
            @Parameter(name = "type", description = "点赞类型", required = true),
            @Parameter(name = "typeId", description = "点赞id", required = true)
    })
    @AccessLimit(seconds = 60, maxCount = 10)
    @PostMapping("/auth/like")
    public ResultData<Void> like(
            @RequestParam("type") @Valid @NotNull Integer type,
            @RequestParam("typeId") @Valid @NotNull Integer typeId
    ) {
        return likeService.userLike(type, typeId);
    }

    /**
     * 取消点赞
     *
     * @param type   点赞类型
     * @param typeId 点赞关联ID
     * @return 操作结果
     */
    @CheckBlacklist
    @Operation(summary = "取消点赞")
    @Parameters({
            @Parameter(name = "type", description = "点赞类型", required = true),
            @Parameter(name = "typeId", description = "点赞id", required = true)
    })
    @AccessLimit(seconds = 60, maxCount = 10)
    @DeleteMapping("/auth/like")
    public ResultData<Void> cancelLike(
            @RequestParam("type") @Valid @NotNull Integer type,
            @RequestParam("typeId") @Valid @NotNull Integer typeId
    ) {
        return likeService.cancelLike(type, typeId);
    }

    /**
     * 判断是否已点赞
     *
     * @param type   点赞类型
     * @param typeId 点赞关联ID
     * @return 点赞记录列表
     */
    @Operation(summary = "是否已经点赞")
    @Parameters({
            @Parameter(name = "type", description = "点赞类型", required = true),
            @Parameter(name = "typeId", description = "点赞id", required = true)
    })
    @AccessLimit(seconds = 60, maxCount = 60)
    @GetMapping("/whether/like")
    public ResultData<List<Like>> isLike(
            @Valid @NotNull @RequestParam("type") Integer type,
            @RequestParam(value = "typeId", required = false) Integer typeId
    ) {
        return likeService.isLike(type, typeId);
    }
}