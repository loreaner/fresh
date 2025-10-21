package com.fresh.common.enums;

import lombok.Getter;

@Getter
public enum OrderStatus {
    PENDING_PAYMENT(1, "待付款"),
    PENDING_DELIVERY(2, "待收货"),
    PENDING_REVIEW(3, "待评价"),
    COMPLETED(4, "已完成");

    private final Integer code;
    private final String desc;

    OrderStatus(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
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

    public static String getDescByCode(Integer code) {
        OrderStatus status = getByCode(code);
        return status == null ? "未知状态" : status.getDesc();
    }
}