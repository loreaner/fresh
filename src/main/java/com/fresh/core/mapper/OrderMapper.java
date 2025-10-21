package com.fresh.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fresh.core.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface OrderMapper extends BaseMapper<Order> {
    /**
     * 分页查询订单列表
     * @param page 分页参数
     * @param userId 用户ID
     * @param status 订单状态（可选）
     * @return 分页数据
     */
    IPage<Order> selectOrderPage(Page<Order> page, @Param("userId") Long userId, @Param("status") Integer status);
    
    /**
     * 查询订单详情（包含地址信息）
     * @param id 订单ID
     * @return 订单信息
     */
    Order selectOrderDetail(@Param("id") Long id);
}