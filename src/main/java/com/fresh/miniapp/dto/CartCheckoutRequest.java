package com.fresh.miniapp.dto;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 购物车结算请求DTO
 */
@Data
public class CartCheckoutRequest implements Serializable {
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 收货地址信息（完整地址对象，不再使用ID）
     */
    private String address;
    
    /**
     * 购物车商品列表（包含商品详情、数量和价格）
     */
    private List<CartItemDto> cart;
    
    /**
     * 订单总价
     */
    private int totalPrice;
    
    /**
     * 订单备注
     */
    private String remark;
    


}