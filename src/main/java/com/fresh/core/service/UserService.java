package com.fresh.core.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fresh.core.entity.User;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * 用户服务接口
 */
public interface UserService extends IService<User> {
    Page<User> getUserPage(Integer pageNum, Integer pageSize, String keyword);
    User getUserDetail(Long id);
    boolean updateUser(User user);
    boolean deleteUser(Long id);
    
    /**
     * 批量查询用户信息
     * @param ids 用户ID列表
     * @return 用户列表
     */
    List<User> getUsersByIds(List<Long> ids);
    
    /**
     * 创建微信登录用户
     * @param user 用户信息
     * @return 创建的用户
     */
    User createWechatUser(User user);
    
    /**
     * 根据微信code查找用户
     * @param wechatCode 微信登录凭证
     * @return 用户信息
     */
    User getUserByWechatCode(String wechatCode);
}