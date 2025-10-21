package com.fresh.miniapp.controller;

import com.fresh.common.response.Result;
import com.fresh.core.entity.User;
import com.fresh.core.entity.UserPoints;
import com.fresh.core.entity.PointExchange;
import com.fresh.core.entity.PointRecord;
import com.fresh.core.mapper.PointRecordMapper;
import com.fresh.core.service.UserService;
import com.fresh.core.service.UserPointsService;
import com.fresh.core.service.PointExchangeService;
import com.fresh.miniapp.dto.UserUpdateRequest;
import com.fresh.miniapp.dto.WechatLoginRequest;
import com.fresh.miniapp.dto.PointExchangeRequest;
import com.fresh.miniapp.dto.AddPointsRequest;
import com.fresh.miniapp.dto.PhoneCodeRequest;
import com.fresh.miniapp.dto.DecryptPhoneRequest;
import com.fresh.miniapp.dto.UserPointsUpdateRequest;
import com.fresh.miniapp.vo.UserVO;
import com.fresh.miniapp.service.WechatMiniappService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户控制器
 */
@RestController
@Slf4j
@RequestMapping("/miniapp/user")
public class UserController {

    @Resource
    private UserService userService;
    
    @Resource
    private UserPointsService userPointsService;
    
    @Resource
    private PointExchangeService pointExchangeService;
    
    @Resource
    private PointRecordMapper pointRecordMapper;

    @Resource
    private WechatMiniappService wechatMiniappService;

    /**
     * 获取用户信息
     */
    @GetMapping("/info/{id}")
    public Result<UserVO> getUserInfo(@PathVariable Long id) {
        User user = userService.getUserDetail(id);
        if (user == null) {
            return Result.error("用户不存在");
        }
        
        UserVO vo = new UserVO();
        BeanUtils.copyProperties(user, vo);
        return Result.success(vo);
    }

    /**
     * 通过微信返回的code获取用户手机号（新接口）
     */
    @PostMapping("/getPhoneNumber")
    public Result<java.util.Map<String, Object>> getPhoneNumber(@RequestBody PhoneCodeRequest request) {
        try {
            if (request == null || request.getCode() == null || request.getCode().isEmpty()) {
                return Result.error("缺少code参数");
            }
            
            log.info("收到手机号获取请求，code: {}", request.getCode());
            java.util.Map<String, Object> phoneInfo = wechatMiniappService.getPhoneNumberByCode(request.getCode());
            log.info("手机号获取成功: {}", phoneInfo);
            return Result.success(phoneInfo);
        } catch (Exception e) {
            log.error("获取手机号失败: {}", e.getMessage(), e);
            return Result.error("获取手机号失败：" + e.getMessage());
        }
    }

    /**
     * 兼容旧版：通过encryptedData解密手机号
     */
    @PostMapping("/decrypt-phone")
    public Result<java.util.Map<String, Object>> decryptPhone(@RequestBody DecryptPhoneRequest request) {
        try {
            if (request == null || request.getEncryptedData() == null || request.getIv() == null || request.getSessionKey() == null) {
                return Result.error("参数不完整");
            }
            java.util.Map<String, Object> phoneInfo = wechatMiniappService.decryptPhoneNumber(
                    request.getEncryptedData(), request.getIv(), request.getSessionKey());
            return Result.success(phoneInfo);
        } catch (Exception e) {
            return Result.error("解密手机号失败：" + e.getMessage());
        }
    }

    /**
     * 更新用户信息
     */
    @PutMapping("/update")
    public Result<Void> updateUserInfo(@RequestBody UserUpdateRequest request) {
        User user = new User();
        BeanUtils.copyProperties(request, user);
        
        boolean success = userService.updateUser(user);
        return success ? Result.success() : Result.error("更新失败");
    }
    
    /**
     * 批量获取用户信息
     */
    @GetMapping("/batch")
    public Result<List<UserVO>> getUsersByIds(@RequestParam List<Long> ids) {
        List<User> users = userService.getUsersByIds(ids);
        if (users == null || users.isEmpty()) {
            return Result.success(new ArrayList<>());
        }
        
        List<UserVO> userVOS = new ArrayList<>();
        for (User user : users) {
            UserVO vo = new UserVO();
            BeanUtils.copyProperties(user, vo);
            userVOS.add(vo);
        }
        
        return Result.success(userVOS);
    }

    /**
     * 获取用户积分信息
     */
    @GetMapping("/points")
    public Result<UserPoints> getUserPoints(String phone) {
        UserPoints userPoints = userPointsService.getUserPoints(phone);
        if (userPoints == null) {
            return Result.error("用户积分信息不存在");
        }
        return Result.success(userPoints);
    }

    /**
     * 更新用户积分信息
     */
    @PutMapping("/points/update")
    public Result<Void> updateUserPoints(@RequestBody UserPointsUpdateRequest request) {
        boolean success = userPointsService.updateUserPoints(request);
        return success ? Result.success() : Result.error("更新失败");
    }

    /**
     * 用户积分兑换
     */
    @PostMapping("/points/exchange")
    public Result<Long> exchangePoints(@RequestBody PointExchangeRequest request) {
        try {
            // 验证用户积分是否足够
            UserPoints userPoints = userPointsService.getUserPoints(request.getPhone());
            if (userPoints == null || userPoints.getAvailablePoints() < request.getPoints()) {
                return Result.error("用户积分不足");
            }
            
            // 创建积分兑换记录，状态为待处理(0)
            PointExchange exchange = pointExchangeService.createExchangeRequest(request);
            if (exchange != null) {
                return Result.success(exchange.getId());
            } else {
                return Result.error("积分兑换申请失败");
            }
        } catch (Exception e) {
            return Result.error("积分兑换失败：" + e.getMessage());
        }
    }

    /**
     * 微信登录/注册
     */
    @PostMapping("/wechat-login")
    public Result<UserVO> wechatLogin(@RequestBody WechatLoginRequest request) {
        
        System.out.println("=== 微信登录请求开始 ===");
        System.out.println("接收到的请求参数:");
        System.out.println("code: " + request.getCode());
        System.out.println("userInfo: " + request.getUserInfo());
        System.out.println("nickName: " + request.getNickName());
        System.out.println("avatarUrl: " + request.getAvatarUrl());
        System.out.println("gender: " + request.getGender());
        System.out.println("=========================");
        
        try {
            String openid;
            
            // 开发模式：如果code是测试code，跳过微信验证
            if ("test-code".equals(request.getCode()) || request.getCode() == null || request.getCode().startsWith("test")) {
                // 使用测试openid，基于用户昵称生成唯一标识
                openid = "test_openid_" + (request.getNickName() != null ? request.getNickName().hashCode() : "default");
                System.out.println("开发模式：使用测试openid = " + openid);
            } else {
                // 生产模式：通过code换取openid与session_key
                java.util.Map<String, String> sessionInfo = wechatMiniappService.code2Session(request.getCode());
                openid = sessionInfo.get("openid");
                System.out.println("生产模式：获取到openid = " + openid);
            }
            // 检查是否已存在该微信用户（以openid为唯一标识）
            User existingUser = userService.getUserByWechatCode(openid);
            
            if (existingUser != null) {
                // 用户已存在，更新用户信息
                existingUser.setNickName(request.getNickName());
                existingUser.setAvatarUrl(request.getAvatarUrl());
                existingUser.setGender(request.getGender());

                existingUser.setCity(request.getCity());
                if (request.getPhone() != null && !request.getPhone().isEmpty()) {
                    existingUser.setPhone(request.getPhone());
                }
                
                boolean updated = userService.updateUser(existingUser);
                if (updated) {
                    UserVO userVO = new UserVO();
                    BeanUtils.copyProperties(existingUser, userVO);
                    return Result.success(userVO);
                } else {
                    return Result.error("更新用户信息失败");
                }
            } else {
                // 创建新用户
                User newUser = new User();
                newUser.setWechatCode(openid);
                newUser.setNickName(request.getNickName());
                newUser.setAvatarUrl(request.getAvatarUrl());
                newUser.setGender(request.getGender());
                newUser.setCity(request.getCity());
                newUser.setPhone(request.getPhone());
                System.out.println(request.getPhone()+"555");
                User createdUser = userService.createWechatUser(newUser);
                if (createdUser != null) {
                    UserVO userVO = new UserVO();
                    BeanUtils.copyProperties(createdUser, userVO);
                    return Result.success(userVO);
                } else {
                    return Result.error("注册失败");
                }
            }
        } catch (Exception e) {
            return Result.error("登录失败：" + e.getMessage());
        }
    }

    /**
     * 获取用户积分记录
     */
    @GetMapping("/points/records/{userId}")
    public Result<List<PointRecord>> getPointRecords(@PathVariable Long userId) {
        try {
            List<PointRecord> records = pointRecordMapper.selectByUserId(userId);
            return Result.success(records);
        } catch (Exception e) {
            return Result.error("获取积分记录失败：" + e.getMessage());
        }
    }

    /**
     * 手动增加用户积分（管理员功能）
     */
    @PostMapping("/points/add")
    public Result<Void> addUserPoints(@RequestBody AddPointsRequest request) {
        try {
            boolean success = userPointsService.addPoints(
               request.getPhone(),
                request.getPoints(), 
                request.getSource() != null ? request.getSource() : "manual",
                request.getDescription() != null ? request.getDescription() : "管理员手动添加"
            );
            
            if (success) {
                return Result.success();
            } else {
                return Result.error("添加积分失败");
            }
        } catch (Exception e) {
            return Result.error("添加积分失败：" + e.getMessage());
        }
    }
    /**
     * 获取用户兑换记录
     */
    @GetMapping("/points/exchange/records/{userId}")
    public Result<List<PointExchange>> getExchangeRecords(@PathVariable Long userId) {
        try {
            List<PointExchange> records = pointExchangeService.getExchangesByUserId(userId);
            return Result.success(records);
        } catch (Exception e) {
            return Result.error("获取兑换记录失败：" + e.getMessage());
        }
    }
}