package com.fresh.miniapp.dto;

import lombok.Data;

/**
 * 用户信息DTO
 */
@Data
public class UserInfoDto {
    private String nickName;       // 用户昵称
    private String phone;          // 手机号
    private String avatarUrl;      // 头像URL
    private Integer gender;        // 性别：0-未知，1-男，2-女
    private String country;        // 国家
    private String province;       // 省份
    private String city;           // 城市
}