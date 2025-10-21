package com.fresh.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fresh.core.entity.UserPoints;
import com.fresh.core.entity.PointRecord;
import com.fresh.core.mapper.UserPointsMapper;
import com.fresh.core.mapper.PointRecordMapper;
import com.fresh.core.service.UserPointsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * 用户积分服务实现类
 */
@Service
public class UserPointsServiceImpl extends ServiceImpl<UserPointsMapper, UserPoints> implements UserPointsService {

    @Resource
    private PointRecordMapper pointRecordMapper;

    @Override
    public UserPoints getUserPoints(String phone) {
        LambdaQueryWrapper<UserPoints> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserPoints::getPhone, phone);
        UserPoints userPoints = getOne(wrapper);
        
        // 如果用户积分记录不存在，则初始化
        if (userPoints == null) {
            initUserPoints(phone);
            userPoints = getOne(wrapper);
        }
        
        return userPoints;
    }

    @Override
    @Transactional
    public boolean initUserPoints(String phone) {
        // 检查是否已存在
        LambdaQueryWrapper<UserPoints> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserPoints::getPhone, phone);
        UserPoints existing = getOne(wrapper);
        
        if (existing != null) {
            return true;
        }
        
        // 创建初始积分记录
        UserPoints userPoints = new UserPoints();
        userPoints.setPhone(phone);
        userPoints.setTotalPoints(0);
        userPoints.setAvailablePoints(0);
        userPoints.setUsedPoints(0);
        userPoints.setExpiredPoints(0);
        userPoints.setCreateTime(LocalDateTime.now());
        userPoints.setUpdateTime(LocalDateTime.now());
        
        return save(userPoints);
    }

    @Override
    @Transactional
    public boolean addPoints(String phone, Integer points, String source, String description) {
        if (points <= 0) {
            return false;
        }
        
        // 获取或初始化用户积分
        UserPoints userPoints = getUserPoints(phone);
        if (userPoints == null) {
            return false;
        }
        
        // 更新积分
        userPoints.setTotalPoints(userPoints.getTotalPoints() + points);
        userPoints.setAvailablePoints(userPoints.getAvailablePoints() + points);
        userPoints.setUpdateTime(LocalDateTime.now());
        
        boolean updateSuccess = updateById(userPoints);
        return updateSuccess;
    }

    @Override
    public boolean deductPoints(String phone, Integer points, String description) {
        return false;
    }


    @Override
    public boolean hasEnoughPoints(String phone, Integer points) {
        UserPoints userPoints = getUserPoints(phone);
        return userPoints != null && userPoints.getAvailablePoints() >= points;
    }
}