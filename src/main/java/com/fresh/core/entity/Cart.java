package com.fresh.core.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("carts")
public class Cart extends BaseEntity {
    private Long userId;
    private Long productId;
    private Integer quantity;
}