package com.fresh.miniapp.controller;

import com.fresh.common.response.Result;
import com.fresh.core.service.WechatPayService;
import com.fresh.miniapp.dto.WechatPayRequest;
import com.fresh.miniapp.dto.WechatPayResponse;
import com.fresh.miniapp.dto.WechatRefundRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * 微信支付控制器
 */
@Slf4j
@RestController
@RequestMapping("/wechat/pay")
public class WechatPayController {

    @Resource
    private WechatPayService wechatPayService;

    /**
     * 创建支付订单
     */
    @PostMapping("/create")
    public Result<WechatPayResponse> createPayOrder(@Valid @RequestBody WechatPayRequest request,
                                                   HttpServletRequest httpRequest) {
        try {
            // 获取客户端IP
            String clientIp = getClientIp(httpRequest);
            request.setClientIp(clientIp);

            WechatPayResponse response = wechatPayService.createPayOrder(request);
            return Result.success(response);
        } catch (Exception e) {
            log.error("创建支付订单失败", e);
            return Result.error("创建支付订单失败：" + e.getMessage());
        }
    }

    /**
     * 查询支付订单状态
     */
    @GetMapping("/query/{orderNo}")
    public Result<String> queryPayOrder(@PathVariable String orderNo) {
        try {
            String status = wechatPayService.queryPayOrder(orderNo);
            return Result.success(status);
        } catch (Exception e) {
            log.error("查询支付订单状态失败，订单号：{}", orderNo, e);
            return Result.error("查询支付订单状态失败");
        }
    }

    /**
     * 申请退款
     */
    @PostMapping("/refund")
    public Result<Boolean> refund(@Valid @RequestBody WechatRefundRequest request) {
        try {
            Boolean result = wechatPayService.refund(request);
            return Result.success(result);
        } catch (Exception e) {
            log.error("申请退款失败", e);
            return Result.error("申请退款失败：" + e.getMessage());
        }
    }

    /**
     * 查询退款状态
     */
    @GetMapping("/refund/query/{refundNo}")
    public Result<String> queryRefund(@PathVariable String refundNo) {
        try {
            String status = wechatPayService.queryRefund(refundNo);
            return Result.success(status);
        } catch (Exception e) {
            log.error("查询退款状态失败，退款单号：{}", refundNo, e);
            return Result.error("查询退款状态失败");
        }
    }

    /**
     * 关闭订单
     */
    @PostMapping("/close/{orderNo}")
    public Result<Boolean> closeOrder(@PathVariable String orderNo) {
        try {
            Boolean result = wechatPayService.closeOrder(orderNo);
            return Result.success(result);
        } catch (Exception e) {
            log.error("关闭订单失败，订单号：{}", orderNo, e);
            return Result.error("关闭订单失败");
        }
    }

    /**
     * 微信支付结果通知
     */
    @PostMapping("/notify")
    public String payNotify(HttpServletRequest request) {
        try {
            String notifyData = getRequestBody(request);
            log.info("收到微信支付回调通知：{}", notifyData);

            Boolean result = wechatPayService.handlePayNotify(notifyData);
            
            if (result) {
                // 返回成功响应给微信
                return "{\"code\":\"SUCCESS\",\"message\":\"成功\"}";
            } else {
                // 返回失败响应给微信
                return "{\"code\":\"FAIL\",\"message\":\"失败\"}";
            }
        } catch (Exception e) {
            log.error("处理微信支付回调通知异常", e);
            return "{\"code\":\"FAIL\",\"message\":\"系统异常\"}";
        }
    }

    /**
     * 微信退款结果通知
     */
    @PostMapping("/refund/notify")
    public String refundNotify(HttpServletRequest request) {
        try {
            String notifyData = getRequestBody(request);
            log.info("收到微信退款回调通知：{}", notifyData);

            Boolean result = wechatPayService.handleRefundNotify(notifyData);
            
            if (result) {
                return "{\"code\":\"SUCCESS\",\"message\":\"成功\"}";
            } else {
                return "{\"code\":\"FAIL\",\"message\":\"失败\"}";
            }
        } catch (Exception e) {
            log.error("处理微信退款回调通知异常", e);
            return "{\"code\":\"FAIL\",\"message\":\"系统异常\"}";
        }
    }

    /**
     * 获取客户端IP地址
     */
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * 获取请求体内容
     */
    private String getRequestBody(HttpServletRequest request) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }
        return sb.toString();
    }
}