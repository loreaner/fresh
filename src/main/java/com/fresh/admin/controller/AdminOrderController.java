package com.fresh.admin.controller;

import com.fresh.admin.dto.AdminOrderDetailDto;
import com.fresh.admin.dto.StatusDto;
import com.fresh.common.response.Result;
import com.fresh.core.service.OrderService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import java.util.List;

// ... existing imports

@RestController
@RequestMapping("/admin/order")
public class AdminOrderController {
    @Resource
    private OrderService orderService;
    @GetMapping("/detail")
    public Result<AdminOrderDetailDto> getOrderDetail(@RequestParam String orderNo) {
        AdminOrderDetailDto orderDetail = orderService.getAdminOrderDetailByOrderNo(orderNo);
        return Result.success(orderDetail);
    }
    @GetMapping("/all")
    public Result<List<AdminOrderDetailDto>> getAllOrderDetails() {
        List<AdminOrderDetailDto> orderDetails = orderService.getAllAdminOrderDetails();
        return Result.success(orderDetails);
    }
    @GetMapping("/updateStatus")
    public Result<Boolean> updateOrderStatus(@RequestBody StatusDto statusDto) {
        boolean updated = orderService.updateStatusAfterPay(statusDto.getPhone(), statusDto.getOrderNo(), statusDto.getStatus());
        return Result.success(updated);
    }
}