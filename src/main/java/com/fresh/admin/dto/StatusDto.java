package com.fresh.admin.dto;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class StatusDto {
    private Integer status;
    private String phone;
    private BigDecimal orderNo;
}
