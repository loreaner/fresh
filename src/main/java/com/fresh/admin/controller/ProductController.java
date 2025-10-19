package com.fresh.admin.controller;


import com.fresh.admin.dto.ProductRequest;
import com.fresh.admin.vo.ProductManageVO;
import com.fresh.admin.vo.ProductStockRequest;

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
import java.util.stream.Collectors;

/**
 * 管理端商品控制器
 */
@RestController
@RequestMapping("/admin/product")
public class ProductController {

    @Resource
    private ProductService productService;

    @Resource
    private CategoryService categoryService;

    /**
     * 根据分类ID查询商品列表
     */
    @GetMapping("/list")
    public Result<List<Product>> getProductList() {
        List<Product> productList = productService.getProductList();
        System.out.println(productList);

        
        return Result.success(productList);
    }

    /**
     * 获取商品详情
     */
    @GetMapping("/detail/{id}")
    public Result<ProductManageVO> getProductDetail(@PathVariable Long id) {
        Product product = productService.getById(id);
        if (product == null) {
            return Result.error("商品不存在");
        }
        
        ProductManageVO vo = new ProductManageVO();
        BeanUtils.copyProperties(product, vo);
        
        // 设置分类名称
        if (product.getCategoryId() != null) {
            Category category = categoryService.getById(product.getCategoryId());
            if (category != null) {
                vo.setCategoryName(category.getName());
            }
        }
        
        return Result.success(vo);
    }

    /**
     * 添加商品
     */
    @PostMapping("/add")
    public Result<Void> addProduct(@RequestBody ProductRequest request) {
        // 检查分类是否存在
        if (request.getCategoryId() == null) {
            return Result.error("请选择商品分类");
        }
        
        Category category = categoryService.getById(request.getCategoryId());
        if (category == null) {
            return Result.error("选择的分类不存在");
        }
        
        Product product = new Product();
        BeanUtils.copyProperties(request, product);
        product.setCreateTime(LocalDateTime.now());
        product.setUpdateTime(LocalDateTime.now());
        
        boolean success = productService.addProduct(product);
        return success ? Result.success() : Result.error("添加失败");
    }

    /**
     * 更新商品
     */
    @PutMapping("/update")
    public Result<Void> updateProduct(@RequestBody ProductRequest request) {
        if (request.getId() == null) {
            return Result.error("商品ID不能为空");
        }
        
        Product product = productService.getById(request.getId());
        if (product == null) {
            return Result.error("商品不存在");
        }
        
        // 如果更新了分类，检查分类是否存在
        if (request.getCategoryId() != null && !request.getCategoryId().equals(product.getCategoryId())) {
            Category category = categoryService.getById(request.getCategoryId());
            if (category == null) {
                return Result.error("选择的分类不存在");
            }
        }
        
        BeanUtils.copyProperties(request, product);
        product.setUpdateTime(LocalDateTime.now());
        
        boolean success = productService.updateProduct(product);
        return success ? Result.success() : Result.error("更新失败");
    }

    /**
     * 删除商品
     */
    @DeleteMapping("/delete/{id}")
    public Result<Void> deleteProduct(@PathVariable Long id) {
        Product product = productService.getById(id);
        if (product == null) {
            return Result.error("商品不存在");
        }
        
        boolean success = productService.deleteProduct(id);
        return success ? Result.success() : Result.error("删除失败");
    }
    
    /**
     * 上架商品
     */
    @PutMapping("/online/{id}")
    public Result<Void> onlineProduct(@PathVariable Long id) {
        Product product = productService.getById(id);
        if (product == null) {
            return Result.error("商品不存在");
        }
        
        product.setStatus(1); // 1-上架
        product.setUpdateTime(LocalDateTime.now());
        boolean success = productService.updateProduct(product);
        return success ? Result.success() : Result.error("上架失败");
    }
    
    /**
     * 下架商品
     */
    @PutMapping("/offline")
    public Result<Void> offlineProduct(@RequestBody ProductRequest request ) {
        Product product = productService.getById(request.getId());
        if (product == null) {
            return Result.error("商品不存在");
        }
        
        product.setStatus(0); // 0-下架
        product.setUpdateTime(LocalDateTime.now());
        boolean success = productService.updateProduct(product);
        return success ? Result.success() : Result.error("下架失败");
    }
    
    /**
     * 更新商品库存
     */
    @PutMapping("/stock")
    public Result<Void> updateStock(@RequestBody ProductStockRequest request) {
        if (request.getStock() < 0) {
            return Result.error("库存不能小于0");
        }
        
        Product product = productService.getById(request.getId());
        if (product == null) {
            return Result.error("商品不存在");
        }
        
        boolean success = productService.updateStock(request.getId(), request.getStock());
        return success ? Result.success() : Result.error("更新库存失败");
    }
}