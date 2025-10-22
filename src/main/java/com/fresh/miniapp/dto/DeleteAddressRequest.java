package com.fresh.miniapp.dto;

import lombok.Data;

@Data
public class DeleteAddressRequest {
    private String phone;
    private String detailAddress;
}
