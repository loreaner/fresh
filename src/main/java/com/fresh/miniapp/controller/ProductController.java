package com.fresh.miniapp.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fresh.common.response.PageResult;
import com.fresh.common.response.Result;
import com.fresh.core.entity.Product;
import com.fresh.core.service.ProductService;
import com.fresh.miniapp.vo.ProductVO;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 小程序商品控制器
 */
@RestController("miniAppProductController")
@RequestMapping("/miniapp/product")
public class ProductController {

    @Resource
    private ProductService productService;

    @GetMapping("/list")
    public Result<PageResult<ProductVO>> getProductList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Long categoryId) {
        
        Page<Product> productPage = productService.getProductPage(pageNum, pageSize, categoryId);
        
        List<ProductVO> productVOList = productPage.getRecords().stream().map(product -> {
            ProductVO vo = new ProductVO();
            BeanUtils.copyProperties(product, vo);
            return vo;
        }).collect(Collectors.toList());
        
        return Result.success(PageResult.of(productPage.getTotal(), productVOList));
    }

    @GetMapping("/detail/{id}")
    public Result<ProductVO> getProductDetail(@PathVariable Long id) {
        Product product = productService.getProductDetail(id);
        if (product == null) {
            return Result.error("商品不存在");
        }
        
        ProductVO vo = new ProductVO();
        BeanUtils.copyProperties(product, vo);
        return Result.success(vo);
    }
}