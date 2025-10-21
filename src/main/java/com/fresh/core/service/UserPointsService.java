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
     * @return 是否成功
     */
    boolean initUserPoints(String phone);
    
    /**
     * 增加用户积分
     * @param points 积分数量
     * @param source 积分来源
     * @param description 描述
     * @return 是否成功
     */
    boolean addPoints(String phone , Integer points, String source, String description);
    
    /**
     * 扣减用户积分
     * @param points 积分数量
     * @param description 描述
     * @return 是否成功
     */
    boolean deductPoints(String phone, Integer points, String description);
    
    /**
     * 检查用户积分是否足够
     * @param points 需要的积分数量
     * @return 是否足够
     */
    boolean hasEnoughPoints(String phone, Integer points);
    
    /**
     * 更新用户积分信息
     * @param request 更新请求
     * @return 是否成功
     */
    boolean updateUserPoints(com.fresh.miniapp.dto.UserPointsUpdateRequest request);
}