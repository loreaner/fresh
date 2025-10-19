package com.fresh.miniapp.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

/**
 * 微信退款请求DTO
 */
@Data
public class WechatRefundRequest {

    /**
     * 原订单号
     */
    @NotBlank(message = "原订单号不能为空")
    private String orderNo;

    /**
     * 退款单号
     */
    @NotBlank(message = "退款单号不能为空")
    private String refundNo;

    /**
     * 退款金额（分）
     */
    @NotNull(message = "退款金额不能为空")
    @Positive(message = "退款金额必须大于0")
    private Integer refundAmount;

    /**
     * 原订单金额（分）
     */
    @NotNull(message = "原订单金额不能为空")
    @Positive(message = "原订单金额必须大于0")
    private Integer totalAmount;

    /**
     * 退款原因
     */
    private String reason;
}