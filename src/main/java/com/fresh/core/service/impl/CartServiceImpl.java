package com.fresh.core.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fresh.core.entity.Cart;
import com.fresh.core.mapper.CartMapper;
import com.fresh.core.service.CartService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CartServiceImpl extends ServiceImpl<CartMapper, Cart> implements CartService {

    @Override
    public List<Cart> getUserCarts(Long userId) {
        return baseMapper.selectByUserId(userId);
    }

    @Override
    public boolean addToCart(Cart cart) {
        // 检查是否已存在相同商品
        Cart existingCart = baseMapper.selectByUserIdAndProductId(cart.getUserId(), cart.getProductId());
        
        if (existingCart != null) {
            // 如果已存在，更新数量
            existingCart.setQuantity(existingCart.getQuantity() + cart.getQuantity());
            existingCart.setUpdateTime(LocalDateTime.now());
            return updateById(existingCart);
        } else {
            // 如果不存在，新增
            cart.setCreateTime(LocalDateTime.now());
            cart.setUpdateTime(LocalDateTime.now());
            return save(cart);
        }
    }

    @Override
    public boolean updateCartQuantity(Cart cart) {
        cart.setUpdateTime(LocalDateTime.now());
        return updateById(cart);
    }

    @Override
    public boolean clearCart(Long userId) {
        return baseMapper.deleteByUserId(userId) >= 0;
    }
}