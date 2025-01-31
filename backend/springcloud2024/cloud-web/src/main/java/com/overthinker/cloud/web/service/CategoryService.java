package com.overthinker.cloud.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.overthinker.cloud.resp.ResultData;
import com.overthinker.cloud.web.entity.DTO.CategoryDTO;
import com.overthinker.cloud.web.entity.DTO.SearchCategoryDTO;
import com.overthinker.cloud.web.entity.PO.Category;
import com.overthinker.cloud.web.entity.VO.CategoryVO;

import java.util.List;


/**
 * (Category)表服务接口
 *
 * @author overH
 * @since 2023-10-15 02:29:13
 */
public interface CategoryService extends IService<Category> {
    /**
     * 查询所有分类
     *
     * @return vo
     */
    List<CategoryVO> listAllCategory();

    /**
     * 添加分类
     * @param categoryDTO 分类
     * @return 是否成功
     */
    ResultData<Void> addCategory(CategoryDTO categoryDTO);

    /**
     * 搜索分类
     * @param searchCategoryDTO 搜索标签DTO
     * @return 分类列表
     */
    List<CategoryVO> searchCategory(SearchCategoryDTO searchCategoryDTO);

    /**
     * 根据id查询
     * @param id id
     * @return 标签
     */
    CategoryVO getCategoryById(Long id);

    /**
     * 新增或修改标签
     * @param categoryDTO 标签DTO
     * @return 是否成功
     */
    ResultData<Void> addOrUpdateCategory(CategoryDTO categoryDTO);

    /**
     * 根据id删除
     * @param ids id
     * @return 是否成功
     */
    ResultData<Void> deleteCategoryByIds(List<Long> ids);

}
