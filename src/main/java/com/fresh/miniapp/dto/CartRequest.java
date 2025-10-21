package com.fresh.miniapp.dto;

import lombok.Data;

@Data
public class CartRequest {
    private Long id;
    private Long userId;
    private Long productId;
    private Integer quantity;
}