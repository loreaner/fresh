package com.fresh.admin.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductRequest {
    private Long id;
    private Long categoryId;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stock;
    private String image;
    private Integer status;
}