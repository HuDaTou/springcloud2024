package com.overthinker.cloud.web.controller;

import com.overthinker.cloud.annotation.LogAnnotation;
import com.overthinker.cloud.controller.base.BaseController;
import com.overthinker.cloud.resp.ResultData;
import com.overthinker.cloud.annotation.AccessLimit;

import com.overthinker.cloud.web.entity.DTO.CategoryDTO;
import com.overthinker.cloud.web.entity.DTO.SearchCategoryDTO;
import com.overthinker.cloud.web.entity.VO.CategoryVO;
import com.overthinker.cloud.annotation.LogConst;
import com.overthinker.cloud.web.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author overH
 * <p>
 * 创建时间：2023/10/27 15:07
 */
@RestController
@Tag(name = "分类相关接口")
@RequestMapping("/category")
@Validated
public class CategoryController extends BaseController {

    @Resource
    private CategoryService categoryService;

    @Operation(summary = "获取所有分类")
    @AccessLimit(seconds = 60, maxCount = 60)
    @GetMapping("/list")
    public ResultData<List<CategoryVO>> listAllCategory() {
        return messageHandler((categoryService::listAllCategory));
    }

    @Operation(summary = "新增分类-文章列表")
    @PreAuthorize("hasAnyAuthority('blog:category:add')")
    @LogAnnotation(module = "新增分类", operation = LogConst.INSERT)
    @AccessLimit(seconds = 60, maxCount = 30)
    @PutMapping()
    public ResultData<Void> addCategory(@RequestBody @Valid CategoryDTO categoryDTO) {
        return categoryService.addCategory(categoryDTO);
    }

    @Operation(summary = "获取分类列表")
    @PreAuthorize("hasAnyAuthority('blog:category:list')")
    @LogAnnotation(module = "分类管理", operation = LogConst.GET)
    @AccessLimit(seconds = 60, maxCount = 30)
    @GetMapping("/back/list")
    public ResultData<List<CategoryVO>> listArticleCategory() {
        return messageHandler((categoryService::listAllCategory));
    }

    @Operation(summary = "搜索分类列表")
    @PreAuthorize("hasAnyAuthority('blog:category:search')")
    @LogAnnotation(module = "分类管理", operation = LogConst.SEARCH)
    @AccessLimit(seconds = 60, maxCount = 30)
    @PostMapping("/back/search")
    public ResultData<List<CategoryVO>> searchCategory(@RequestBody SearchCategoryDTO searchCategoryDTO) {
        return messageHandler(() -> categoryService.searchCategory(searchCategoryDTO));
    }

    @Operation(summary = "根据id查询分类")
    @PreAuthorize("hasAnyAuthority('blog:category:search')")
    @LogAnnotation(module = "分类管理", operation = LogConst.GET)
    @AccessLimit(seconds = 60, maxCount = 30)
    @GetMapping("/back/get/{id}")
    public ResultData<CategoryVO> getCategoryById(@PathVariable(value = "id") Long id) {
        return messageHandler(() -> categoryService.getCategoryById(id));
    }

    @Operation(summary = "新增分类-分类列表")
    @PreAuthorize("hasAnyAuthority('blog:category:add')")
    @LogAnnotation(module = "分类管理", operation = LogConst.INSERT)
    @AccessLimit(seconds = 60, maxCount = 30)
    @PutMapping("/back/add")
    public ResultData<Void> addOrUpdateCategory(@RequestBody @Valid CategoryDTO categoryDTO) {
        return categoryService.addOrUpdateCategory(categoryDTO.setId(null));
    }

    @Operation(summary = "修改分类")
    @PreAuthorize("hasAnyAuthority('blog:category:update')")
    @LogAnnotation(module = "分类管理", operation = LogConst.UPDATE)
    @AccessLimit(seconds = 60, maxCount = 30)
    @PostMapping("/back/update")
    public ResultData<Void> updateCategory(@RequestBody @Valid CategoryDTO categoryDTO) {
        return categoryService.addOrUpdateCategory(categoryDTO);
    }

    @Operation(summary = "删除分类")
    @PreAuthorize("hasAnyAuthority('blog:category:delete')")
    @LogAnnotation(module = "分类管理", operation = LogConst.DELETE)
    @AccessLimit(seconds = 60, maxCount = 30)
    @DeleteMapping("/back/delete")
    public ResultData<Void> deleteCategory(@RequestBody List<Long> ids) {
        return categoryService.deleteCategoryByIds(ids);
    }
}
