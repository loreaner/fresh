package com.fresh.miniapp.controller;

import com.fresh.core.service.WechatPayService;
import com.fresh.miniapp.dto.WechatPayRequest;
import com.fresh.miniapp.dto.WechatPayResponse;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 简化版支付控制器
 * 直接传递价格创建支付订单，不依赖订单系统
 */
@RestController
@RequestMapping("/simple/pay")
public class SimplePayController {

    @Resource
    private WechatPayService wechatPayService;

    /**
     * 简化版创建支付订单
     * 直接传递价格和描述信息
     */
    @PostMapping("/create")
    public Map<String, Object> createSimplePayment(@RequestBody Map<String, Object> request) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 获取请求参数
            Integer amount = (Integer) request.get("amount"); // 金额（分）
            String description = (String) request.get("description"); // 商品描述
            String openid = (String) request.get("openid"); // 用户openid
            // 参数校验
            if (amount == null || amount <= 0) {
                result.put("success", false);
                result.put("message", "金额必须大于0");
                return result;
            }
            
            if (openid == null || openid.trim().isEmpty()) {
                result.put("success", false);
                result.put("message", "用户openid不能为空");
                return result;
            }
            
            if (description == null || description.trim().isEmpty()) {
                description = "生鲜配送商品";
            }
            
            // 生成订单号（时间戳 + 随机数）
            String orderNo = generateOrderNo();
            
            // 构建支付请求
            WechatPayRequest payRequest = new WechatPayRequest();
            payRequest.setOrderNo(orderNo);
            payRequest.setDescription(description);
            payRequest.setAmount(amount);
            payRequest.setOpenid(openid);
            
            // 创建支付订单
            WechatPayResponse payResponse = wechatPayService.createPayOrder(payRequest);
            
            result.put("success", true);
            result.put("data", payResponse);
            result.put("orderNo", orderNo);
            result.put("message", "支付订单创建成功");
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "创建支付订单失败：" + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * 查询支付结果
     */
    @GetMapping("/query/{orderNo}")
    public Map<String, Object> queryPayment(@PathVariable String orderNo) {
        Map<String, Object> result = new HashMap<>();
        return result;
    }
    
    /**
     * 关闭订单
     */
    @PostMapping("/close/{orderNo}")
    public Map<String, Object> closeOrder(@PathVariable String orderNo) {
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
     * 生成订单号
     * 格式：SP + 时间戳 + 4位随机数
     */
    private String generateOrderNo() {
        long timestamp = System.currentTimeMillis();
        int random = (int) (Math.random() * 9000) + 1000; // 1000-9999
        return "SP" + timestamp + random;
    }
}