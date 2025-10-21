package com.fresh.delivery.controller;

import com.fresh.common.response.Result;
import com.fresh.core.entity.Order;
import com.fresh.core.enums.OrderStatus;
import com.fresh.core.service.OrderService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/delivery/order")
public class DeliveryOrderController {

    @Resource
    private OrderService orderService;

    @PutMapping("/deliver/{id}")
    public Result<Void> deliverOrder(@PathVariable Long id) {
        Order order = orderService.getById(id);
        if (order == null) {
            return Result.error("订单不存在");
        }
        
        if (!OrderStatus.PENDING_DELIVERY.getCode().equals(order.getStatus())) {
            return Result.error("订单状态不正确");
        }
        
        boolean success = orderService.updateOrderStatus(id, 1);
        return success ? Result.success() : Result.error("更新失败");
    }

    @GetMapping("/detail/{id}")
    public Result<Order> getOrderDetail(@PathVariable Long id) {
        Order order = orderService.getOrderDetail(id);
        if (order == null) {
            return Result.error("订单不存在");
        }
        return Result.success(order);
    }
}