package com.fresh.core.service.impl;

import com.fresh.core.entity.PointExchange;
import com.fresh.miniapp.dto.PointExchangeRequest;
import com.fresh.core.mapper.PointExchangeMapper;
import com.fresh.core.service.PointExchangeService;
import com.fresh.core.service.UserPointsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 积分兑换服务实现类
 */
@Service
public class PointExchangeServiceImpl implements PointExchangeService {
    
    @Autowired
    private PointExchangeMapper pointExchangeMapper;
    
    @Autowired
    private UserPointsService userPointsService;
    
    @Override
    @Transactional
    public PointExchange createExchangeRequest(PointExchangeRequest request) {
        // 检查用户积分是否足够
        if (!userPointsService.hasEnoughPoints(request.getUserId(), request.getPoints())) {
            throw new RuntimeException("积分不足");
        }
        
        // 创建兑换记录
        PointExchange exchange = new PointExchange();
        exchange.setUserId(request.getUserId());
        exchange.setPoints(request.getPoints());
        exchange.setStatus(0); // 待处理状态
        exchange.setCreateTime(LocalDateTime.now());
        exchange.setUpdateTime(LocalDateTime.now());
        
        pointExchangeMapper.insert(exchange);
        return exchange;
    }
    
    @Override
    public List<PointExchange> getExchangesByUserId(Long userId) {
        return pointExchangeMapper.selectByUserId(userId);
    }
    
    @Override
    public PointExchange getExchangeById(Long id) {
        return pointExchangeMapper.selectById(id);
    }
    
    @Override
    @Transactional
    public boolean processExchange(Long id, Integer status, String adminRemark) {
        PointExchange exchange = pointExchangeMapper.selectById(id);
        if (exchange == null || exchange.getStatus() != 0) {
            return false; // 记录不存在或已处理
        }
        
        LocalDateTime processTime = LocalDateTime.now();
        int result = pointExchangeMapper.updateStatus(id, status, adminRemark, processTime);
        
        // 如果审核通过，扣除用户积分
        if (status == 1 && result > 0) {
            userPointsService.deductPoints(exchange.getUserId(), exchange.getPoints(), 
                "积分兑换");
        }
        
        return result > 0;
    }
    
    @Override
    public List<PointExchange> getPendingExchanges() {
        return pointExchangeMapper.selectByStatus(0);
    }
}