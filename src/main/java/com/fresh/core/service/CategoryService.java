package com.fresh.core.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fresh.core.entity.Category;

public interface CategoryService extends IService<Category> {

    Page<Category> getCategoryPage(Integer pageNum, Integer pageSize, String keyword);

    Category getCategoryDetail(Long id);

    boolean addCategory(Category category);

    boolean updateCategory(Category category);

    boolean deleteCategory(Long id);

    /**
     * 检查分类下是否有商品
     * @param id 分类ID
     * @return 是否有商品
     */
    boolean hasProducts(Long id);
}
