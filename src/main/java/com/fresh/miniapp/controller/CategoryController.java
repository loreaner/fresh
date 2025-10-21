package com.fresh.miniapp.controller;

import com.fresh.common.response.Result;
import com.fresh.core.entity.Category;
import com.fresh.core.service.CategoryService;
import com.fresh.miniapp.vo.CategoryVO;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 小程序商品分类控制器
 */
@RestController("miniAppCategoryController")
@RequestMapping("/miniapp/category")
public class CategoryController {

    @Resource
    private CategoryService categoryService;

    /**
     * 获取所有分类
     */
    @GetMapping("/list")
    public Result<List<CategoryVO>> getCategoryList() {
        List<Category> categories = categoryService.list();
        List<CategoryVO> voList = categories.stream().map(category -> {
            CategoryVO vo = new CategoryVO();
            BeanUtils.copyProperties(category, vo);
            return vo;
        }).collect(Collectors.toList());
        return Result.success(voList);
    }

    /**
     * 获取分类详情
     */
    @GetMapping("/detail/{id}")
    public Result<CategoryVO> getCategoryDetail(@PathVariable Long id) {
        Category category = categoryService.getById(id);
        if (category == null) {
            return Result.error("分类不存在");
        }
        
        CategoryVO vo = new CategoryVO();
        BeanUtils.copyProperties(category, vo);
        return Result.success(vo);
    }
}