package com.fresh.miniapp.dto;

import lombok.Data;
import java.io.Serializable;

@Data
public class OrderCreateRequest implements Serializable {
    private Long userId;
    private Long addressId;
    private String remark;
    private Long productId;
    private Integer quantity;
}