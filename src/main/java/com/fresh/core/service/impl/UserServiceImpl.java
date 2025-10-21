package com.fresh.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fresh.core.entity.User;
import com.fresh.core.mapper.UserMapper;
import com.fresh.core.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 用户服务实现类
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public Page<User> getUserPage(Integer pageNum, Integer pageSize, String keyword) {
        Page<User> page = new Page<>(pageNum, pageSize);
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.like("username", keyword)
                  .or()
                  .like("phone", keyword);
        }
        
        wrapper.orderByDesc("create_time");
        return page(page, wrapper);
    }

    @Override
    public User getUserDetail(Long id) {
        return getById(id);
    }

    @Override
    public boolean updateUser(User user) {
        return updateById(user);
    }

    @Override
    public boolean deleteUser(Long id) {
        return removeById(id);
    }
    
    @Override
    public List<User> getUsersByIds(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return null;
        }
        return listByIds(ids);
    }
    
    @Override
    public User createWechatUser(User user) {
        // 生成唯一的用户名（如果没有提供）
        if (user.getUsername() == null || user.getUsername().isEmpty()) {
            user.setUsername("wx_" + System.currentTimeMillis());
        }
        
        // 保存用户
        boolean success = save(user);
        return success ? user : null;
    }
    
    @Override
    public User getUserByWechatCode(String wechatCode) {
        if (wechatCode == null || wechatCode.isEmpty()) {
            return null;
        }
        
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("wechat_code", wechatCode);
        return getOne(wrapper);
    }

    @Override
    public User getUserByPhone(String phone) {
        if (phone == null || phone.isEmpty()) {
            return null;
        }
        
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("phone", phone);
        return getOne(wrapper);
    }
}