package com.fresh.miniapp.dto;

import lombok.Data;
import java.io.Serializable;

@Data
public class OrderItemRequest implements Serializable {
    private Long productId;
    private Integer quantity;
}