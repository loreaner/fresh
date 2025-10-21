package com.fresh.core.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fresh.miniapp.dto.Cart;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("orders")
public class Order extends BaseEntity {
    private String phone;
    private String orderNo;
    private List<Cart> carts;
    private Integer totalPrice;
    private Integer status;
    private LocalDateTime paymentTime;
    private String name;
    private String receiverAddress;
    private LocalDateTime deliveryTime;
    private LocalDateTime finishTime;
    private String remark;
}