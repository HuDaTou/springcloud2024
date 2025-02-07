package com.overthinker.cloud.web.controller;

import com.alibaba.fastjson.JSON;
import com.overthinker.cloud.resp.ResultData;
import com.overthinker.cloud.web.annotation.AccessLimit;
import com.overthinker.cloud.web.annotation.CheckBlacklist;
import com.overthinker.cloud.web.annotation.LogAnnotation;
import com.overthinker.cloud.web.controller.base.BaseController;
import com.overthinker.cloud.web.entity.DTO.SearchTreeHoleDTO;
import com.overthinker.cloud.web.entity.DTO.TreeHoleIsCheckDTO;
import com.overthinker.cloud.web.entity.VO.TreeHoleListVO;
import com.overthinker.cloud.web.entity.VO.TreeHoleVO;
import com.overthinker.cloud.web.entity.constants.LogConst;
import com.overthinker.cloud.web.service.TreeHoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

;

/**
 * @author overH
 * <p>
 * 创建时间：2023/10/30 11:20
 */
@RestController
@Tag(name = "树洞相关接口")
@RequestMapping("/treeHole")
@Validated
public class TreeHoleController extends BaseController {

    @Resource
    private TreeHoleService treeHoleService;

    @CheckBlacklist
    @Operation(summary = "添加树洞")
    @AccessLimit(seconds = 60, maxCount = 60)
    @PostMapping("/auth/addTreeHole")
    public ResultData<Void> addTreeHole(@Valid @NotNull @RequestBody String content) {
        return treeHoleService.addTreeHole(JSON.parseObject(content).getString("content"));
    }

    @Operation(summary = "查看树洞")
    @AccessLimit(seconds = 60, maxCount = 60)
    @GetMapping("/getTreeHoleList")
    public ResultData<List<TreeHoleVO>> getTreeHoleList() {
        return messageHandler(() -> treeHoleService.getTreeHole());
    }

    @PreAuthorize("hasAnyAuthority('blog:treeHole:list')")
    @Operation(summary = "后台树洞列表")
    @AccessLimit(seconds = 60, maxCount = 30)
    @LogAnnotation(module="树洞管理",operation= LogConst.GET)
    @GetMapping("/back/list")
    public ResultData<List<TreeHoleListVO>> backList() {
        return messageHandler(() -> treeHoleService.getBackTreeHoleList(null));
    }

    @PreAuthorize("hasAnyAuthority('blog:treeHole:search')")
    @Operation(summary = "搜索后台树洞列表")
    @AccessLimit(seconds = 60, maxCount = 30)
    @LogAnnotation(module="树洞管理",operation= LogConst.SEARCH)
    @PostMapping("/back/search")
    public ResultData<List<TreeHoleListVO>> backList(@RequestBody SearchTreeHoleDTO searchDTO) {
        return messageHandler(() -> treeHoleService.getBackTreeHoleList(searchDTO));
    }

    @PreAuthorize("hasAnyAuthority('blog:treeHole:isCheck')")
    @Operation(summary = "修改树洞是否通过")
    @AccessLimit(seconds = 60, maxCount = 30)
    @LogAnnotation(module="树洞管理",operation= LogConst.UPDATE)
    @PostMapping("/back/isCheck")
    public ResultData<Void> isCheck(@RequestBody @Valid TreeHoleIsCheckDTO treeHoleIsCheckDTO) {
        return treeHoleService.isCheckTreeHole(treeHoleIsCheckDTO);
    }

    @PreAuthorize("hasAnyAuthority('blog:treeHole:delete')")
    @Operation(summary = "删除树洞")
    @AccessLimit(seconds = 60, maxCount = 30)
    @LogAnnotation(module="树洞管理",operation= LogConst.DELETE)
    @DeleteMapping("/back/delete")
    public ResultData<Void> delete(@RequestBody List<Long> ids) {
        return treeHoleService.deleteTreeHole(ids);
    }

}
