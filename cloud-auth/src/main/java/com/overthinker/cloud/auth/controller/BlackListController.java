package com.overthinker.cloud.auth.controller;

import com.overthinker.cloud.common.annotation.AccessLimit;
import com.overthinker.cloud.common.annotation.LogAnnotation;
import com.overthinker.cloud.common.annotation.LogConst;
import com.overthinker.cloud.auth.entity.DTO.AddBlackListDTO;
import com.overthinker.cloud.auth.entity.DTO.SearchBlackListDTO;
import com.overthinker.cloud.auth.entity.DTO.UpdateBlackListDTO;
import com.overthinker.cloud.auth.entity.VO.BlackListVO;
import com.overthinker.cloud.auth.service.BlackListService;
import com.overthinker.cloud.common.core.base.BaseController;
import com.overthinker.cloud.common.resp.ResultData;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import com.overthinker.cloud.auth.exception.BlackListException;

/**
 * (BlackList)表控制层
 *
 * @author overH
 * @since 2024-09-05 16:13:19
 */
@RestController
@RequestMapping("blackList")
public class BlackListController extends BaseController {
    /**
     * 服务对象
     */
    @Resource
    private BlackListService blackListService;

    /**
     * 增加黑名单
     */
    @PreAuthorize("hasAnyAuthority('blog:black:add')")
    @Operation(summary = "添加黑名单")
    @Parameter(name = "addBlackListDTO", description = "添加黑名单DTO")
    @LogAnnotation(module = "黑名单管理", operation = LogConst.INSERT)
    @AccessLimit(seconds = 60, maxCount = 30)
    @PostMapping("/add")
    public ResultData<Void> addBlackList(@RequestBody @Valid AddBlackListDTO addBlackListDTO) throws BlackListException {
        return blackListService.addBlackList(addBlackListDTO);
    }

    /**
     * 修改黑名单
     */
    @PreAuthorize("hasAnyAuthority('blog:black:update')")
    @Operation(summary = "修改黑名单")
    @Parameter(name = "updateBlackListDTO", description = "修改黑名单")
    @LogAnnotation(module = "黑名单管理", operation = LogConst.UPDATE)
    @AccessLimit(seconds = 60, maxCount = 30)
    @PutMapping("/update")
    public ResultData<Void> updateBlackList(@RequestBody @Valid UpdateBlackListDTO updateBlackListDTO) {
        return blackListService.updateBlackList(updateBlackListDTO);
    }

    /**
     * 查询黑名单
     */
    @PreAuthorize("hasAnyAuthority('blog:black:select')")
    @Operation(summary = "查询黑名单")
    @LogAnnotation(module = "黑名单管理", operation = LogConst.GET)
    @AccessLimit(seconds = 60, maxCount = 30)
    @PostMapping("/getBlackListing")
    public ResultData<List<BlackListVO>> getBlackList(@RequestBody(required = false) SearchBlackListDTO searchBlackListDTO) {
        return messageHandler(() -> blackListService.getBlackList(searchBlackListDTO));
    }

    /**
     * 删除黑名单
     */
    @PreAuthorize("hasAnyAuthority('blog:black:delete')")
    @Operation(summary = "删除黑名单")
    @Parameter(name = "id", description = "id")
    @LogAnnotation(module = "黑名单管理", operation = LogConst.DELETE)
    @AccessLimit(seconds = 60, maxCount = 30)
    @DeleteMapping("/delete")
    public ResultData<Void> deleteBlackList(@RequestBody List<Long> ids) {
        return blackListService.deleteBlackList(ids);
    }

}

