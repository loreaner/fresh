package com.fresh.admin.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
@Data
public class AdminOrderDetailDto {
    private String receiver_name;
    private String phone;
    private String address;
    private List<ProductDto> products;
    private Integer totalPrice;
    private int status;

    // Getters and Setters




    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<ProductDto> getProducts() {
        return products;
    }

    public void setProducts(List<ProductDto> products) {
        this.products = products;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Integer totalPrice) {
        this.totalPrice = totalPrice;
    }
}