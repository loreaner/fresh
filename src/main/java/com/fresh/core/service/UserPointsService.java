package com.fresh.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fresh.core.entity.UserPoints;

/**
 * 用户积分服务接口
 */
public interface UserPointsService extends IService<UserPoints> {
    
    /**
     * 获取用户积分信息
     * @return 用户积分信息
     */
    UserPoints getUserPoints(String phone);
    
    /**
     * 初始化用户积分
     * @param userId 用户ID
     * @return 是否成功
     */
    boolean initUserPoints(Long userId);
    
    /**
     * 增加用户积分
     * @param userId 用户ID
     * @param points 积分数量
     * @param source 积分来源
     * @param description 描述
     * @return 是否成功
     */
    boolean addPoints(Long userId, Integer points, String source, String description);
    
    /**
     * 扣减用户积分
     * @param userId 用户ID
     * @param points 积分数量
     * @param description 描述
     * @return 是否成功
     */
    boolean deductPoints(Long userId, Integer points, String description);
    
    /**
     * 检查用户积分是否足够
     * @param userId 用户ID
     * @param points 需要的积分数量
     * @return 是否足够
     */
    boolean hasEnoughPoints(Long userId, Integer points);
}