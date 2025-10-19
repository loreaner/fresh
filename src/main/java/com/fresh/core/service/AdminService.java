package com.fresh.core.service;



/**
 * 管理员服务接口
 */
public interface AdminService {
    
    /**
     * 管理员登录
     * @param username 用户名
     * @param password 密码
     * @return 登录信息
     */
   Boolean login(String username, String password);
    
    /**
     * 管理员登出
     * @param token 登录令牌
     */
    void logout(String token);
}