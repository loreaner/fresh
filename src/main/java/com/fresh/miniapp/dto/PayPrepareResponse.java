package com.fresh.miniapp.dto;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class PayPrepareResponse implements Serializable {
    private Long orderId;
    private String orderNo;
    private Integer totalPrice;
}