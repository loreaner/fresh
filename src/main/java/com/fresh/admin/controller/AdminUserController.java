package com.fresh.admin.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fresh.common.response.PageResult;
import com.fresh.common.response.Result;
import com.fresh.core.entity.User;
import com.fresh.core.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/admin/user")
public class AdminUserController {

    @Resource
    private UserService userService;





    /**
     * 更新用户信息（接收JSON格式数据）
     */
    @PostMapping("/update")
    public Result<Void> updateUser(@RequestBody User user) {
        if (user.getId() == null) {
            return Result.error("ID不能为空");
        }
        
        boolean success = userService.updateUser(user);
        return success ? Result.success() : Result.error("更新失败");
    }

    /**
     * 删除用户（接收JSON格式数据）
     */
    @PostMapping("/delete")
    public Result<Void> deleteUser(@RequestBody User request) {
        if (request.getId() == null) {
            return Result.error("ID不能为空");
        }
        
        boolean success = userService.deleteUser(request.getId());
        return success ? Result.success() : Result.error("删除失败");
    }
    
    /**
     * 查询全部用户信息（接收JSON格式数据）
     */
    @PostMapping("/all")
    public Result<List<User>> getAllUsers() {
        List<User> users = userService.list();
        return Result.success(users);
    }
}