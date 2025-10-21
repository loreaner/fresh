package com.fresh.core.enums;

import lombok.Getter;

import javax.tools.Diagnostic;

@Getter
public enum OrderStatus {
    PENDING_PAYMENT(1, "待付款"),
    PENDING_SHIPMENT(2, "待发货"),
    SHIPPED(3, "已发货"),
    DELIVERED(4, "已送达"),
    COMPLETED(5, "已完成"),
    CANCELLED(6, "已取消"),
    REFUNDED(7, "已退款");

    public static final Diagnostic<Object> PENDING_DELIVERY = null ;
    private final Integer code;
    private final String description;

    OrderStatus(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    public static OrderStatus getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (OrderStatus status : OrderStatus.values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }

    public static String getDescriptionByCode(Integer code) {
        OrderStatus status = getByCode(code);
        return status != null ? status.getDescription() : "未知状态";
    }

    public static boolean canTransferTo(Integer fromStatus, Integer toStatus) {
        if (fromStatus == null || toStatus == null) {
            return false;
        }
        
        // 待付款 -> 待发货（支付成功后）
        if (fromStatus.equals(PENDING_PAYMENT.getCode()) && 
            toStatus.equals(PENDING_SHIPMENT.getCode())) {
            return true;
        }
        
        // 待付款 -> 已取消（用户取消或超时取消）
        if (fromStatus.equals(PENDING_PAYMENT.getCode()) && 
            toStatus.equals(CANCELLED.getCode())) {
            return true;
        }
        
        // 待发货 -> 已发货（商家发货后）
        if (fromStatus.equals(PENDING_SHIPMENT.getCode()) && 
            toStatus.equals(SHIPPED.getCode())) {
            return true;
        }
        
        // 待发货 -> 已取消（商家取消）
        if (fromStatus.equals(PENDING_SHIPMENT.getCode()) && 
            toStatus.equals(CANCELLED.getCode())) {
            return true;
        }
        
        // 已发货 -> 已送达（配送员送达后）
        if (fromStatus.equals(SHIPPED.getCode()) && 
            toStatus.equals(DELIVERED.getCode())) {
            return true;
        }
        
        // 已送达 -> 已完成（用户确认收货或系统自动完成）
        if (fromStatus.equals(DELIVERED.getCode()) && 
            toStatus.equals(COMPLETED.getCode())) {
            return true;
        }
        
        // 已完成 -> 已退款（退货退款）
        if (fromStatus.equals(COMPLETED.getCode()) && 
            toStatus.equals(REFUNDED.getCode())) {
            return true;
        }
        
        return false;
    }
}