package com.fresh.core.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fresh.core.entity.Product;
import com.fresh.core.mapper.ProductMapper;
import com.fresh.core.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {
    @Autowired
    ProductMapper productMapper;

    @Override
    public Page<Product> getProductPage(Integer pageNum, Integer pageSize,Long categoryId) {
        Page<Product> page = new Page<>(pageNum, pageSize);
        return (Page<Product>) baseMapper.selectProductPage(page, categoryId);
    }

    @Override
    public Page<Product> getProductPage(Integer pageNum, Integer pageSize,Long categoryId, String keyword) {
        Page<Product> page = new Page<>(pageNum, pageSize);
        return (Page<Product>) baseMapper.selectProductPage(page, categoryId, keyword);
    }

    @Override
    public List<Product> getProductList() {
        System.out.println(list()+"6666");
        return list();
    }

    @Override
    public Product getProductDetail(Long id) {
        return getById(id);
    }

    @Override
    public boolean addProduct(Product product) {
        return save(product);
    }

    @Override
    public boolean updateProduct(Product product) {
        product.setUpdateTime(LocalDateTime.now());
        return updateById(product);
    }

    @Override
    public boolean deleteProduct(Long id) {
        return removeById(id);
    }

    @Override
    public boolean updateStock(Long id, Integer stock) {
        Product product = new Product();
        product.setId(id);
        product.setStock(stock);
        product.setUpdateTime(LocalDateTime.now());
        return updateById(product);
    }
    
    @Override
    public boolean updateStatus(String name,Long categoryId,Integer status) {
        Product product = baseMapper.selectByCategoryIdAndName(categoryId, name);
        if (product == null) {
            return false;
        }
        
        product.setStatus(status);
        product.setUpdateTime(LocalDateTime.now());
        return updateById(product);
    }
}