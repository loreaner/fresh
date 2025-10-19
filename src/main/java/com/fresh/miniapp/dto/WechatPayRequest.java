package com.fresh.miniapp.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

/**
 * 微信支付请求DTO
 */
@Data
public class WechatPayRequest {

    /**
     * 订单号
     */
    @NotBlank(message = "订单号不能为空")
    private String orderNo;

    /**
     * 用户openid
     */
    @NotBlank(message = "用户openid不能为空")
    private String openid;

    /**
     * 支付金额（分）
     */
    @NotNull(message = "支付金额不能为空")
    @Positive(message = "支付金额必须大于0")
    private Integer totalAmount;

    /**
     * 商品描述
     */
    @NotBlank(message = "商品描述不能为空")
    private String description;

    /**
     * 用户IP地址
     */
    private String clientIp;

    private Integer amount;
}