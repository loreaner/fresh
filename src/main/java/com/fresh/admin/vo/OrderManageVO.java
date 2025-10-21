package com.fresh.admin.vo;

import com.fresh.miniapp.vo.OrderItemVO;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderManageVO {
    private Long id;
    private String orderNo;
    private Long userId;
    private String username;
    private String phone;
    private Long addressId;
    private String addressDetail;
    private BigDecimal totalAmount;
    private Integer status;
    private String statusText;
    private String remark;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    
    // 订单项信息
    private List<OrderItemVO> items;
    
    // 配送信息
    private Long deliveryManId;
    private String deliveryManName;
    private String deliveryManPhone;
    private Integer deliveryStatus;
    private LocalDateTime pickupTime;
    private LocalDateTime deliveryTime;
}

