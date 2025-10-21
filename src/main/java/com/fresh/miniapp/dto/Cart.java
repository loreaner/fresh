package com.fresh.miniapp.dto;

import com.fresh.core.entity.Product;
import lombok.Data;

@Data
public class Cart {
    private Product product;
    private Integer quantity;
}
