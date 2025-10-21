package com.fresh.core.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("delivery_men")
public class DeliveryMan extends BaseEntity {
    private String username;
    private String phone;
    private Integer status;
}