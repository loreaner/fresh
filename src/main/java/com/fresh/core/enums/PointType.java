package com.fresh.core.enums;

import lombok.Getter;

@Getter
public enum PointType {
    EARN(1, "获得"),
    USE(2, "使用"),
    EXPIRE(3, "过期"),
    REFUND(4, "退还");

    private final Integer code;
    private final String description;

    PointType(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    public static PointType getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (PointType type : PointType.values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }

    public static String getDescriptionByCode(Integer code) {
        PointType type = getByCode(code);
        return type == null ? "未知类型" : type.getDescription();
    }
}