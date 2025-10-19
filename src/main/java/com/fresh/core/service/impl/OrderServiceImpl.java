package com.fresh.core.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fresh.core.entity.Address;
import com.fresh.core.entity.Order;
import com.fresh.core.entity.OrderItem;
import com.fresh.core.entity.Product;
import com.fresh.core.entity.User;
import com.fresh.core.enums.OrderStatus;
import com.fresh.core.mapper.OrderItemMapper;
import com.fresh.core.mapper.OrderMapper;
import com.fresh.core.service.AddressService;
import com.fresh.core.service.CartService;
import com.fresh.core.service.OrderService;
import com.fresh.core.service.ProductService;
import com.fresh.miniapp.dto.CartItemDto;
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

    @Resource
    private com.fresh.core.service.UserService userService;

    @Override
    public Page<Order> getOrderPage(Integer pageNum, Integer pageSize, Long userId, Integer status) {
        return null;
    }

    @Override
    public Page<Order> getOrderPage(Integer pageNum, Integer pageSize, String phone, Integer status) {
        User user = userService.getUserByPhone(phone);
        if (user == null) {
            return new Page<>(pageNum, pageSize); // 用户不存在，返回空列表
        }
        Long userId = user.getId();

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
    public Long createOrder(OrderCreateRequest request) {
        if (request == null || request.getUserId() == null || request.getAddressId() == null 
            || request.getProductId() == null || request.getQuantity() == null || request.getQuantity() <= 0) {
            return null;
        }

        // 校验地址
        Address address = addressService.getById(request.getAddressId());
        if (address == null || !request.getUserId().equals(address.getUserId())) {
            return null;
        }

        // 校验商品并计算金额
        Product product = productService.getById(request.getProductId());
        if (product == null || product.getStock() < request.getQuantity()) {
            throw new RuntimeException("商品库存不足");
        }

        
        // 准备订单项
        OrderItem orderItem = new OrderItem();
        orderItem.setProductId(product.getId());
        orderItem.setProductName(product.getName());
        orderItem.setProductImage(product.getImage());
        orderItem.setPrice(product.getPrice().intValue());
        orderItem.setQuantity(request.getQuantity());
        
        // 扣减库存
        product.setStock(product.getStock() - request.getQuantity());
        productService.updateById(product);
        // 创建订单
        Order order = new Order();
        order.setUserId(request.getUserId());
        order.setRemark(request.getRemark());
        order.setOrderNo(generateOrderNo());
        order.setStatus(OrderStatus.PENDING_PAYMENT.getCode());
        boolean saved = save(order);
        if (!saved || order.getId() == null) {
            throw new RuntimeException("订单创建失败");
        }

        // 保存订单项
        Long orderId = order.getId();
        orderItem.setOrderId(orderId);
        int rows = orderItemMapper.insert(orderItem);
        if (rows != 1) {
            throw new RuntimeException("订单项保存失败");
        }

        return orderId;
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createOrderFromCart(CartCheckoutRequest request) {
        if (request == null || request.getUserId() == null||request.getCart()==null||request.getRemark()==null) {
            return null;
        }

        // 校验商品并计算金额
        BigDecimal total = BigDecimal.ZERO;
        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItemDto itemReq : request.getCart()) {
            if (itemReq.getProduct() == null || itemReq.getQuantity() == null || itemReq.getQuantity() <= 0) {
                throw new RuntimeException("商品信息不完整");
            }
            
            Product product = productService.getById(itemReq.getProduct().getId());
            if (product == null || product.getStock() < itemReq.getQuantity()) {
                throw new RuntimeException("商品库存不足：" + (product != null ? product.getName() : "未知商品"));
            }


            // 准备订单项
            OrderItem item = new OrderItem();
            item.setProductId(product.getId());
            item.setProductName(product.getName());
            item.setProductImage(product.getImage());
            item.setPrice(product.getPrice().intValue());
            item.setQuantity(itemReq.getQuantity());
            orderItems.add(item);
            // 扣减库存
            product.setStock(product.getStock() - itemReq.getQuantity());
            productService.updateById(product);
        }

        // 创建订单
        Order order = new Order();
        order.setUserId(request.getUserId());
        order.setRemark(request.getRemark());
        order.setOrderNo(generateOrderNo());
        order.setStatus(OrderStatus.PENDING_PAYMENT.getCode());
        order.setPaymentStatus(0); // 未支付
        boolean saved = save(order);
        if (!saved || order.getId() == null) {
            throw new RuntimeException("订单创建失败");
        }

        // 保存订单项
        Long orderId = order.getId();
        orderItems.forEach(item -> item.setOrderId(orderId));
        int rows = orderItemMapper.insertBatch(orderItems);
        if (rows != orderItems.size()) {
            throw new RuntimeException("订单项保存失败");
        }

        return orderId;
    }

    @Override
    public Order getOrderByOrderNo(String orderNo) {
        return baseMapper.selectByOrderNo(orderNo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updatePaymentInfo(String orderNo, String wechatOrderNo, String prepayId) {
        Order order = getOrderByOrderNo(orderNo);
        if (order == null) {
            return false;
        }
        int result = baseMapper.updatePaymentInfo(order.getId(), wechatOrderNo, prepayId);
        return result > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean handlePaymentSuccess(String orderNo, String wechatOrderNo, String paymentTime) {
        Order order = getOrderByOrderNo(orderNo);
        if (order == null) {
            return false;
        }
        
        // 解析支付时间
        java.util.Date payTime = null;
        try {
            LocalDateTime localDateTime = LocalDateTime.parse(paymentTime.replace("T", " "), 
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            payTime = java.sql.Timestamp.valueOf(localDateTime);
        } catch (Exception e) {
            payTime = new java.util.Date();
        }

        int result = baseMapper.updatePaymentStatus(
                order.getId(), 
                1, // 已支付
                payTime, 
                wechatOrderNo,
                Integer.valueOf(OrderStatus.PENDING_DELIVERY.getCode()) // 待发货
        );
        return result > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean handlePaymentFailed(String orderNo) {
        Order order = getOrderByOrderNo(orderNo);
        if (order == null) {
            return false;
        }
        
        int result = baseMapper.updatePaymentStatus(
                order.getId(), 
                2, // 支付失败
                null, 
                null, 
                order.getStatus() // 保持原状态
        );
        return result > 0;
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