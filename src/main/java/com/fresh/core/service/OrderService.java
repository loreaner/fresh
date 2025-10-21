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
    /**
     * 分页查询用户订单列表
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @param userId 用户ID
     * @param status 订单状态（可选）
     * @return 分页数据
     */
    Page<Order> getOrderPage(Integer pageNum, Integer pageSize, Long userId, Integer status);
    
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
     boolean createOrder(OrderCreateRequest order);

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
}