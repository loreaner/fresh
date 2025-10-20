package com.fresh.common.config;

import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import com.wechat.pay.java.service.payments.jsapi.JsapiServiceExtension;
import com.wechat.pay.java.service.refund.RefundService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * 微信支付配置类
 */
@Slf4j
@Configuration
@ConfigurationProperties(prefix = "wechat.pay")
@Data
public class WechatPayConfig {

    /**
     * 小程序APPID
     */
    private String appid;

    /**
     * 商户号
     */
    private String mchid;

    /**
     * 商户私钥文件路径
     */
    private String privateKeyPath;

    /**
     * 商户证书序列号
     */
    private String merchantSerialNumber;

    /**
     * APIv3密钥
     */
    private String apiV3Key;

    /**
     * 支付结果通知地址
     */
    private String notifyUrl;

    /**
     * 退款结果通知地址
     */
    private String refundNotifyUrl;

    /**
     * 微信支付配置
     */
    @Bean
    @ConditionalOnProperty(name = "wechat.pay.enabled", havingValue = "true", matchIfMissing = false)
    public Config config() {
        try {
            // 验证必要的配置参数
            if (!isConfigValid()) {
                log.warn("微信支付配置不完整，跳过初始化");
                throw new IllegalStateException("微信支付配置不完整");
            }
            
            // 读取商户私钥
            String privateKey = getPrivateKey();
            
            // 使用自动更新证书的配置
            return new RSAAutoCertificateConfig.Builder()
                    .merchantId(mchid)
                    .privateKey(privateKey)
                    .merchantSerialNumber(merchantSerialNumber)
                    .apiV3Key(apiV3Key)
                    .build();
        } catch (Exception e) {
            log.error("微信支付配置初始化失败: {}", e.getMessage());
            throw new IllegalStateException("微信支付配置初始化失败: " + e.getMessage(), e);
        }
    }

    /**
     * JSAPI支付服务
     */
    @Bean
    @ConditionalOnProperty(name = "wechat.pay.enabled", havingValue = "true", matchIfMissing = false)
    public JsapiServiceExtension jsapiService(Config config) {
        if (config == null) {
            log.warn("微信支付配置为空，跳过JSAPI服务初始化");
            return null;
        }
        return new JsapiServiceExtension.Builder().config(config).build();
    }

    /**
     * 退款服务
     */
    @Bean
    @ConditionalOnProperty(name = "wechat.pay.enabled", havingValue = "true", matchIfMissing = false)
    public RefundService refundService(Config config) {
        if (config == null) {
            log.warn("微信支付配置为空，跳过退款服务初始化");
            return null;
        }
        return new RefundService.Builder().config(config).build();
    }

    /**
     * 验证配置是否完整
     */
    private boolean isConfigValid() {
        return StringUtils.hasText(appid) 
                && StringUtils.hasText(mchid) 
                && StringUtils.hasText(merchantSerialNumber) 
                && StringUtils.hasText(apiV3Key) 
                && StringUtils.hasText(privateKeyPath)
                && !appid.equals("your_app_id")
                && !mchid.equals("your_mch_id")
                && !merchantSerialNumber.equals("your_mch_serial_no")
                && !apiV3Key.equals("your_api_v3_key");
    }

    /**
     * 读取商户私钥
     */
    private String getPrivateKey() throws IOException {
        try {
            // 尝试从classpath读取
            ClassPathResource resource = new ClassPathResource(privateKeyPath.replace("classpath:", ""));
            return new String(Files.readAllBytes(Paths.get(resource.getURI())));
        } catch (Exception e) {
            // 如果classpath读取失败，尝试从文件系统读取
            return new String(Files.readAllBytes(Paths.get(privateKeyPath)));
        }
    }
}