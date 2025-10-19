package com.fresh.miniapp.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AddressVO {
    private Long id;
    private String receiverName;
    private String receiverPhone;
    private String province;
    private String city;
    private String district;
    private String detailAddress;
    private Boolean isDefault;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    // 格式化后的完整地址
    public String getFullAddress() {
        return String.format("%s%s%s%s", 
            province, 
            city, 
            district, 
            detailAddress
        );
    }
}