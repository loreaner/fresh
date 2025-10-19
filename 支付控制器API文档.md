# 支付控制器API文档

## 概述

本文档详细描述了生鲜配送系统中三个支付控制器的API接口，包括微信支付控制器、支付测试控制器和简化版支付控制器。

---

## 1. 微信支付控制器 (WechatPayController)

### 基础信息
- **控制器路径**: `/wechat/pay`
- **功能**: 完整的微信支付功能，包括创建订单、查询、退款等

### 1.1 创建支付订单

**接口地址**: `POST /wechat/pay/create`

**请求体 (WechatPayRequest)**:
```json
{
  "orderNo": "20240101120000001",        // 订单号，必填，不能为空
  "openid": "oABC123456789",             // 用户微信openid，必填，不能为空
  "totalAmount": 5000,                   // 支付金额（分），必填，必须大于0
  "description": "生鲜配送订单",          // 商品描述，必填，不能为空
  "clientIp": "192.168.1.100"            // 客户端IP，自动获取
}
```

**响应数据 (Result<WechatPayResponse>)**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "appId": "wx1234567890abcdef",       // 小程序APPID
    "timeStamp": "1640995200",           // 时间戳
    "nonceStr": "abc123def456",          // 随机字符串
    "packageValue": "prepay_id=wx123456789", // 订单详情扩展字符串
    "signType": "RSA",                   // 签名方式
    "paySign": "signature_string",       // 支付签名
    "prepayId": "wx123456789"            // 微信支付订单号
  }
}
```

**错误响应**:
```json
{
  "code": 500,
  "message": "创建支付订单失败：具体错误信息",
  "data": null
}
```

### 1.2 查询支付订单状态

**接口地址**: `GET /wechat/pay/query/{orderNo}`

**路径参数**:
- `orderNo`: 订单号

**响应数据 (Result<String>)**:
```json
{
  "code": 200,
  "message": "success",
  "data": "SUCCESS"  // 支付状态：SUCCESS-成功，NOTPAY-未支付，CLOSED-已关闭等
}
```

### 1.3 申请退款

**接口地址**: `POST /wechat/pay/refund`

**请求体 (WechatRefundRequest)**:
```json
{
  "orderNo": "20240101120000001",        // 原订单号，必填，不能为空
  "refundNo": "R20240101120000001",      // 退款单号，必填，不能为空
  "refundAmount": 5000,                  // 退款金额（分），必填，必须大于0
  "totalAmount": 5000,                   // 原订单金额（分），必填，必须大于0
  "reason": "用户申请退款"                // 退款原因，选填
}
```

**响应数据 (Result<Boolean>)**:
```json
{
  "code": 200,
  "message": "success",
  "data": true  // true-退款申请成功，false-退款申请失败
}
```

### 1.4 查询退款状态

**接口地址**: `GET /wechat/pay/refund/query/{refundNo}`

**路径参数**:
- `refundNo`: 退款单号

**响应数据 (Result<String>)**:
```json
{
  "code": 200,
  "message": "success",
  "data": "SUCCESS"  // 退款状态：SUCCESS-成功，PROCESSING-处理中，CLOSED-关闭等
}
```

### 1.5 关闭订单

**接口地址**: `POST /wechat/pay/close/{orderNo}`

**路径参数**:
- `orderNo`: 订单号

**响应数据 (Result<Boolean>)**:
```json
{
  "code": 200,
  "message": "success",
  "data": true  // true-关闭成功，false-关闭失败
}
```

### 1.6 支付结果通知（微信回调）

**接口地址**: `POST /wechat/pay/notify`

**说明**: 此接口由微信支付系统调用，用于通知支付结果

**响应数据**:
```json
{"code":"SUCCESS","message":"成功"}  // 成功响应
{"code":"FAIL","message":"失败"}     // 失败响应
```

### 1.7 退款结果通知（微信回调）

**接口地址**: `POST /wechat/pay/refund/notify`

**说明**: 此接口由微信支付系统调用，用于通知退款结果

**响应数据**:
```json
{"code":"SUCCESS","message":"成功"}  // 成功响应
{"code":"FAIL","message":"失败"}     // 失败响应
```

---

## 2. 支付测试控制器 (PaymentTestController)

### 基础信息
- **控制器路径**: `/test/payment`
- **功能**: 用于测试微信支付功能，依赖订单系统

### 2.1 测试创建支付

**接口地址**: `POST /test/payment/create`

**请求体**:
```json
{
  "orderNo": "20240101120000001",        // 订单号，必填
  "openid": "oABC123456789"              // 用户微信openid，必填
}
```

**响应数据**:
```json
{
  "success": true,
  "data": {
    "appId": "wx1234567890abcdef",
    "timeStamp": "1640995200",
    "nonceStr": "abc123def456",
    "packageValue": "prepay_id=wx123456789",
    "signType": "RSA",
    "paySign": "signature_string",
    "prepayId": "wx123456789"
  },
  "message": "支付订单创建成功"
}
```

**错误响应**:
```json
{
  "success": false,
  "message": "订单号和openid不能为空"  // 或其他错误信息
}
```

### 2.2 测试查询支付

**接口地址**: `GET /test/payment/query/{orderNo}`

**路径参数**:
- `orderNo`: 订单号

**响应数据**:
```json
{
  "success": true,
  "data": "SUCCESS",
  "message": "查询成功"
}
```

### 2.3 测试关闭订单

**接口地址**: `POST /test/payment/close/{orderNo}`

**路径参数**:
- `orderNo`: 订单号

**响应数据**:
```json
{
  "success": true,
  "data": true,
  "message": "订单关闭成功"
}
```

### 2.4 获取订单信息

**接口地址**: `GET /test/payment/order/{orderNo}`

**路径参数**:
- `orderNo`: 订单号

**响应数据**:
```json
{
  "success": true,
  "data": {
    "id": 1,
    "orderNo": "20240101120000001",
    "userId": 1,
    "totalAmount": 5000,
    "actualAmount": 5000,
    "status": 1,
    "paymentStatus": 0
  },
  "message": "获取订单信息成功"
}
```

---

## 3. 简化版支付控制器 (SimplePayController)

### 基础信息
- **控制器路径**: `/simple/pay`
- **功能**: 简化版支付接口，不依赖订单系统，直接传递价格创建支付

### 3.1 简化版创建支付订单

**接口地址**: `POST /simple/pay/create`

**请求体**:
```json
{
  "amount": 5000,                        // 支付金额（分），必填，必须大于0
  "description": "生鲜配送商品",          // 商品描述，选填，默认为"生鲜配送商品"
  "openid": "oABC123456789"              // 用户微信openid，必填，不能为空
}
```

**响应数据**:
```json
{
  "success": true,
  "orderNo": "SP1704067200001234",       // 系统自动生成的订单号
  "data": {
    "appId": "wx1234567890abcdef",
    "timeStamp": "1640995200",
    "nonceStr": "abc123def456",
    "packageValue": "prepay_id=wx123456789",
    "signType": "RSA",
    "paySign": "signature_string",
    "prepayId": "wx123456789"
  },
  "message": "支付订单创建成功"
}
```

**错误响应**:
```json
{
  "success": false,
  "message": "金额必须大于0"  // 或其他错误信息
}
```

### 3.2 简化版查询支付结果

**接口地址**: `GET /simple/pay/query/{orderNo}`

**路径参数**:
- `orderNo`: 订单号

**响应数据**:
```json
{
  "success": true,
  "data": "SUCCESS",
  "message": "查询成功"
}
```

### 3.3 简化版关闭订单

**接口地址**: `POST /simple/pay/close/{orderNo}`

**路径参数**:
- `orderNo`: 订单号

**响应数据**:
```json
{
  "success": true,
  "data": true,
  "message": "订单关闭成功"
}
```

---

## 通用说明

### 金额单位
- 所有金额均以"分"为单位，例如：1元 = 100分

### 订单号格式
- 微信支付控制器：使用业务系统的订单号
- 支付测试控制器：使用业务系统的订单号
- 简化版支付控制器：自动生成，格式为 `SP + 时间戳 + 4位随机数`

### 错误处理
- 所有接口都有统一的错误处理机制
- 错误信息会记录在日志中
- 返回给客户端的错误信息经过处理，不会暴露敏感信息

### 安全性
- 所有支付相关接口都有参数验证
- 微信支付回调接口会验证签名
- 支持IP白名单配置

### 环境配置
- 支持开发环境和生产环境配置
- 可通过配置文件控制是否启用微信支付功能
- 支持沙箱环境测试

---

## 使用建议

1. **开发测试阶段**：使用 `PaymentTestController` 或 `SimplePayController`
2. **生产环境**：使用 `WechatPayController`
3. **快速原型**：使用 `SimplePayController`，无需复杂的订单系统
4. **完整业务**：使用 `WechatPayController`，支持完整的支付流程

## 注意事项

1. 确保微信支付配置正确
2. 证书文件安全存放
3. 回调地址必须可以被微信服务器访问
4. 建议在支付成功后调用查询接口确认支付状态
5. 处理好支付回调的幂等性