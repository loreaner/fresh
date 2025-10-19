package com.fresh.miniapp.dto;

import lombok.Data;

/**
 * 购物车项DTO
 */
@Data
public class CartItemDto {
    private ProductDto product;     // 商品信息
    private Integer quantity;       // 购买数量
    private Integer price;          // 单价（分为单位，可能有优惠价格）
    
    /**
     * 计算小计金额（分为单位）
     */
    public Integer getSubtotal() {
        if (price != null && quantity != null) {
            return price * quantity;
        }
        return 0;
    }
    
    /**
     * 获取小计金额的元表示（保留两位小数）
     */
    public String getSubtotalInYuan() {
        Integer subtotal = getSubtotal();
        return String.format("%.2f", subtotal / 100.0);
    }
    
    /**
     * 获取单价的元表示（保留两位小数）
     */
    public String getPriceInYuan() {
        if (price == null) {
            return "0.00";
        }
        return String.format("%.2f", price / 100.0);
    }
}