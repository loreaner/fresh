package com.fresh.core.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fresh.core.entity.Order;
import com.fresh.core.entity.OrderItem;
import com.fresh.miniapp.dto.OrderCreateRequest;
import com.fresh.miniapp.dto.CartCheckoutRequest;
import com.fresh.miniapp.dto.PayPrepareResponse;

import java.util.List;

public interface OrderService extends IService<Order> {
    Page<Order> getOrderPage(Integer pageNum, Integer pageSize, Long userId, Integer status);

    /**
     * 分页查询用户订单列表
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @param userId 用户ID
     * @param status 订单状态（可选）
     * @return 分页数据
     */
    Page<Order> getOrderPage(Integer pageNum, Integer pageSize, String phone, Integer status);
    
    /**
     * 获取订单详情（包含地址信息）
     * @param id 订单ID
     * @return 订单信息
     */
    Order getOrderDetail(Long id);
    
    /**
     * 获取订单的所有订单项
     * @param orderId 订单ID
     * @return 订单项列表
     */
    List<OrderItem> getOrderItems(Long orderId);
    
    /**
     * 更新订单状态
     * @param id 订单ID
     * @param status 目标状态
     * @return 是否成功
     */
    boolean updateOrderStatus(Long id, Integer status);
    
    /**
     * 检查订单状态是否可以更新到目标状态
     * @param id 订单ID
     * @param targetStatus 目标状态
     * @return 是否可以更新
     */
    boolean canUpdateStatus(Long id, Integer targetStatus);

    /**
     * 创建订单
     * @param request 创建订单请求
     * @return 订单ID
     */
    Long createOrder(OrderCreateRequest request);

    /**
     * 从购物车创建订单
     * @param request 购物车结算请求
     * @return 订单ID
     */
    Long createOrderFromCart(CartCheckoutRequest request);
    
    /**
     * 准备支付
     * @param orderId 订单ID
     * @return 支付准备响应
     */
    PayPrepareResponse preparePay(Long orderId);
    
    /**
     * 支付成功后更新订单状态
     * @param orderId 订单ID
     * @return 是否成功
     */
    boolean updateStatusAfterPay(Long orderId);
    
    /**
     * 根据订单号查询订单
     * @param orderNo 订单号
     * @return 订单信息
     */
    Order getOrderByOrderNo(String orderNo);
    
    /**
     * 更新订单支付信息
     * @param orderNo 订单号
     * @param wechatOrderNo 微信订单号
     * @param prepayId 微信预支付ID
     * @return 是否成功
     */
    boolean updatePaymentInfo(String orderNo, String wechatOrderNo, String prepayId);
    
    /**
     * 支付成功回调处理
     * @param orderNo 订单号
     * @param wechatOrderNo 微信订单号
     * @param paymentTime 支付时间
     * @return 是否成功
     */
    boolean handlePaymentSuccess(String orderNo, String wechatOrderNo, String paymentTime);
    
    /**
     * 支付失败处理
     * @param orderNo 订单号
     * @return 是否成功
     */
    boolean handlePaymentFailed(String orderNo);
}