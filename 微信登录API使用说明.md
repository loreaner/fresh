# 微信小程序登录API使用说明

## 概述

本文档详细说明了如何在Uniapp微信小程序中使用我们的微信登录API，特别是如何通过code获取手机号码并完成用户登录。

## API接口列表

### 1. 完整微信登录 `/miniapp/user/wechat-login`

**接口地址**: `POST /api/miniapp/user/wechat-login`

**功能**: 支持通过登录code和手机号code一次性完成登录

**请求参数**:
```json
{
  "code": "登录凭证code",
  "phoneCode": "手机号授权code（可选）",
  "nickName": "用户昵称（可选）",
  "avatarUrl": "头像URL（可选）",
  "gender": 1,
  "city": "城市（可选）",
  "phone": "手机号（可选，如果提供phoneCode会自动获取）"
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "username": "13800138001",
    "phone": "13800138001",
    "nickName": "张三",
    "avatarUrl": "https://...",
    "gender": 1,
    "city": "北京"
  }
}
```

### 2. 分步骤微信登录 `/miniapp/user/wechat-phone-login`

**接口地址**: `POST /api/miniapp/user/wechat-phone-login`

**功能**: 分别提供登录code和手机号code进行登录

**请求参数**:
```json
{
  "loginCode": "登录凭证code",
  "phoneCode": "手机号授权code"
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "username": "13800138001",
    "phone": "13800138001",
    "nickName": "微信用户"
  }
}
```

### 3. 单独获取手机号 `/miniapp/user/getPhoneNumber`

**接口地址**: `POST /api/miniapp/user/getPhoneNumber`

**功能**: 仅获取手机号，不进行登录操作

**请求参数**:
```json
{
  "code": "手机号授权code"
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "phoneNumber": "13800138001",
    "purePhoneNumber": "13800138001",
    "countryCode": "86"
  }
}
```

## Uniapp前端实现示例

### 方式一：完整登录（推荐）

```javascript
// 在页面中实现微信登录
async function wechatLogin() {
  try {
    // 1. 获取登录code
    const loginRes = await uni.login({
      provider: 'weixin'
    });
    
    if (!loginRes[1].code) {
      throw new Error('获取登录code失败');
    }
    
    // 2. 获取用户信息（可选）
    const userInfoRes = await uni.getUserInfo({
      provider: 'weixin'
    });
    
    // 3. 获取手机号授权
    const phoneRes = await new Promise((resolve, reject) => {
      // 在button的@getphonenumber事件中调用
      // <button open-type="getPhoneNumber" @getphonenumber="getPhoneNumber">授权手机号</button>
      window.getPhoneNumberCallback = (e) => {
        if (e.detail.code) {
          resolve(e.detail.code);
        } else {
          reject(new Error('用户拒绝授权手机号'));
        }
      };
    });
    
    // 4. 调用登录API
    const response = await uni.request({
      url: 'http://localhost:8081/api/miniapp/user/wechat-login',
      method: 'POST',
      data: {
        code: loginRes[1].code,
        phoneCode: phoneRes,
        nickName: userInfoRes[1].userInfo.nickName,
        avatarUrl: userInfoRes[1].userInfo.avatarUrl,
        gender: userInfoRes[1].userInfo.gender,
        city: userInfoRes[1].userInfo.city
      }
    });
    
    if (response[1].data.code === 200) {
      // 登录成功，保存用户信息
      uni.setStorageSync('userInfo', response[1].data.data);
      console.log('登录成功', response[1].data.data);
    } else {
      throw new Error(response[1].data.message);
    }
    
  } catch (error) {
    console.error('登录失败', error);
    uni.showToast({
      title: '登录失败: ' + error.message,
      icon: 'none'
    });
  }
}

// 在template中的按钮
function getPhoneNumber(e) {
  if (window.getPhoneNumberCallback) {
    window.getPhoneNumberCallback(e);
  }
}
```

### 方式二：分步骤登录

```javascript
// 分步骤登录实现
async function stepByStepLogin() {
  try {
    // 1. 获取登录code
    const loginRes = await uni.login({
      provider: 'weixin'
    });
    
    // 2. 获取手机号code（需要用户点击授权按钮）
    const phoneCode = await getPhoneCode(); // 这个需要在button的事件中获取
    
    // 3. 调用分步骤登录API
    const response = await uni.request({
      url: 'http://localhost:8081/api/miniapp/user/wechat-phone-login',
      method: 'POST',
      data: {
        loginCode: loginRes[1].code,
        phoneCode: phoneCode
      }
    });
    
    if (response[1].data.code === 200) {
      uni.setStorageSync('userInfo', response[1].data.data);
      console.log('登录成功', response[1].data.data);
    }
    
  } catch (error) {
    console.error('登录失败', error);
  }
}
```

### 方式三：仅获取手机号

```javascript
// 仅获取手机号
async function getPhoneNumberOnly(phoneCode) {
  try {
    const response = await uni.request({
      url: 'http://localhost:8081/api/miniapp/user/getPhoneNumber',
      method: 'POST',
      data: {
        code: phoneCode
      }
    });
    
    if (response[1].data.code === 200) {
      const phoneNumber = response[1].data.data.phoneNumber;
      console.log('获取到手机号:', phoneNumber);
      return phoneNumber;
    }
    
  } catch (error) {
    console.error('获取手机号失败', error);
  }
}
```

## 前端页面模板示例

```vue
<template>
  <view class="login-container">
    <view class="login-form">
      <text class="title">微信登录</text>
      
      <!-- 方式一：完整登录 -->
      <button 
        class="login-btn" 
        open-type="getUserInfo" 
        @getuserinfo="getUserInfo"
        v-if="!hasUserInfo">
        获取用户信息
      </button>
      
      <button 
        class="phone-btn" 
        open-type="getPhoneNumber" 
        @getphonenumber="getPhoneNumber"
        v-if="hasUserInfo">
        授权手机号并登录
      </button>
      
      <!-- 方式二：分步骤登录 -->
      <button class="step-login-btn" @click="stepLogin">
        分步骤登录
      </button>
      
      <!-- 用户信息显示 -->
      <view v-if="userInfo" class="user-info">
        <text>用户ID: {{userInfo.id}}</text>
        <text>手机号: {{userInfo.phone}}</text>
        <text>昵称: {{userInfo.nickName}}</text>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      hasUserInfo: false,
      userInfo: null,
      loginCode: null,
      tempUserInfo: null
    }
  },
  
  methods: {
    // 获取用户信息
    async getUserInfo(e) {
      if (e.detail.userInfo) {
        this.tempUserInfo = e.detail.userInfo;
        this.hasUserInfo = true;
        
        // 同时获取登录code
        const loginRes = await uni.login({ provider: 'weixin' });
        this.loginCode = loginRes[1].code;
      }
    },
    
    // 获取手机号并完成登录
    async getPhoneNumber(e) {
      if (e.detail.code && this.loginCode) {
        try {
          const response = await uni.request({
            url: 'http://localhost:8081/api/miniapp/user/wechat-login',
            method: 'POST',
            data: {
              code: this.loginCode,
              phoneCode: e.detail.code,
              nickName: this.tempUserInfo.nickName,
              avatarUrl: this.tempUserInfo.avatarUrl,
              gender: this.tempUserInfo.gender,
              city: this.tempUserInfo.city
            }
          });
          
          if (response[1].data.code === 200) {
            this.userInfo = response[1].data.data;
            uni.setStorageSync('userInfo', this.userInfo);
            uni.showToast({
              title: '登录成功',
              icon: 'success'
            });
          }
        } catch (error) {
          uni.showToast({
            title: '登录失败',
            icon: 'none'
          });
        }
      }
    },
    
    // 分步骤登录
    async stepLogin() {
      // 这里需要实现分步骤的逻辑
      // 可以弹出模态框让用户分别授权
    }
  }
}
</script>
```

## 注意事项

1. **Code的有效期**: 微信的code只能使用一次，且有效期为5分钟
2. **手机号授权**: 手机号授权需要用户主动点击按钮，不能自动获取
3. **错误处理**: 请务必处理各种异常情况，如用户拒绝授权、网络错误等
4. **用户体验**: 建议使用方式一（完整登录），用户体验更好
5. **数据存储**: 登录成功后记得将用户信息存储到本地存储中

## 错误码说明

- `200`: 成功
- `400`: 参数错误
- `500`: 服务器内部错误

常见错误信息：
- "登录code不能为空"
- "手机号授权code不能为空"
- "获取手机号失败"
- "用户不存在"
- "注册失败"

## 测试建议

1. 在微信开发者工具中测试基本流程
2. 在真机上测试手机号授权功能
3. 测试各种异常情况的处理
4. 确保用户信息正确保存和读取