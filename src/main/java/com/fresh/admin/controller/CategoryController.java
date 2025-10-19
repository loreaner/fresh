package com.fresh.admin.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fresh.admin.dto.CategoryRequest;
import com.fresh.common.response.PageResult;
import com.fresh.common.response.Result;
import com.fresh.core.entity.Category;
import com.fresh.core.entity.Product;
import com.fresh.core.service.CategoryService;
import com.fresh.core.service.ProductService;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;


/**
 * 管理端商品分类控制器
 */
@RestController("adminCategoryController")
@RequestMapping("/admin/category")
public class CategoryController {

    @Resource
    private CategoryService categoryService;
    
    @Resource
    private ProductService productService;

    /**
     * 获取分类列表
     */
    @GetMapping("/list")
    public Result<List<Category>> getCategoryList() {
        List<Category> categories = categoryService.list();
        return Result.success(categories);
    }

    /**
     * 分页查询分类
     */
    @GetMapping("/page")
    public Result<PageResult<Category>> getCategoryPage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String keyword) {
        
        Page<Category> page = categoryService.getCategoryPage(pageNum, pageSize, keyword);
        return Result.success(new PageResult<>(page.getTotal(), page.getRecords()));
    }

    /**
     * 获取分类详情
     */
    @GetMapping("/detail/{id}")
    public Result<Category> getCategoryDetail(@PathVariable Long id) {
        Category category = categoryService.getById(id);
        if (category == null) {
            return Result.error("分类不存在");
        }
        return Result.success(category);
    }

    /**
     * 添加分类
     */
    @PostMapping("/add")
    public Result<Void> addCategory(@RequestBody CategoryRequest request) {
        Category category = new Category();
        BeanUtils.copyProperties(request, category);
        category.setCreateTime(LocalDateTime.now());
        category.setUpdateTime(LocalDateTime.now());
        
        boolean success = categoryService.save(category);
        return success ? Result.success() : Result.error("添加失败");
    }

    /**
     * 更新分类
     */
    @PutMapping("/update")
    public Result<Void> updateCategory(@RequestBody CategoryRequest request) {
        if (request.getId() == null) {
            return Result.error("分类ID不能为空");
        }
        
        Category category = categoryService.getById(request.getId());
        if (category == null) {
            return Result.error("分类不存在");
        }
        
        BeanUtils.copyProperties(request, category);
        category.setUpdateTime(LocalDateTime.now());
        
        boolean success = categoryService.updateById(category);
        return success ? Result.success() : Result.error("更新失败");
    }

    /**
     * 删除分类
     */
    @DeleteMapping("/delete/{id}")
    public Result<Void> deleteCategory(@PathVariable Long id) {
        Category category = categoryService.getById(id);
        if (category == null) {
            return Result.error("分类不存在");
        }
        
        // 检查是否有商品使用此分类
        Page<Product> productPage = productService.getProductPage(1, 1, id);
        if (productPage.getTotal() > 0) {
            return Result.error("该分类下有商品，不能删除");
        }
        
        boolean success = categoryService.removeById(id);
        return success ? Result.success() : Result.error("删除失败");
    }

    /**
     * 调整分类排序
     */
    @PutMapping("/sort")
    public Result<Void> updateSort(@RequestParam Long id, @RequestParam Integer sort) {
        Category category = categoryService.getById(id);
        if (category == null) {
            return Result.error("分类不存在");
        }
        
        category.setSort(sort);
        category.setUpdateTime(LocalDateTime.now());
        
        boolean success = categoryService.updateById(category);
        return success ? Result.success() : Result.error("更新排序失败");
    }
}