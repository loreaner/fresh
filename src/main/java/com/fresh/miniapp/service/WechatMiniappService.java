package com.fresh.miniapp.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fresh.miniapp.config.WechatMiniappProperties;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * 微信小程序相关接口调用服务
 */
@Service
public class WechatMiniappService {

    @Resource
    private WechatMiniappProperties props;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private volatile String cachedAccessToken;
    private volatile long accessTokenExpireEpochMs;

    /**
     * 获取access_token，带简单内存缓存
     */
    public synchronized String getAccessToken() {
        long now = Instant.now().toEpochMilli();
        if (StringUtils.hasText(cachedAccessToken) && now < accessTokenExpireEpochMs - 60_000) {
            return cachedAccessToken;
        }

        String url = String.format(
                "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s",
                props.getAppid(), props.getSecret());
        String resp = restTemplate.getForObject(url, String.class);
        try {
            JsonNode node = objectMapper.readTree(resp);
            if (node.has("access_token")) {
                cachedAccessToken = node.get("access_token").asText();
                int expiresIn = node.get("expires_in").asInt(7200);
                accessTokenExpireEpochMs = now + expiresIn * 1000L;
                return cachedAccessToken;
            } else {
                throw new RuntimeException("获取access_token失败: " + resp);
            }
        } catch (Exception e) {
            throw new RuntimeException("解析access_token失败", e);
        }
    }

    /**
     * jscode2session：登录code换取openid与session_key
     */
    public Map<String, String> code2Session(String code) {
        String url = String.format(
                "https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code",
                props.getAppid(), props.getSecret(), code);
        String resp = restTemplate.getForObject(url, String.class);
        try {
            JsonNode node = objectMapper.readTree(resp);
            if (node.has("openid") && node.has("session_key")) {
                Map<String, String> result = new HashMap<>();
                result.put("openid", node.get("openid").asText());
                result.put("session_key", node.get("session_key").asText());
                return result;
            }
            throw new RuntimeException("code2session失败: " + node.path("errmsg").asText("未知错误"));
        } catch (Exception e) {
            throw new RuntimeException("解析code2session响应失败", e);
        }
    }

    /**
     * 根据微信提供的code换取手机号
     */
    public Map<String, Object> getPhoneNumberByCode(String code) {
        String token = getAccessToken();
        String url = "https://api.weixin.qq.com/wxa/business/getuserphonenumber?access_token=" + token;

        Map<String, String> body = new HashMap<>();
        body.put("code", code);

        String resp = restTemplate.postForObject(url, body, String.class);
        try {
            JsonNode node = objectMapper.readTree(resp);
            int errCode = node.path("errcode").asInt(-1);
            if (errCode == 0) {
                JsonNode info = node.path("phone_info");
                Map<String, Object> result = new HashMap<>();
                result.put("success", true);
                result.put("phoneNumber", info.path("phoneNumber").asText(""));
                result.put("purePhoneNumber", info.path("purePhoneNumber").asText(""));
                result.put("countryCode", info.path("countryCode").asText(""));
                return result;
            }
            throw new RuntimeException("获取手机号失败: " + node.path("errmsg").asText("未知错误"));
        } catch (Exception e) {
            throw new RuntimeException("解析手机号响应失败", e);
        }
    }

    /**
     * 旧版：使用encryptedData + iv + sessionKey解密手机号
     */
    public Map<String, Object> decryptPhoneNumber(String encryptedData, String iv, String sessionKey) {
        try {
            byte[] dataBytes = Base64.getDecoder().decode(encryptedData);
            byte[] ivBytes = Base64.getDecoder().decode(iv);
            byte[] keyBytes = Base64.getDecoder().decode(sessionKey);
            SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(ivBytes));
            byte[] resultBytes = cipher.doFinal(dataBytes);
            String json = new String(resultBytes, StandardCharsets.UTF_8);
            JsonNode node = objectMapper.readTree(json);
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("phoneNumber", node.path("phoneNumber").asText(""));
            result.put("purePhoneNumber", node.path("purePhoneNumber").asText(""));
            result.put("countryCode", node.path("countryCode").asText(""));
            return result;
        } catch (Exception e) {
            throw new RuntimeException("手机号解密失败", e);
        }
    }
}