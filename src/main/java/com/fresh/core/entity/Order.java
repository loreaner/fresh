package com.fresh.core.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("orders")
public class Order extends BaseEntity {
    private Long userId;
    private String orderNo;
    private Integer totalAmount;        // 订单总金额（分）
    private Integer actualAmount;       // 实际支付金额（分）
    private Integer discountAmount;     // 优惠金额（分）
    private Integer deliveryFee;        // 配送费（分）
    private Integer status;
    private Integer paymentStatus;
    private String paymentMethod;       // 支付方式
    private LocalDateTime paymentTime;
    private Long addressId;             // 收货地址ID
    private String receiverName;
    private String receiverPhone;
    private String receiverAddress;
    private String province;
    private String city;
    private String district;
    private String detailAddress;
    private Long deliveryManId;         // 配送员ID
    private LocalDateTime deliveryTime;
    private LocalDateTime finishTime;
    private LocalDateTime cancelTime;
    private String cancelReason;
    private String remark;
    private Integer pointsEarned;       // 获得积分
    private Integer pointsUsed;         // 使用积分
    private String wechatOrderNo;       // 微信订单号
    private String wechatPrepayId;      // 微信预支付ID
}