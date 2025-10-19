package com.fresh.core.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("order_items")
public class OrderItem extends BaseEntity {
    private Long orderId;
    private Long productId;
    private String productName;
    private String productImage;
    private Integer price;      // 价格（分）
    private Integer quantity;
    
    /**
     * 获取价格（元）
     */
    public Double getPriceInYuan() {
        return price != null ? price / 100.0 : 0.0;
    }
    
    /**
     * 获取小计（分）
     */
    public Integer getSubtotal() {
        return price != null && quantity != null ? price * quantity : 0;
    }
    
    /**
     * 获取小计（元）
     */
    public Double getSubtotalInYuan() {
        return getSubtotal() / 100.0;
    }
}