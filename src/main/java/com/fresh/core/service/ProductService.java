package com.fresh.core.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fresh.core.entity.Product;

import java.util.List;

public interface ProductService extends IService<Product> {
    Page<Product> getProductPage(Integer pageNum, Integer pageSize,Long categoryId);
    Page<Product> getProductPage(Integer pageNum, Integer pageSize,Long categoryId, String keyword);
    List<Product> getProductList();
    Product getProductDetail(Long id);
    boolean addProduct(Product product);
    boolean updateProduct(Product product);
    boolean deleteProduct(Long id);
    boolean updateStock(Long id, Integer stock);
    boolean updateStatus(String name, Long categoryId,Integer status);
}