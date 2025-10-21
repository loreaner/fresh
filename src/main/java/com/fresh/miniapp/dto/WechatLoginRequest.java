package com.fresh.miniapp.dto;

import lombok.Data;

/**
 * 微信登录请求DTO
 */
@Data
public class WechatLoginRequest {
    private String code;           // 微信登录凭证
    private UserInfoDto userInfo;  // 用户信息

    // 为了兼容旧的扁平结构，保留这些字段
    private String nickName;       // 用户昵称
    private String avatarUrl;      // 头像URL
    private Integer gender;        // 性别：0-未知，1-男，2-女
    private String country;        // 国家
    private String province;       // 省份
    private String city;           // 城市
    private String phone;          // 手机号（可选）

    // 获取昵称的方法，优先从userInfo获取
    public String getNickName() {
        return userInfo != null ? userInfo.getNickName() : nickName;
    }

    // 获取头像URL的方法，优先从userInfo获取
    public String getAvatarUrl() {
        return userInfo != null ? userInfo.getAvatarUrl() : avatarUrl;
    }

    // 获取性别的方法，优先从userInfo获取
    public Integer getGender() {
        return userInfo != null ? userInfo.getGender() : gender;
    }

    // 获取国家的方法，优先从userInfo获取
    public String getCountry() {
        return userInfo != null ? userInfo.getCountry() : country;
    }

    // 获取省份的方法，优先从userInfo获取
    public String getProvince() {
        return userInfo != null ? userInfo.getProvince() : province;
    }

    // 获取城市的方法，优先从userInfo获取
    public String getCity() {
        return userInfo != null ? userInfo.getCity() : city;
    }

    // 获取手机号的方法，优先从userInfo获取
    public String getPhone() {
        return userInfo != null ? userInfo.getPhone() : phone;
    }
}