package com.fresh.miniapp.dto;

import lombok.Data;
import java.io.Serializable;

/**
 * 积分兑换请求DTO
 */
@Data
public class PointExchangeRequest implements Serializable {
    private String phone;
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 兑换的积分数量
     */
    private Integer points;
}