package com.fresh.core.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fresh.core.entity.Address;
import com.fresh.core.entity.Order;
import com.fresh.core.entity.OrderItem;
import com.fresh.core.entity.Product;
import com.fresh.core.enums.OrderStatus;
import com.fresh.core.mapper.OrderItemMapper;
import com.fresh.core.mapper.OrderMapper;
import com.fresh.core.service.AddressService;
import com.fresh.core.service.CartService;
import com.fresh.core.service.OrderService;
import com.fresh.core.service.ProductService;
import com.fresh.miniapp.dto.Cart;
import com.fresh.miniapp.dto.OrderCreateRequest;
import com.fresh.miniapp.dto.CartCheckoutRequest;
import com.fresh.miniapp.dto.OrderItemRequest;
import com.fresh.miniapp.dto.PayPrepareResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Resource
    private ProductService productService;

    @Resource
    private AddressService addressService;

    @Resource
    private CartService cartService;

    @Resource
    private OrderItemMapper orderItemMapper;

    @Override
    public Page<Order> getOrderPage(Integer pageNum, Integer pageSize, Long userId, Integer status) {
        Page<Order> page = new Page<>(pageNum, pageSize);
        return (Page<Order>) baseMapper.selectOrderPage(page, userId, status);
    }

    @Override
    public Order getOrderDetail(Long id) {
        return baseMapper.selectOrderDetail(id);
    }

    @Override
    public List<OrderItem> getOrderItems(Long orderId) {
        return orderItemMapper.selectByOrderId(orderId);
    }

    @Override
    public boolean updateOrderStatus(Long id, Integer status) {
        if (!canUpdateStatus(id, status)) {
            return false;
        }
        Order order = new Order();
        order.setId(id);
        order.setStatus(status);
        order.setUpdateTime(LocalDateTime.now());
        return updateById(order);
    }

    @Override
    public boolean canUpdateStatus(Long id, Integer targetStatus) {
        Order order = getById(id);
        if (order == null) {
            return false;
        }
        return OrderStatus.canTransferTo(order.getStatus(), targetStatus);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createOrder(OrderCreateRequest request) {
        if (request == null || request.getPhone() == null || request.getAddress() == null
                || request.getCarts() == null || request.getCarts().isEmpty()) {
            return false;
        }



        // 创建订单
        Order order = new Order();
        order.setRemark(request.getRemark());
        order.setOrderNo(generateOrderNo());
        order.setTotalPrice(request.getTotalPrice());
        order.setCarts(request.getCarts());
        order.setPaymentTime(LocalDateTime.now());

        boolean saved = save(order);
        if (!saved || order.getId() == null) {
            throw new RuntimeException("订单创建失败");
        }


        return saved;
    }

    @Override
    public Long createOrderFromCart(CartCheckoutRequest request) {
        return null;
    }

    @Override
    public PayPrepareResponse preparePay(Long orderId) {
        Order order = getById(orderId);
        if (order == null || !OrderStatus.PENDING_PAYMENT.getCode().equals(order.getStatus())) {
            return null;
        }

        PayPrepareResponse resp = new PayPrepareResponse();
        resp.setOrderId(order.getId());
        resp.setOrderNo(order.getOrderNo());
        resp.setTotalPrice(order.getTotalPrice());
        return resp;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateStatusAfterPay(Long orderId) {
        Order order = getById(orderId);
        if (order == null) {
            return false;
        }
        
        if (!OrderStatus.canTransferTo(order.getStatus(), 1)) {
            return false;
        }
        
        order.setStatus(1);
        order.setUpdateTime(LocalDateTime.now());
        return updateById(order);
    }


    /**
     * 生成订单号
     * 格式：年月日时分秒 + 4位随机数
     */
    private String generateOrderNo() {
        LocalDateTime now = LocalDateTime.now();
        String timeStr = now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String randomStr = String.format("%04d", new Random().nextInt(10000));
        return timeStr + randomStr;
    }
}