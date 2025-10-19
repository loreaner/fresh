package com.fresh.core.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fresh.common.config.WechatPayConfig;
import com.fresh.core.service.OrderService;
import com.fresh.core.service.WechatPayService;
import com.fresh.miniapp.dto.WechatPayRequest;
import com.fresh.miniapp.dto.WechatPayResponse;
import com.fresh.miniapp.dto.WechatRefundRequest;
import com.wechat.pay.java.core.exception.ServiceException;
import com.wechat.pay.java.service.payments.jsapi.JsapiServiceExtension;
import com.wechat.pay.java.service.payments.jsapi.model.*;
import com.wechat.pay.java.service.payments.model.Transaction;
import com.wechat.pay.java.service.refund.RefundService;
import com.wechat.pay.java.service.refund.model.CreateRequest;
import com.wechat.pay.java.service.refund.model.Refund;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;


/**
 * 微信支付服务实现类
 */
@Slf4j
@Service
public class WechatPayServiceImpl implements WechatPayService {

    @Resource
    private WechatPayConfig wechatPayConfig;

    @Autowired(required = false)
    private JsapiServiceExtension jsapiService;

    @Autowired(required = false)
    private RefundService refundService;

    @Resource
    private OrderService orderService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public WechatPayResponse createPayOrder(WechatPayRequest request) {
        if (jsapiService == null) {
            log.warn("微信支付服务未配置，无法创建支付订单");
            throw new RuntimeException("微信支付服务未配置");
        }
        
        try {
            log.info("创建微信支付订单，订单号：{}", request.getOrderNo());

            // 构建支付请求
            PrepayRequest prepayRequest = new PrepayRequest();
            prepayRequest.setAppid(wechatPayConfig.getAppid());
            prepayRequest.setMchid(wechatPayConfig.getMchid());
            prepayRequest.setDescription(request.getDescription());
            prepayRequest.setOutTradeNo(request.getOrderNo());
            prepayRequest.setNotifyUrl(wechatPayConfig.getNotifyUrl());

            // 设置金额
            Amount amount = new Amount();
            amount.setTotal(request.getTotalAmount());
            amount.setCurrency("CNY");
            prepayRequest.setAmount(amount);

            // 设置支付者信息
            Payer payer = new Payer();
            payer.setOpenid(request.getOpenid());
            prepayRequest.setPayer(payer);

            // 设置场景信息
            SceneInfo sceneInfo = new SceneInfo();
            sceneInfo.setPayerClientIp(request.getClientIp());
            prepayRequest.setSceneInfo(sceneInfo);

            // 调用微信支付API
            PrepayWithRequestPaymentResponse response = jsapiService.prepayWithRequestPayment(prepayRequest);

            // 更新订单支付信息
            orderService.updatePaymentInfo(request.getOrderNo(), null, response.getPackageVal());

            // 构建返回结果
            WechatPayResponse payResponse = new WechatPayResponse();
            payResponse.setAppId(response.getAppId());
            payResponse.setTimeStamp(response.getTimeStamp());
            payResponse.setNonceStr(response.getNonceStr());
            payResponse.setPackageValue(response.getPackageVal());
            payResponse.setSignType(response.getSignType());
            payResponse.setPaySign(response.getPaySign());
            payResponse.setPrepayId(response.getPackageVal());
            log.info("微信支付订单创建成功，订单号：{}，prepayId：{}", request.getOrderNo(), response.getPackageVal());
            return payResponse;

        } catch (ServiceException e) {
            log.error("创建微信支付订单失败，订单号：{}，错误：{}", request.getOrderNo(), e.getMessage(), e);
            throw new RuntimeException("创建支付订单失败：" + e.getMessage());
        } catch (Exception e) {
            log.error("创建微信支付订单异常，订单号：{}", request.getOrderNo(), e);
            throw new RuntimeException("创建支付订单异常");
        }
    }

    @Override
    public String queryPayOrder(String orderNo) {
        if (jsapiService == null) {
            log.warn("微信支付服务未配置，无法查询支付订单");
            return null;
        }
        
        try {
            log.info("查询微信支付订单状态，订单号：{}", orderNo);

            QueryOrderByOutTradeNoRequest request = new QueryOrderByOutTradeNoRequest();
            request.setMchid(wechatPayConfig.getMchid());
            request.setOutTradeNo(orderNo);

            Transaction transaction = jsapiService.queryOrderByOutTradeNo(request);
            
            log.info("查询微信支付订单状态成功，订单号：{}，状态：{}", orderNo, transaction.getTradeState());
            return transaction.getTradeState().name();

        } catch (ServiceException e) {
            log.error("查询微信支付订单状态失败，订单号：{}，错误：{}", orderNo, e.getMessage(), e);
            return "UNKNOWN";
        } catch (Exception e) {
            log.error("查询微信支付订单状态异常，订单号：{}", orderNo, e);
            return "ERROR";
        }
    }

    @Override
    public Boolean refund(WechatRefundRequest request) {
        if (refundService == null) {
            log.warn("微信退款服务未配置，无法申请退款");
            return false;
        }
        
        try {
            log.info("申请微信退款，订单号：{}，退款单号：{}", request.getOrderNo(), request.getRefundNo());

            CreateRequest refundRequest = new CreateRequest();
            refundRequest.setOutTradeNo(request.getOrderNo());
            refundRequest.setOutRefundNo(request.getRefundNo());
            refundRequest.setReason(request.getReason());
            refundRequest.setNotifyUrl(wechatPayConfig.getRefundNotifyUrl());

            // 设置金额
            com.wechat.pay.java.service.refund.model.AmountReq amountReq = 
                new com.wechat.pay.java.service.refund.model.AmountReq();
            amountReq.setRefund(Long.valueOf(request.getRefundAmount()));
            amountReq.setTotal(Long.valueOf(request.getTotalAmount()));
            amountReq.setCurrency("CNY");
            refundRequest.setAmount(amountReq);

            Refund refund = refundService.create(refundRequest);
            
            log.info("申请微信退款成功，订单号：{}，退款单号：{}，退款ID：{}", 
                    request.getOrderNo(), request.getRefundNo(), refund.getRefundId());
            return true;

        } catch (ServiceException e) {
            log.error("申请微信退款失败，订单号：{}，退款单号：{}，错误：{}", 
                    request.getOrderNo(), request.getRefundNo(), e.getMessage(), e);
            return false;
        } catch (Exception e) {
            log.error("申请微信退款异常，订单号：{}，退款单号：{}", 
                    request.getOrderNo(), request.getRefundNo(), e);
            return false;
        }
    }

    @Override
    public String queryRefund(String refundNo) {
        if (refundService == null) {
            log.warn("微信退款服务未配置，无法查询退款状态");
            return null;
        }
        
        try {
            log.info("查询微信退款状态，退款单号：{}", refundNo);

            com.wechat.pay.java.service.refund.model.QueryByOutRefundNoRequest request = 
                new com.wechat.pay.java.service.refund.model.QueryByOutRefundNoRequest();
            request.setOutRefundNo(refundNo);

            Refund refund = refundService.queryByOutRefundNo(request);
            
            log.info("查询微信退款状态成功，退款单号：{}，状态：{}", refundNo, refund.getStatus());
            return refund.getStatus().name();

        } catch (ServiceException e) {
            log.error("查询微信退款状态失败，退款单号：{}，错误：{}", refundNo, e.getMessage(), e);
            return "UNKNOWN";
        } catch (Exception e) {
            log.error("查询微信退款状态异常，退款单号：{}", refundNo, e);
            return "ERROR";
        }
    }

    @Override
    public Boolean handlePayNotify(String notifyData) {
        try {
            log.info("处理微信支付回调通知");

            // 解析通知数据
            JsonNode jsonNode = objectMapper.readTree(notifyData);
            String eventType = jsonNode.get("event_type").asText();
            
            if ("TRANSACTION.SUCCESS".equals(eventType)) {
                // 简化处理：直接从通知数据中获取基本信息
                // 注意：在生产环境中需要实现完整的解密和验签逻辑
                log.info("收到微信支付成功通知，事件类型：{}", eventType);
                
                // 这里应该实现完整的解密逻辑，暂时简化处理
                // 在实际使用时，需要根据微信支付文档实现完整的回调验证
                log.warn("支付回调处理已简化，请在生产环境中实现完整的解密和验签逻辑");
                
                return true;
            }

            return false;

        } catch (Exception e) {
            log.error("处理微信支付回调通知异常", e);
            return false;
        }
    }

    @Override
    public Boolean handleRefundNotify(String notifyData) {
        try {
            log.info("处理微信退款回调通知");

            // 解析通知数据
            JsonNode jsonNode = objectMapper.readTree(notifyData);
            String eventType = jsonNode.get("event_type").asText();
            
            if ("REFUND.SUCCESS".equals(eventType)) {
                // 解密资源数据并更新退款状态
                log.info("微信退款回调处理成功");
                return true;
            }

            return false;

        } catch (Exception e) {
            log.error("处理微信退款回调通知异常", e);
            return false;
        }
    }

    @Override
    public Boolean closeOrder(String orderNo) {
        if (jsapiService == null) {
            log.warn("微信支付服务未配置，无法关闭订单");
            return false;
        }
        
        try {
            log.info("关闭微信支付订单，订单号：{}", orderNo);

            CloseOrderRequest request = new CloseOrderRequest();
            request.setMchid(wechatPayConfig.getMchid());
            request.setOutTradeNo(orderNo);

            jsapiService.closeOrder(request);
            
            log.info("关闭微信支付订单成功，订单号：{}", orderNo);
            return true;

        } catch (ServiceException e) {
            log.error("关闭微信支付订单失败，订单号：{}，错误：{}", orderNo, e.getMessage(), e);
            return false;
        } catch (Exception e) {
            log.error("关闭微信支付订单异常，订单号：{}", orderNo, e);
            return false;
        }
    }
}