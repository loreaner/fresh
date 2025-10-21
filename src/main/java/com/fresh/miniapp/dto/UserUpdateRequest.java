package com.fresh.miniapp.dto;

import lombok.Data;

@Data
public class UserUpdateRequest {
    private Long id;
    private String nickname;
    private String avatar;
    private String phone;
    private Integer gender;
}