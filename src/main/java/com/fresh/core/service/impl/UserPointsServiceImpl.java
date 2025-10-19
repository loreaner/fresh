package com.fresh.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fresh.core.entity.User;
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

    @Resource
    private com.fresh.core.service.UserService userService;

    @Override
    public UserPoints getUserPoints(String phone) {
        User user = userService.getUserByPhone(phone);
        if (user == null) {
            return null; // 用户不存在，直接返回null
        }
        Long userId = user.getId();

        LambdaQueryWrapper<UserPoints> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserPoints::getUserId, userId);
        UserPoints userPoints = getOne(wrapper);

        // 如果用户积分记录不存在，则初始化
        if (userPoints == null) {
            initUserPoints(userId);
            userPoints = getOne(wrapper);
        }

        return userPoints;
    }

    @Override
    @Transactional
    public boolean initUserPoints(Long userId) {
        // 检查是否已存在
        LambdaQueryWrapper<UserPoints> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserPoints::getUserId, userId);
        UserPoints existing = getOne(wrapper);
        
        if (existing != null) {
            return true;
        }
        
        // 创建初始积分记录
        UserPoints userPoints = new UserPoints();
        userPoints.setUserId(userId);
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
    public boolean addPoints(Long userId, Integer points, String source, String description) {
        if (points <= 0) {
            return false;
        }
        
        // 获取或初始化用户积分
        UserPoints userPoints = getUserPoints(String.valueOf(userId));
        if (userPoints == null) {
            return false;
        }
        
        // 更新积分
        userPoints.setTotalPoints(userPoints.getTotalPoints() + points);
        userPoints.setAvailablePoints(userPoints.getAvailablePoints() + points);
        userPoints.setUpdateTime(LocalDateTime.now());
        
        boolean updateSuccess = updateById(userPoints);
        
        if (updateSuccess) {
            // 记录积分变动
            PointRecord record = new PointRecord();
            record.setUserId(userId);
            record.setType(1); // 1-获得
            record.setPoints(points);
            record.setSource(source);
            record.setDescription(description);
            record.setCreateTime(LocalDateTime.now());
            
            pointRecordMapper.insert(record);
        }
        
        return updateSuccess;
    }

    @Override
    @Transactional
    public boolean deductPoints(Long userId, Integer points, String description) {
        if (points <= 0) {
            return false;
        }
        
        UserPoints userPoints = getUserPoints(String.valueOf(userId));
        if (userPoints == null || userPoints.getAvailablePoints() < points) {
            return false;
        }
        
        // 更新积分
        userPoints.setAvailablePoints(userPoints.getAvailablePoints() - points);
        userPoints.setUsedPoints(userPoints.getUsedPoints() + points);
        userPoints.setUpdateTime(LocalDateTime.now());
        
        boolean updateSuccess = updateById(userPoints);
        
        if (updateSuccess) {
            // 记录积分变动
            PointRecord record = new PointRecord();
            record.setUserId(userId);
            record.setType(2); // 2-使用
            record.setPoints(points);
            record.setSource("exchange");
            record.setDescription(description);
            record.setCreateTime(LocalDateTime.now());
            
            pointRecordMapper.insert(record);
        }
        
        return updateSuccess;
    }

    @Override
    public boolean hasEnoughPoints(Long userId, Integer points) {
        UserPoints userPoints = getUserPoints(String.valueOf(userId));
        return userPoints != null && userPoints.getAvailablePoints() >= points;
    }
}