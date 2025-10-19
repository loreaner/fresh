package com.fresh.miniapp.dto;

import lombok.Data;

@Data
public class DecryptPhoneRequest {
    private String encryptedData;
    private String iv;
    private String sessionKey;
}