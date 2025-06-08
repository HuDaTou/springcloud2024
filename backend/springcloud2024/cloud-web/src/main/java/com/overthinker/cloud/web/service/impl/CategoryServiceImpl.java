package com.overthinker.cloud.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.overthinker.cloud.resp.ResultData;
import com.overthinker.cloud.web.entity.DTO.CategoryDTO;
import com.overthinker.cloud.web.entity.DTO.SearchCategoryDTO;
import com.overthinker.cloud.web.entity.PO.Article;
import com.overthinker.cloud.web.entity.PO.Category;
import com.overthinker.cloud.web.entity.VO.CategoryVO;
import com.overthinker.cloud.web.entity.constants.FunctionConst;
import com.overthinker.cloud.web.mapper.ArticleMapper;
import com.overthinker.cloud.web.mapper.CategoryMapper;
import com.overthinker.cloud.web.service.CategoryService;
import com.overthinker.cloud.web.utils.StringUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * (Category)表服务实现类
 *
 * @author overH
 * @since 2023-10-15 02:29:14
 */
@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Resource
    private ArticleMapper articleMapper;

    @Resource
    private CategoryMapper categoryMapper;

    @Override
    public List<CategoryVO> listAllCategory() {
        List<Category> categories = this.query().list();

        return categories.stream().map(category -> category.copyProperties(CategoryVO.class, item -> {
            item.setArticleCount(articleMapper.selectCount(new LambdaQueryWrapper<Article>().eq(Article::getCategoryId, category.getId())));
        })).toList();
    }

    @Override
    public ResultData<Void> addCategory(CategoryDTO categoryDTO) {
        categoryDTO.setId(null);
        if (this.save(categoryDTO.copyProperties(Category.class))) return ResultData.success();
        return ResultData.failure();
    }

    @Override
    public List<CategoryVO> searchCategory(SearchCategoryDTO searchCategoryDTO) {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(searchCategoryDTO.getCategoryName()), Category::getCategoryName, searchCategoryDTO.getCategoryName());
        if (StringUtils.isNotNull(searchCategoryDTO.getStartTime()) && StringUtils.isNotNull(searchCategoryDTO.getEndTime()))
            queryWrapper.between(Category::getCreateTime, searchCategoryDTO.getStartTime(), searchCategoryDTO.getEndTime());

        return categoryMapper.selectList(queryWrapper)
                .stream()
                .map(category ->
                        category.copyProperties(CategoryVO.class, item ->
                                item.setArticleCount(articleMapper.selectCount(new LambdaQueryWrapper<Article>()
                                        .eq(Article::getCategoryId, category.getId())))))
                .toList();
    }

    @Override
    public CategoryVO getCategoryById(Long id) {
        return categoryMapper.selectById(id).copyProperties(CategoryVO.class);
    }

    @Transactional
    @Override
    public ResultData<Void> addOrUpdateCategory(CategoryDTO categoryDTO) {
        if (this.saveOrUpdate(categoryDTO.copyProperties(Category.class))) return ResultData.success();
        return ResultData.failure();
    }

    @Transactional
    @Override
    public ResultData<Void> deleteCategoryByIds(List<Long> ids) {
        // 是否有剩下文章
        Long count = articleMapper.selectCount(new LambdaQueryWrapper<Article>().in(Article::getCategoryId, ids));
        if (count > 0) return ResultData.failure(FunctionConst.CATEGORY_EXIST_ARTICLE);
        // 执行删除
        if (this.removeByIds(ids)) return ResultData.success();
        return ResultData.failure();
    }
}
