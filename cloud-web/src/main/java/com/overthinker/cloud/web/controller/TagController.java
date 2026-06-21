package com.overthinker.cloud.web.controller;

import com.overthinker.cloud.common.core.annotation.LogAnnotation;
import com.overthinker.cloud.common.core.annotation.LogConst;
import com.overthinker.cloud.common.core.base.BaseController;
import com.overthinker.cloud.common.core.resp.ResultData;
import com.overthinker.cloud.system.starter.redis.annotation.AccessLimit;
import com.overthinker.cloud.web.entity.DTO.SearchTagDTO;
import com.overthinker.cloud.web.entity.DTO.TagDTO;
import com.overthinker.cloud.web.entity.VO.TagVO;
import com.overthinker.cloud.web.service.TagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 标签控制器
 * <p>
 * 处理文章标签的管理接口，包括新增、修改、删除、查询和搜索等操作
 * </p>
 *
 * @author overH
 * @since 2023-10-30
 */
@RestController
@Tag(name = "标签相关接口")
@RequestMapping("/tag")
@Validated
@RequiredArgsConstructor
public class TagController extends BaseController {

    private final TagService tagService;

    /**
     * 获取标签列表
     *
     * @return 标签列表
     */
    @Operation(summary = "获取标签列表")
    @GetMapping("/list")
    @AccessLimit(seconds = 60, maxCount = 60)
    public ResultData<List<TagVO>> list() {
        return messageHandler(tagService::listAllTag);
    }

    /**
     * 新增标签-文章列表
     *
     * @param tagDTO 标签信息
     * @return 操作结果
     */
    @Operation(summary = "新增标签-文章列表")
    @PreAuthorize("hasAnyAuthority('blog:tag:add')")
    @LogAnnotation(module = "标签管理", operation = LogConst.INSERT)
    @AccessLimit(seconds = 60, maxCount = 30)
    @PutMapping
    public ResultData<Void> addTag(@RequestBody @Valid TagDTO tagDTO) {
        return tagService.addTag(tagDTO);
    }

    /**
     * 获取后台标签列表
     *
     * @return 标签列表
     */
    @Operation(summary = "获取标签列表")
    @PreAuthorize("hasAnyAuthority('blog:tag:list')")
    @LogAnnotation(module = "标签管理", operation = LogConst.GET)
    @AccessLimit(seconds = 60, maxCount = 30)
    @GetMapping("/back/list")
    public ResultData<List<TagVO>> listArticleTag() {
        return messageHandler(tagService::listAllTag);
    }

    /**
     * 搜索标签列表
     *
     * @param searchTagDTO 搜索条件
     * @return 标签列表
     */
    @Operation(summary = "搜索标签列表")
    @PreAuthorize("hasAnyAuthority('blog:tag:search')")
    @LogAnnotation(module = "标签管理", operation = LogConst.SEARCH)
    @AccessLimit(seconds = 60, maxCount = 30)
    @PostMapping("/back/search")
    public ResultData<List<TagVO>> searchTag(@RequestBody SearchTagDTO searchTagDTO) {
        return messageHandler(() -> tagService.searchTag(searchTagDTO));
    }

    /**
     * 根据ID查询标签
     *
     * @param id 标签ID
     * @return 标签信息
     */
    @Operation(summary = "根据id查询标签")
    @PreAuthorize("hasAnyAuthority('blog:tag:search')")
    @LogAnnotation(module = "标签管理", operation = LogConst.GET)
    @AccessLimit(seconds = 60, maxCount = 30)
    @GetMapping("/back/get/{id}")
    public ResultData<TagVO> getTagById(@PathVariable("id") Long id) {
        return messageHandler(() -> tagService.getTagById(id));
    }

    /**
     * 新增标签-标签列表
     *
     * @param tagDTO 标签信息
     * @return 操作结果
     */
    @Operation(summary = "新增标签-标签列表")
    @PreAuthorize("hasAnyAuthority('blog:tag:add')")
    @LogAnnotation(module = "标签管理", operation = LogConst.INSERT)
    @AccessLimit(seconds = 60, maxCount = 30)
    @PutMapping("/back/add")
    public ResultData<Void> addOrUpdateTag(@RequestBody @Valid TagDTO tagDTO) {
        return tagService.addOrUpdateTag(tagDTO.setId(null));
    }

    /**
     * 修改标签
     *
     * @param tagDTO 标签信息
     * @return 操作结果
     */
    @Operation(summary = "修改标签")
    @PreAuthorize("hasAnyAuthority('blog:tag:update')")
    @LogAnnotation(module = "标签管理", operation = LogConst.UPDATE)
    @AccessLimit(seconds = 60, maxCount = 30)
    @PostMapping("/back/update")
    public ResultData<Void> updateTag(@RequestBody @Valid TagDTO tagDTO) {
        return tagService.addOrUpdateTag(tagDTO);
    }

    /**
     * 删除标签
     *
     * @param ids 标签ID列表
     * @return 操作结果
     */
    @Operation(summary = "删除标签")
    @PreAuthorize("hasAnyAuthority('blog:tag:delete')")
    @LogAnnotation(module = "标签管理", operation = LogConst.DELETE)
    @AccessLimit(seconds = 60, maxCount = 30)
    @DeleteMapping("/back/delete")
    public ResultData<Void> deleteTag(@RequestBody List<Long> ids) {
        return tagService.deleteTagByIds(ids);
    }
}