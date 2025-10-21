package com.fresh.miniapp.dto;

import lombok.Data;

import java.util.List;

@Data
public class OrderCreateRequest {
    private String phone;
    private String address;
    private String remark;
    private List<Cart> carts;
    private Integer totalPrice;

}