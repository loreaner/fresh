package com.fresh.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fresh.core.entity.PointRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 积分记录Mapper接口
 */
@Mapper
public interface PointRecordMapper extends BaseMapper<PointRecord> {
    
    /**
     * 根据用户ID查询积分记录
     * @param userId 用户ID
     * @return 积分记录列表
     */
    List<PointRecord> selectByUserId(@Param("userId") Long userId);
    
    /**
     * 根据用户ID和类型查询积分记录
     * @param userId 用户ID
     * @param type 积分类型
     * @return 积分记录列表
     */
    List<PointRecord> selectByUserIdAndType(@Param("userId") Long userId, @Param("type") Integer type);
}