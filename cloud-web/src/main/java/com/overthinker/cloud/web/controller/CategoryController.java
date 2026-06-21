package com.overthinker.cloud.web.controller;

import com.overthinker.cloud.common.core.annotation.LogAnnotation;
import com.overthinker.cloud.common.core.annotation.LogConst;
import com.overthinker.cloud.common.core.base.BaseController;
import com.overthinker.cloud.common.core.resp.ResultData;
import com.overthinker.cloud.system.starter.redis.annotation.AccessLimit;
import com.overthinker.cloud.web.entity.DTO.CategoryDTO;
import com.overthinker.cloud.web.entity.DTO.SearchCategoryDTO;
import com.overthinker.cloud.web.entity.VO.CategoryVO;
import com.overthinker.cloud.web.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 分类控制器
 * <p>
 * 处理文章分类的管理接口，包括新增、修改、删除、查询和搜索等操作
 * </p>
 *
 * @author overH
 * @since 2023-10-27
 */
@RestController
@Tag(name = "分类相关接口")
@RequestMapping("/category")
@Validated
@RequiredArgsConstructor
public class CategoryController extends BaseController {

    private final CategoryService categoryService;

    /**
     * 获取所有分类
     *
     * @return 分类列表
     */
    @Operation(summary = "获取所有分类")
    @AccessLimit(seconds = 60, maxCount = 60)
    @GetMapping("/list")
    public ResultData<List<CategoryVO>> listAllCategory() {
        return messageHandler(categoryService::listAllCategory);
    }

    /**
     * 新增分类-文章列表
     *
     * @param categoryDTO 分类信息
     * @return 操作结果
     */
    @Operation(summary = "新增分类-文章列表")
    @PreAuthorize("hasAnyAuthority('blog:category:add')")
    @LogAnnotation(module = "新增分类", operation = LogConst.INSERT)
    @AccessLimit(seconds = 60, maxCount = 30)
    @PutMapping
    public ResultData<Void> addCategory(@RequestBody @Valid CategoryDTO categoryDTO) {
        return categoryService.addCategory(categoryDTO);
    }

    /**
     * 获取后台分类列表
     *
     * @return 分类列表
     */
    @Operation(summary = "获取分类列表")
    @PreAuthorize("hasAnyAuthority('blog:category:list')")
    @LogAnnotation(module = "分类管理", operation = LogConst.GET)
    @AccessLimit(seconds = 60, maxCount = 30)
    @GetMapping("/back/list")
    public ResultData<List<CategoryVO>> listArticleCategory() {
        return messageHandler(categoryService::listAllCategory);
    }

    /**
     * 搜索分类列表
     *
     * @param searchCategoryDTO 搜索条件
     * @return 分类列表
     */
    @Operation(summary = "搜索分类列表")
    @PreAuthorize("hasAnyAuthority('blog:category:search')")
    @LogAnnotation(module = "分类管理", operation = LogConst.SEARCH)
    @AccessLimit(seconds = 60, maxCount = 30)
    @PostMapping("/back/search")
    public ResultData<List<CategoryVO>> searchCategory(@RequestBody SearchCategoryDTO searchCategoryDTO) {
        return messageHandler(() -> categoryService.searchCategory(searchCategoryDTO));
    }

    /**
     * 根据ID查询分类
     *
     * @param id 分类ID
     * @return 分类信息
     */
    @Operation(summary = "根据id查询分类")
    @PreAuthorize("hasAnyAuthority('blog:category:search')")
    @LogAnnotation(module = "分类管理", operation = LogConst.GET)
    @AccessLimit(seconds = 60, maxCount = 30)
    @GetMapping("/back/get/{id}")
    public ResultData<CategoryVO> getCategoryById(@PathVariable("id") Long id) {
        return messageHandler(() -> categoryService.getCategoryById(id));
    }

    /**
     * 新增分类-分类列表
     *
     * @param categoryDTO 分类信息
     * @return 操作结果
     */
    @Operation(summary = "新增分类-分类列表")
    @PreAuthorize("hasAnyAuthority('blog:category:add')")
    @LogAnnotation(module = "分类管理", operation = LogConst.INSERT)
    @AccessLimit(seconds = 60, maxCount = 30)
    @PutMapping("/back/add")
    public ResultData<Void> addOrUpdateCategory(@RequestBody @Valid CategoryDTO categoryDTO) {
        return categoryService.addOrUpdateCategory(categoryDTO.setId(null));
    }

    /**
     * 修改分类
     *
     * @param categoryDTO 分类信息
     * @return 操作结果
     */
    @Operation(summary = "修改分类")
    @PreAuthorize("hasAnyAuthority('blog:category:update')")
    @LogAnnotation(module = "分类管理", operation = LogConst.UPDATE)
    @AccessLimit(seconds = 60, maxCount = 30)
    @PostMapping("/back/update")
    public ResultData<Void> updateCategory(@RequestBody @Valid CategoryDTO categoryDTO) {
        return categoryService.addOrUpdateCategory(categoryDTO);
    }

    /**
     * 删除分类
     *
     * @param ids 分类ID列表
     * @return 操作结果
     */
    @Operation(summary = "删除分类")
    @PreAuthorize("hasAnyAuthority('blog:category:delete')")
    @LogAnnotation(module = "分类管理", operation = LogConst.DELETE)
    @AccessLimit(seconds = 60, maxCount = 30)
    @DeleteMapping("/back/delete")
    public ResultData<Void> deleteCategory(@RequestBody List<Long> ids) {
        return categoryService.deleteCategoryByIds(ids);
    }
}