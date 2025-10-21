package com.fresh.core.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("users")
public class User extends BaseEntity {
    private String username;
    private String phone;
    private String avatar;
    private String nickName;      // 微信昵称
    private String avatarUrl;     // 微信头像URL
    private Integer gender;       // 性别：0-未知，1-男，2-女
    private String country;       // 国家
    private String province;      // 省份
    private String city;          // 城市
    private String wechatCode;    // 微信登录凭证
}