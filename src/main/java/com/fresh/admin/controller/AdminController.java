package com.fresh.admin.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fresh.common.response.Result;

import com.fresh.core.entity.Order;
import com.fresh.core.service.AdminService;
import com.fresh.core.service.OrderService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


/**
 * 管理员控制器
 */
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Resource
    private AdminService adminService;

    @Resource
    private OrderService orderService;

    /**
     * 分页获取订单
     */
    @GetMapping("/order/page")
    public Result<Page<Order>> getOrderPage(@RequestParam(defaultValue = "1") Integer pageNum,
                                            @RequestParam(defaultValue = "10") Integer pageSize,
                                            @RequestParam(required = false) String phone,
                                            @RequestParam(required = false) Integer status) {

        return Result.success(null);
    }

    /**
     * 管理员登录
     */
    @PostMapping("/login")
    public Result login(@RequestParam String username,@RequestParam String password) {
        try {
            Boolean loginVO = adminService.login(username, password);
            return Result.success(loginVO);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 管理员登出
     */
    @PostMapping("/logout")
    public Result<Void> logout(@RequestHeader(value = "Authorization", required = false) String token) {
        try {
            adminService.logout(token);
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}