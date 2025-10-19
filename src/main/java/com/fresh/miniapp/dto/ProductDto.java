package com.fresh.miniapp.dto;

import lombok.Data;

/**
 * 商品信息DTO
 */
@Data
public class ProductDto {
    private Long id;               // 商品ID
    private String name;           // 商品名称
    private String description;    // 商品描述
    private Integer price;         // 商品价格（分为单位，例如：12.80元 = 1280分）
    private String image;          // 商品图片
    private Long categoryId;       // 分类ID
    
    /**
     * 获取价格的元表示（保留两位小数）
     */
    public String getPriceInYuan() {
        if (price == null) {
            return "0.00";
        }
        return String.format("%.2f", price / 100.0);
    }
}