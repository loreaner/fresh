package com.fresh.test;

import com.fresh.miniapp.dto.CartCheckoutRequest;
import com.fresh.miniapp.dto.OrderItemRequest;
import com.fresh.core.service.OrderService;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 购物车结算测试类
 * 演示如何使用 CartCheckoutRequest 生成订单
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class CartCheckoutTest {

    @Resource
    private OrderService orderService;

    /**
     * 测试购物车结算生成订单
     */
    @Test
    public void testCartCheckout() {
        // 创建购物车结算请求
        CartCheckoutRequest request = new CartCheckoutRequest();
        
        // 设置用户ID（根据数据库中的实际用户）
        request.setUserId(1L);
        
        // 设置收货地址ID（根据数据库中的实际地址）
        request.setAddressId(1L);
        
        // 创建商品列表
        List<OrderItemRequest> items = new ArrayList<>();
        
        // 添加商品1：红富士苹果，数量2
        OrderItemRequest item1 = new OrderItemRequest();
        item1.setProductId(1L);
        item1.setQuantity(2);
        items.add(item1);
        
        // 添加商品2：海南香蕉，数量3
        OrderItemRequest item2 = new OrderItemRequest();
        item2.setProductId(2L);
        item2.setQuantity(3);
        items.add(item2);
        
        // 添加商品3：有机生菜，数量1
        OrderItemRequest item3 = new OrderItemRequest();
        item3.setProductId(3L);
        item3.setQuantity(1);
        items.add(item3);
        
        request.setItems(items);
        
        // 设置订单备注
        request.setRemark("请尽快配送，谢谢！");
        
        try {
            // 调用服务创建订单
            Long orderId = orderService.createOrderFromCart(request);
            
            if (orderId != null) {
                System.out.println("订单创建成功！订单ID: " + orderId);
                
                // 可以进一步查询订单详情
                // Order order = orderService.getOrderDetail(orderId);
                // System.out.println("订单详情: " + order);
                
            } else {
                System.out.println("订单创建失败！");
            }
            
        } catch (Exception e) {
            System.out.println("订单创建异常: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 测试单个商品的购物车结算
     */
    @Test
    public void testSingleItemCheckout() {
        CartCheckoutRequest request = new CartCheckoutRequest();
        request.setUserId(2L);
        request.setAddressId(3L);
        
        List<OrderItemRequest> items = new ArrayList<>();
        OrderItemRequest item = new OrderItemRequest();
        item.setProductId(1L);  // 红富士苹果
        item.setQuantity(5);    // 数量5
        items.add(item);
        
        request.setItems(items);
        request.setRemark("单品测试订单");
        
        try {
            Long orderId = orderService.createOrderFromCart(request);
            System.out.println("单品订单创建结果: " + (orderId != null ? "成功，订单ID: " + orderId : "失败"));
        } catch (Exception e) {
            System.out.println("单品订单创建异常: " + e.getMessage());
        }
    }
    
    /**
     * 测试空购物车的情况
     */
    @Test
    public void testEmptyCartCheckout() {
        CartCheckoutRequest request = new CartCheckoutRequest();
        request.setUserId(1L);
        request.setAddressId(1L);
        request.setItems(new ArrayList<>());  // 空商品列表
        request.setRemark("空购物车测试");
        
        try {
            Long orderId = orderService.createOrderFromCart(request);
            System.out.println("空购物车订单创建结果: " + (orderId != null ? "成功" : "失败（预期结果）"));
        } catch (Exception e) {
            System.out.println("空购物车订单创建异常: " + e.getMessage());
        }
    }
}