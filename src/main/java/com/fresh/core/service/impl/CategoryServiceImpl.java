package com.fresh.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fresh.core.entity.Category;
import com.fresh.core.mapper.CategoryMapper;
import com.fresh.core.service.CategoryService;
import com.fresh.core.service.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    
    @Resource
    private ProductService productService;

    @Override
    public Page<Category> getCategoryPage(Integer pageNum, Integer pageSize, String keyword) {
        Page<Category> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        
        if (StringUtils.hasText(keyword)) {
            wrapper.like(Category::getName, keyword);
        }
        
        wrapper.orderByDesc(Category::getCreateTime);
        return page(page, wrapper);
    }

    @Override
    public Category getCategoryDetail(Long id) {
        return getById(id);
    }

    @Override
    public boolean addCategory(Category category) {
        return save(category);
    }

    @Override
    public boolean updateCategory(Category category) {
        return updateById(category);
    }

    @Override
    public boolean deleteCategory(Long id) {
        return removeById(id);
    }
    
    @Override
    public boolean hasProducts(Long id) {
        // 检查是否有商品使用此分类
        Page<?> productPage = productService.getProductPage(1, 1, id);
        return productPage.getTotal() > 0;
    }
}
