package com.fresh.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fresh.core.entity.Cart;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CartMapper extends BaseMapper<Cart> {
    
    /**
     * 根据用户ID获取购物车列表
     */
    List<Cart> selectByUserId(@Param("userId") Long userId);
    
    /**
     * 根据用户ID和商品ID查询购物车项
     */
    Cart selectByUserIdAndProductId(@Param("userId") Long userId, @Param("productId") Long productId);
    
    /**
     * 清空用户购物车
     */
    int deleteByUserId(@Param("userId") Long userId);
}