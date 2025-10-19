package com.fresh.miniapp.controller;

import com.fresh.common.response.Result;
import com.fresh.core.entity.Cart;
import com.fresh.core.service.CartService;
import com.fresh.miniapp.dto.CartRequest;
import com.fresh.miniapp.vo.CartVO;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 小程序购物车控制器
 */
@RestController
@RequestMapping("/miniapp/cart")
public class CartController {

    @Resource
    private CartService cartService;

    /**
     * 获取用户购物车列表
     */
    @GetMapping("/list/{userId}")
    public Result<List<CartVO>> getCartList(@PathVariable Long userId) {
        List<Cart> carts = cartService.getUserCarts(userId);
        List<CartVO> voList = carts.stream().map(cart -> {
            CartVO vo = new CartVO();
            BeanUtils.copyProperties(cart, vo);
            return vo;
        }).collect(Collectors.toList());
        return Result.success(voList);
    }

    /**
     * 添加商品到购物车
     */
    @PostMapping("/add")
    public Result<Void> addToCart(@RequestBody CartRequest request) {
        Cart cart = new Cart();
        BeanUtils.copyProperties(request, cart);
        
        boolean success = cartService.addToCart(cart);
        return success ? Result.success() : Result.error("添加失败");
    }

    /**
     * 更新购物车商品数量
     */
    @PutMapping("/update")
    public Result<Void> updateCartQuantity(@RequestBody CartRequest request) {
        if (request.getId() == null) {
            return Result.error("购物车ID不能为空");
        }
        
        Cart cart = new Cart();
        BeanUtils.copyProperties(request, cart);
        
        boolean success = cartService.updateCartQuantity(cart);
        return success ? Result.success() : Result.error("更新失败");
    }

    /**
     * 删除购物车商品
     */
    @DeleteMapping("/delete/{id}")
    public Result<Void> deleteCartItem(@PathVariable Long id) {
        boolean success = cartService.removeById(id);
        return success ? Result.success() : Result.error("删除失败");
    }

    /**
     * 清空用户购物车
     */
    @DeleteMapping("/clear/{userId}")
    public Result<Void> clearCart(@PathVariable Long userId) {
        boolean success = cartService.clearCart(userId);
        return success ? Result.success() : Result.error("清空失败");
    }
}