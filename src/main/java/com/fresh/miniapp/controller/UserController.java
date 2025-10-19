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
import com.fresh.miniapp.vo.UserVO;
import com.fresh.miniapp.service.WechatMiniappService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
@Slf4j
/**
 * 用户控制器
 */
@RestController
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
     * 用于单独获取手机号，不进行登录操作
     */
    @PostMapping("/getPhoneNumber")
    public Result<java.util.Map<String, Object>> getPhoneNumber(@RequestBody PhoneCodeRequest request) {
        try {
            if (request == null || request.getCode() == null || request.getCode().isEmpty()) {
                log.warn("获取手机号失败：缺少code参数");
                return Result.error("缺少code参数");
            }
            
            log.info("开始通过code获取手机号，code: {}", request.getCode());
            java.util.Map<String, Object> phoneInfo = wechatMiniappService.getPhoneNumberByCode(request.getCode());
            
            if (phoneInfo != null && phoneInfo.containsKey("phoneNumber")) {
                log.info("成功获取手机号: {}", phoneInfo.get("phoneNumber"));
                return Result.success(phoneInfo);
            } else {
                log.warn("获取手机号失败：微信API返回数据异常");
                return Result.error("获取手机号失败：数据异常");
            }
        } catch (Exception e) {
            log.error("获取手机号失败", e);
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
    public Result<UserPoints> getUserPoints(@RequestParam String phone) {
        UserPoints userPoints = userPointsService.getUserPoints(phone);
        if (userPoints == null) {
            return Result.error("用户积分信息不存在");
        }
        return Result.success(userPoints);
    }

    /**
     * 用户积分兑换
     */
    @PostMapping("/points/exchange")
    public Result<Long> exchangePoints(@RequestBody PointExchangeRequest request) {
        try {
            // 验证用户积分是否足够
            UserPoints userPoints = userPointsService.getUserPoints(request.getCode());
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
     * 支持通过phoneCode自动获取手机号进行登录
     */
    @PostMapping("/wechat-login")
    public Result<UserVO> wechatLogin(@RequestBody WechatLoginRequest request) {
        try {
            // 1. 通过登录code换取openid与session_key
            java.util.Map<String, String> sessionInfo = wechatMiniappService.code2Session(request.getCode());
            String openid = sessionInfo.get("openid");
            log.info("获取到openid: {}", openid);

            // 2. 如果提供了phoneCode，则通过微信API获取手机号
            String phoneNumber = request.getPhone(); // 默认使用传入的手机号
            if (request.getPhoneCode() != null && !request.getPhoneCode().isEmpty()) {
                try {
                    java.util.Map<String, Object> phoneResult = wechatMiniappService.getPhoneNumberByCode(request.getPhoneCode());
                    if (phoneResult != null && phoneResult.containsKey("phoneNumber")) {
                        phoneNumber = (String) phoneResult.get("phoneNumber");
                        log.info("通过phoneCode获取到手机号: {}", phoneNumber);
                    }
                } catch (Exception e) {
                    log.warn("通过phoneCode获取手机号失败: {}", e.getMessage());
                    // 如果获取手机号失败，继续使用传入的手机号或openid进行登录
                }
            }

            User existingUser = null;
            
            // 3. 优先使用手机号查找用户
            if (phoneNumber != null && !phoneNumber.isEmpty()) {
                existingUser = userService.getUserByPhone(phoneNumber);
                log.info("通过手机号查找用户: {}", existingUser != null ? "找到" : "未找到");
            }

            // 4. 如果通过手机号未找到用户，则尝试使用openid查找
            if (existingUser == null) {
                existingUser = userService.getUserByWechatCode(openid);
                log.info("通过openid查找用户: {}", existingUser != null ? "找到" : "未找到");
            }
            
            if (existingUser != null) {
                // 5. 用户已存在，更新用户信息
                existingUser.setNickName(request.getNickName());
                existingUser.setAvatarUrl(request.getAvatarUrl());
                existingUser.setGender(request.getGender());
                existingUser.setCity(request.getCity());
                
                // 更新手机号（如果获取到了新的手机号）
                if (phoneNumber != null && !phoneNumber.isEmpty()) {
                    existingUser.setPhone(phoneNumber);
                }
                
                // 绑定微信openid（如果用户原先没有绑定）
                if (existingUser.getWechatCode() == null || existingUser.getWechatCode().isEmpty()) {
                    existingUser.setWechatCode(openid);
                }
                
                boolean updated = userService.updateUser(existingUser);
                if (updated) {
                    UserVO userVO = new UserVO();
                    BeanUtils.copyProperties(existingUser, userVO);
                    log.info("用户登录成功，用户ID: {}", existingUser.getId());
                    return Result.success(userVO);
                } else {
                    return Result.error("更新用户信息失败");
                }
            } else {
                // 6. 创建新用户
                User newUser = new User();
                newUser.setWechatCode(openid);
                newUser.setNickName(request.getNickName());
                newUser.setAvatarUrl(request.getAvatarUrl());
                newUser.setGender(request.getGender());
                newUser.setCity(request.getCity());
                newUser.setPhone(phoneNumber);
                
                // 如果没有手机号，使用openid作为username
                if (phoneNumber != null && !phoneNumber.isEmpty()) {
                    newUser.setUsername(phoneNumber);
                } else {
                    newUser.setUsername("wx_" + openid.substring(0, 8));
                }
                
                User createdUser = userService.createWechatUser(newUser);
                if (createdUser != null) {
                    UserVO userVO = new UserVO();
                    BeanUtils.copyProperties(createdUser, userVO);
                    log.info("新用户注册成功，用户ID: {}", createdUser.getId());
                    return Result.success(userVO);
                } else {
                    return Result.error("注册失败");
                }
            }
        } catch (Exception e) {
            log.error("微信登录失败", e);
            return Result.error("登录失败：" + e.getMessage());
        }
    }

    /**
     * 微信手机号登录（分步骤）
     * 第一步：通过登录code获取openid
     * 第二步：通过手机号code获取手机号并完成登录
     */
    @PostMapping("/wechat-phone-login")
    public Result<UserVO> wechatPhoneLogin(@RequestBody java.util.Map<String, String> request) {
        try {
            String loginCode = request.get("loginCode");
            String phoneCode = request.get("phoneCode");
            
            if (loginCode == null || loginCode.isEmpty()) {
                return Result.error("登录code不能为空");
            }
            
            if (phoneCode == null || phoneCode.isEmpty()) {
                return Result.error("手机号授权code不能为空");
            }
            
            // 1. 通过登录code获取openid
            java.util.Map<String, String> sessionInfo = wechatMiniappService.code2Session(loginCode);
            String openid = sessionInfo.get("openid");
            log.info("获取到openid: {}", openid);
            
            // 2. 通过手机号code获取手机号
            java.util.Map<String, Object> phoneResult = wechatMiniappService.getPhoneNumberByCode(phoneCode);
            if (phoneResult == null || !phoneResult.containsKey("phoneNumber")) {
                return Result.error("获取手机号失败");
            }
            
            String phoneNumber = (String) phoneResult.get("phoneNumber");
            log.info("获取到手机号: {}", phoneNumber);
            
            // 3. 查找或创建用户
            User existingUser = userService.getUserByPhone(phoneNumber);
            
            if (existingUser != null) {
                // 用户已存在，绑定微信openid
                if (existingUser.getWechatCode() == null || existingUser.getWechatCode().isEmpty()) {
                    existingUser.setWechatCode(openid);
                    userService.updateUser(existingUser);
                }
                
                UserVO userVO = new UserVO();
                BeanUtils.copyProperties(existingUser, userVO);
                log.info("用户登录成功，用户ID: {}", existingUser.getId());
                return Result.success(userVO);
            } else {
                // 创建新用户
                User newUser = new User();
                newUser.setWechatCode(openid);
                newUser.setPhone(phoneNumber);
                newUser.setUsername(phoneNumber);
                newUser.setNickName("微信用户");
                
                User createdUser = userService.createWechatUser(newUser);
                if (createdUser != null) {
                    UserVO userVO = new UserVO();
                    BeanUtils.copyProperties(createdUser, userVO);
                    log.info("新用户注册成功，用户ID: {}", createdUser.getId());
                    return Result.success(userVO);
                } else {
                    return Result.error("注册失败");
                }
            }
        } catch (Exception e) {
            log.error("微信手机号登录失败", e);
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
                request.getUserId(), 
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