package com.fresh.miniapp.dto;

import lombok.Data;

/**
 * 微信支付回调通知DTO
 */
@Data
public class WechatPayNotifyRequest {

    /**
     * 通知ID
     */
    private String id;

    /**
     * 通知创建时间
     */
    private String createTime;

    /**
     * 通知类型
     */
    private String eventType;

    /**
     * 通知数据类型
     */
    private String resourceType;

    /**
     * 通知资源数据
     */
    private Resource resource;

    /**
     * 回调摘要
     */
    private String summary;

    @Data
    public static class Resource {
        /**
         * 加密算法类型
         */
        private String algorithm;

        /**
         * 数据密文
         */
        private String ciphertext;

        /**
         * 附加数据
         */
        private String associatedData;

        /**
         * 原始类型
         */
        private String originalType;

        /**
         * 随机串
         */
        private String nonce;
    }
}