package com.fresh.core.service;

import com.fresh.miniapp.dto.WechatPayRequest;
import com.fresh.miniapp.dto.WechatPayResponse;
import com.fresh.miniapp.dto.WechatRefundRequest;

/**
 * 微信支付服务接口
 */
public interface WechatPayService {

    /**
     * 创建支付订单（统一下单）
     *
     * @param request 支付请求参数
     * @return 支付响应参数
     */
    WechatPayResponse createPayOrder(WechatPayRequest request);

    /**
     * 查询支付订单状态
     *
     * @param orderNo 订单号
     * @return 支付状态
     */
    String queryPayOrder(String orderNo);

    /**
     * 申请退款
     *
     * @param request 退款请求参数
     * @return 退款结果
     */
    Boolean refund(WechatRefundRequest request);

    /**
     * 查询退款状态
     *
     * @param refundNo 退款单号
     * @return 退款状态
     */
    String queryRefund(String refundNo);

    /**
     * 处理支付结果通知
     *
     * @param notifyData 通知数据
     * @return 处理结果
     */
    Boolean handlePayNotify(String notifyData);

    /**
     * 处理退款结果通知
     *
     * @param notifyData 通知数据
     * @return 处理结果
     */
    Boolean handleRefundNotify(String notifyData);

    /**
     * 关闭订单
     *
     * @param orderNo 订单号
     * @return 关闭结果
     */
    Boolean closeOrder(String orderNo);
}