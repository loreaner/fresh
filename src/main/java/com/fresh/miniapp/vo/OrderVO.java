package com.fresh.miniapp.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderVO {
    private Long id;
    private String orderNo;
    private BigDecimal totalAmount;
    private Integer status;
    private String statusText;
    private String remark;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    
    // 收货地址信息
    private String receiverName;
    private String receiverPhone;
    private String fullAddress;
    
    // 订单商品列表
    private List<OrderItemVO> items;
}