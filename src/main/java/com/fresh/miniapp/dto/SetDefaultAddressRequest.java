package com.fresh.miniapp.dto;

import lombok.Data;

@Data
public class SetDefaultAddressRequest {
    private Long userId;
    private Long addressId;
}
