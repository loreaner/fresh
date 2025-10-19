package com.fresh.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fresh.core.entity.UserPoints;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 用户积分Mapper接口
 */
@Mapper
public interface UserPointsMapper extends BaseMapper<UserPoints> {
    
    /**
     * 根据用户ID获取积分信息
     * @param userId 用户ID
     * @return 用户积分信息
     */
    UserPoints selectByUserId(@Param("userId") Long userId);
    
    /**
     * 更新用户积分
     * @param userId 用户ID
     * @param totalPoints 总积分变化量
     * @param availablePoints 可用积分变化量
     * @param usedPoints 已使用积分变化量
     * @return 影响行数
     */
    int updateUserPoints(@Param("userId") Long userId, 
                        @Param("totalPoints") Integer totalPoints,
                        @Param("availablePoints") Integer availablePoints,
                        @Param("usedPoints") Integer usedPoints);
}