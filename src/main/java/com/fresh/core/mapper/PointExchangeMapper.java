package com.fresh.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fresh.core.entity.PointExchange;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 积分兑换Mapper接口
 */
@Mapper
public interface PointExchangeMapper extends BaseMapper<PointExchange> {
    
    /**
     * 根据用户ID查询兑换记录
     * @param userId 用户ID
     * @return 兑换记录列表
     */
    List<PointExchange> selectByUserId(@Param("userId") Long userId);
    
    /**
     * 根据状态查询兑换记录
     * @param status 状态
     * @return 兑换记录列表
     */
    List<PointExchange> selectByStatus(@Param("status") Integer status);
    
    /**
     * 更新兑换记录状态
     * @param id 兑换记录ID
     * @param status 新状态
     * @param adminRemark 管理员备注
     * @param processTime 处理时间
     * @return 更新行数
     */
    int updateStatus(@Param("id") Long id, 
                    @Param("status") Integer status, 
                    @Param("adminRemark") String adminRemark, 
                    @Param("processTime") java.time.LocalDateTime processTime);
}