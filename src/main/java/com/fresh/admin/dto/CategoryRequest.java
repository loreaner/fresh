package com.fresh.admin.dto;

import lombok.Data;

@Data
public class CategoryRequest {
    private Long id;
    private String name;
    private String description;
    private String image;
    private Integer sort;
    private Integer status;
}