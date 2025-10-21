package com.fresh.core.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("point_records")
public class PointRecord extends BaseEntity {
    private Long userId;
    private Long orderId;
    private Integer type; // 1-获得，2-使用，3-过期，4-退还
    private Integer points;
    private String source; // order-订单，sign-签到，activity-活动，manual-手动调整
    private String description;
    private LocalDateTime expireTime;
}