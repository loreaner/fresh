package com.fresh.delivery.vo;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class DeliveryManVO {
    private Long id;
    private String username;
    private String phone;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}