package com.fresh.core.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("order_items")
public class OrderItem extends BaseEntity {
    private int orderId;
    private int productId;
    private String productName;
    private String productImage;
    private BigDecimal price;
    private Integer quantity;
}