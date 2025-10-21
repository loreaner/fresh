package com.fresh.miniapp.dto;

import lombok.Data;
import java.io.Serializable;
import java.util.List;

/**
 * 购物车结算请求DTO
 */
@Data
public class CartCheckoutRequest implements Serializable {
    
    /**
     *
     */
    private String phone;
    /**
     * 收货地址ID
     */
    private String address;
    
    /**
     * 商品列表（支持多个商品）
     */
    private List<OrderItemRequest> items;
    
    /**
     * 订单备注
     */
    private String remark;
}