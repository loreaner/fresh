package com.fresh.admin.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class OrderQueryRequest {
    private Long userId;
    private String orderNo;
    private Integer status;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}