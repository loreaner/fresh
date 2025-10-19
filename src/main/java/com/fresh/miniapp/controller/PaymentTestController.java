package com.fresh.miniapp.controller;

import com.fresh.core.entity.Order;
import com.fresh.core.service.OrderService;
import com.fresh.core.service.WechatPayService;
import com.fresh.miniapp.dto.WechatPayRequest;
import com.fresh.miniapp.dto.WechatPayResponse;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 支付测试控制器
 * 用于测试微信支付功能
 */
@RestController
@RequestMapping("/test/payment")
public class PaymentTestController {

    @Resource
    private WechatPayService wechatPayService;

    @Resource
    private OrderService orderService;

    /**
     * 测试创建支付订单
     */
    @PostMapping("/create")
    public Map<String, Object> testCreatePayment(@RequestBody Map<String, Object> request) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            String orderNo = (String) request.get("orderNo");
            String openid = (String) request.get("openid");
            
            if (orderNo == null || openid == null) {
                result.put("success", false);
                result.put("message", "订单号和openid不能为空");
                return result;
            }
            
            // 查询订单
            Order order = orderService.getOrderByOrderNo(orderNo);
            if (order == null) {
                result.put("success", false);
                result.put("message", "订单不存在");
                return result;
            }
            
            // 构建支付请求
            WechatPayRequest payRequest = new WechatPayRequest();
            payRequest.setOrderNo(orderNo);
            payRequest.setDescription("生鲜配送-" + orderNo);
            payRequest.setAmount(order.getActualAmount());
            payRequest.setOpenid(openid);
            
            // 创建支付订单
            WechatPayResponse payResponse = wechatPayService.createPayOrder(payRequest);
            
            result.put("success", true);
            result.put("data", payResponse);
            result.put("message", "支付订单创建成功");
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "创建支付订单失败：" + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * 测试查询支付订单
     */
    @GetMapping("/query/{orderNo}")
    public Map<String, Object> testQueryPayment(@PathVariable String orderNo) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            String payResult = wechatPayService.queryPayOrder(orderNo);
            
            result.put("success", true);
            result.put("data", payResult);
            result.put("message", "查询成功");
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "查询支付订单失败：" + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * 测试关闭订单
     */
    @PostMapping("/close/{orderNo}")
    public Map<String, Object> testCloseOrder(@PathVariable String orderNo) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            boolean success = wechatPayService.closeOrder(orderNo);
            
            result.put("success", success);
            result.put("message", success ? "订单关闭成功" : "订单关闭失败");
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "关闭订单失败：" + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * 获取订单信息
     */
    @GetMapping("/order/{orderNo}")
    public Map<String, Object> getOrderInfo(@PathVariable String orderNo) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            Order order = orderService.getOrderByOrderNo(orderNo);
            
            if (order == null) {
                result.put("success", false);
                result.put("message", "订单不存在");
            } else {
                result.put("success", true);
                result.put("data", order);
                result.put("message", "查询成功");
            }
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "查询订单失败：" + e.getMessage());
        }
        
        return result;
    }
}