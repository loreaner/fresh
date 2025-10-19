package com.fresh.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fresh.core.entity.Admin;
import com.fresh.core.mapper.AdminMapper;
import com.fresh.core.service.AdminService;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;

/**
 * 管理员服务实现类
 */
@Service
public class AdminServiceImpl implements AdminService {

    @Resource
    private AdminMapper adminMapper;

    @Override
    public Boolean login(String username, String password) {
        // 查询管理员
        Admin admin = adminMapper.selectByUsername(username,password);
        
        // 如果管理员不存在，返回false
        if (admin == null) {
            return false;
        }
        
        // 验证密码（MD5加密）

        return true;
    }

    @Override
    public void logout(String token) {
        // 简化版登出，实际项目中应该清除token缓存
        // 这里可以添加token失效逻辑
    }
}