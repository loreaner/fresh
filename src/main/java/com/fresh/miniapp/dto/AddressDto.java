package com.fresh.miniapp.dto;

import lombok.Data;

/**
 * 地址信息DTO
 */
@Data
public class AddressDto {
    private String receiverName;    // 收货人姓名
    private String receiverPhone;   // 收货人电话
    private String province;        // 省份
    private String city;           // 城市
    private String district;       // 区县
    private String detailAddress;  // 详细地址
    
    /**
     * 获取完整地址
     */
    public String getFullAddress() {
        return String.format("%s%s%s%s", 
            province != null ? province : "",
            city != null ? city : "",
            district != null ? district : "",
            detailAddress != null ? detailAddress : ""
        );
    }
}