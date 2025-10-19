package com.fresh.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fresh.core.entity.Cart;

import java.util.List;

public interface CartService extends IService<Cart> {
    
    /**
     * 获取用户购物车列表
     */
    List<Cart> getUserCarts(Long userId);
    
    /**
     * 添加商品到购物车
     */
    boolean addToCart(Cart cart);
    
    /**
     * 更新购物车商品数量
     */
    boolean updateCartQuantity(Cart cart);
    
    /**
     * 清空用户购物车
     */
    boolean clearCart(Long userId);
}