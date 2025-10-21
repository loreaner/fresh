package com.fresh.core.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fresh.core.entity.Order;
import com.fresh.core.entity.OrderItem;
import com.fresh.core.enums.OrderStatus;
import com.fresh.core.mapper.OrderItemMapper;
import com.fresh.core.mapper.OrderMapper;
import com.fresh.core.service.AddressService;
import com.fresh.core.service.OrderService;
import com.fresh.core.service.ProductService;
import com.fresh.core.service.UserService; // 引入UserService
import com.fresh.miniapp.dto.Cart;
import com.fresh.miniapp.dto.OrderCreateRequest;
import com.fresh.miniapp.dto.CartCheckoutRequest;
import com.fresh.miniapp.dto.PayPrepareResponse;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fresh.admin.dto.AdminOrderDetailDto;
import com.fresh.admin.dto.ProductDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


@Slf4j
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Resource
    private ProductService productService;

    @Resource
    private AddressService addressService;

    

    @Resource
    private OrderItemMapper orderItemMapper;
    @Resource
    private UserService userService; // 注入UserService

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
        Order order = new Order();
        // 1. 设置订单号和状态
        order.setOrderNo(generateOrderNo());
        order.setStatus(OrderStatus.PENDING_PAYMENT.getCode()); // 待支付
        order.setReceiverAddress(request.getAddress()) ;
        order.setPhone(request.getPhone());
        order.setTotalPrice(request.getTotalPrice());

        // 2. 创建订单项
        List<OrderItem> orderItems = new ArrayList<>();

        for (Cart cartItem :request.getCarts()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setProductName(cartItem.getProduct().getName());
            orderItem.setProductImage(cartItem.getProduct().getImage());
            orderItem.setPrice(cartItem.getProduct().getPrice());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPhone(request.getPhone());
            orderItems.add(orderItem);
        }
        System.out.println(order);
        // 3. 保存订单
        boolean saved = save(order);
        if (!saved ) {
            throw new RuntimeException("订单创建失败");
        }
        // 4. 保存订单项
        for (OrderItem orderItem : orderItems) {
            orderItem.setOrderNo(order.getOrderNo());
            orderItemMapper.insert(orderItem);
            log.info("保存订单项：{}", orderItem);
        }


        return true;
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

    @Override
    public AdminOrderDetailDto getAdminOrderDetailByOrderNo(String orderNo) {
        Order order = getOne(new QueryWrapper<Order>().eq("order_no", orderNo));
        if (order == null) {
            return null;
        }

        List<OrderItem> orderItems = orderItemMapper.selectList(new QueryWrapper<OrderItem>().eq("order_no", order.getOrderNo()));

        AdminOrderDetailDto adminOrderDetailDto = new AdminOrderDetailDto();
        adminOrderDetailDto.setReceiver_name(order.getReceiver_name()); // Assuming receiver name is in address
        adminOrderDetailDto.setPhone(order.getPhone());
        adminOrderDetailDto.setAddress(order.getReceiverAddress());
        adminOrderDetailDto.setTotalPrice(order.getTotalPrice());

        List<ProductDto> productDtos = new ArrayList<>();
        for (OrderItem orderItem : orderItems) {
            ProductDto productDto = new ProductDto();
            productDto.setProductName(orderItem.getProductName());
            productDto.setPrice(orderItem.getPrice());
            productDto.setQuantity(orderItem.getQuantity());
            productDtos.add(productDto);
        }
        adminOrderDetailDto.setProducts(productDtos);

        return adminOrderDetailDto;
    }

    @Override
    public List<AdminOrderDetailDto> getAllAdminOrderDetails() {
        List<Order> orders = list();
        List<AdminOrderDetailDto> orderDetails = new ArrayList<>();
        for (Order order : orders) {
            AdminOrderDetailDto orderDetail = getAdminOrderDetailByOrderNo(order.getOrderNo());
            if (orderDetail != null) {
                orderDetails.add(orderDetail);
            }
        }
        return orderDetails;
    }
}