package com.fresh.delivery.dto;

import lombok.Data;

@Data
public class DeliveryManRequest {
    private Long id;
    private String username;
    private String phone;
    private Integer status;
}