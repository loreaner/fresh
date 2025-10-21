package com.fresh.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fresh.core.entity.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProductMapper extends BaseMapper<Product> {
    IPage<Product> selectProductPage(Page<Product> page, @Param("categoryId")Long categoryId);
    IPage<Product> selectProductPage(Page<Product> page, @Param("categoryId")Long categoryId, @Param("keyword") String keyword);
    List<Product> selectProductList(@Param("categoryId") int categoryId);
    Product selectByCategoryIdAndName(@Param("categoryId") Long categoryId, @Param("name")String name);
}