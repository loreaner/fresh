package com.fresh.miniapp.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fresh.common.response.PageResult;
import com.fresh.common.response.Result;
import com.fresh.core.entity.Order;
import com.fresh.core.entity.OrderItem;
import com.fresh.core.enums.OrderStatus;
import com.fresh.core.service.OrderService;
import com.fresh.miniapp.dto.OrderCreateRequest;
import com.fresh.miniapp.dto.CartCheckoutRequest;
import com.fresh.miniapp.dto.PayPrepareResponse;
import com.fresh.miniapp.vo.OrderVO;
import com.fresh.miniapp.vo.OrderItemVO;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/miniapp/order")
public class OrderController {

    @Resource
    private OrderService orderService;

    @GetMapping("/list")
    public Result<PageResult<OrderVO>> getOrderList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam Long userId,
            @RequestParam(required = false) Integer status) {
        
        Page<Order> page = orderService.getOrderPage(pageNum, pageSize, userId, status);
        
        List<OrderVO> voList = page.getRecords().stream().map(order -> {
            OrderVO vo = new OrderVO();
            BeanUtils.copyProperties(order, vo);
            vo.setStatusText(OrderStatus.getDescriptionByCode(order.getStatus()));
            
            // 获取订单项
            List<OrderItem> items = orderService.getOrderItems(order.getId());
            List<OrderItemVO> itemVOs = items.stream().map(item -> {
                OrderItemVO itemVO = new OrderItemVO();
                BeanUtils.copyProperties(item, itemVO);
                return itemVO;
            }).collect(Collectors.toList());
            vo.setItems(itemVOs);
            
            return vo;
        }).collect(Collectors.toList());
        
        return Result.success(PageResult.of(page.getTotal(), voList));
    }

    @GetMapping("/detail/{id}")
    public Result<OrderVO> getOrderDetail(@PathVariable Long id) {
        Order order = orderService.getOrderDetail(id);
        if (order == null) {
            return Result.error("订单不存在");
        }
        
        OrderVO vo = new OrderVO();
        BeanUtils.copyProperties(order, vo);
        vo.setStatusText(OrderStatus.getDescriptionByCode(order.getStatus()));
        
        // 获取订单项
        List<OrderItem> items = orderService.getOrderItems(order.getId());
        List<OrderItemVO> itemVOs = items.stream().map(item -> {
            OrderItemVO itemVO = new OrderItemVO();
            BeanUtils.copyProperties(item, itemVO);
            return itemVO;
        }).collect(Collectors.toList());
        vo.setItems(itemVOs);
        
        return Result.success(vo);
    }

    @PostMapping("/create")
    public Result<Long> createOrder(@RequestBody  OrderCreateRequest request) {
       boolean a = orderService.createOrder(request);
        if (a == false) {
            return Result.error("订单创建失败");
        }
        return Result.success();
    }

    /**
     * 购物车结算生成订单
     */
    @PostMapping("/checkout")
    public Result<Long> checkoutCart(@RequestBody CartCheckoutRequest request) {
        Long orderId = orderService.createOrderFromCart(request);
        if (orderId == null) {
            return Result.error("订单创建失败");
        }
        return Result.success(orderId);
    }

    @GetMapping("/prepare-pay/{orderId}")
    public Result<PayPrepareResponse> preparePay(@PathVariable Long orderId) {
        PayPrepareResponse response = orderService.preparePay(orderId);
        if (response == null) {
            return Result.error("订单不存在或状态不正确");
        }
        return Result.success(response);
    }

    @PostMapping("/paid/{orderId}")
    public Result<Void> orderPaid(@PathVariable Long orderId) {
        boolean success = orderService.updateStatusAfterPay(orderId);
        return success ? Result.success() : Result.error("更新失败");
    }

    @PostMapping("/confirm/{orderId}")
    public Result<Void> confirmReceived(@PathVariable Long orderId) {
        boolean success = orderService.updateOrderStatus(orderId, OrderStatus.COMPLETED.getCode());
        return success ? Result.success() : Result.error("确认收货失败");
    }
}