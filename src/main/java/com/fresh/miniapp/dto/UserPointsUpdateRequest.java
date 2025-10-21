package com.fresh.miniapp.dto;

import lombok.Data;

@Data
public class UserPointsUpdateRequest {
    private String phone;
    private Integer totalPoints;
    private Integer availablePoints;
    private Integer usedPoints;
    private Integer expiredPoints;
}