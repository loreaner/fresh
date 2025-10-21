package com.fresh.core.service;

import com.fresh.core.entity.PointExchange;
import com.fresh.miniapp.dto.PointExchangeRequest;

import java.util.List;

/**
 * 积分兑换服务接口
 */
public interface PointExchangeService {
    
    /**
     * 创建积分兑换申请
     * @param request 兑换请求
     * @return 兑换记录
     */
    PointExchange createExchangeRequest(PointExchangeRequest request);
    
    /**
     * 根据用户ID查询兑换记录
     * @param userId 用户ID
     * @return 兑换记录列表
     */
    List<PointExchange> getExchangesByUserId(Long userId);
    
    /**
     * 根据ID查询兑换记录
     * @param id 兑换记录ID
     * @return 兑换记录
     */
    PointExchange getExchangeById(Long id);
    
    /**
     * 管理员处理兑换申请
     * @param id 兑换记录ID
     * @param status 处理状态 (1-通过, 2-拒绝)
     * @param adminRemark 管理员备注
     * @return 是否处理成功
     */
    boolean processExchange(Long id, Integer status, String adminRemark);
    
    /**
     * 查询待处理的兑换申请
     * @return 待处理的兑换记录列表
     */
    List<PointExchange> getPendingExchanges();
}