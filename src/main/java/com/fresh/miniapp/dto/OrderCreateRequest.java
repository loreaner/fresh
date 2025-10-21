package com.fresh.miniapp.dto;

import com.fresh.core.entity.Product;
import lombok.Data;
import java.io.Serializable;
import java.util.List;

@Data
public class OrderCreateRequest implements Serializable {
    private String phone;
    private String address;
    private String remark;
    private List<Cart> carts;
    private Integer totalPrice;

}