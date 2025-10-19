package com.fresh.miniapp.dto;

import lombok.Data;

/**
 * 微信支付响应DTO
 */
@Data
public class WechatPayResponse {

    /**
     * 小程序调起支付API时需要的参数
     */
    private String appId;

    /**
     * 时间戳
     */
    private String timeStamp;

    /**
     * 随机字符串
     */
    private String nonceStr;

    /**
     * 订单详情扩展字符串
     */
    private String packageValue;

    /**
     * 签名方式
     */
    private String signType;

    /**
     * 签名
     */
    private String paySign;

    /**
     * 微信支付订单号
     */
    private String prepayId;
}