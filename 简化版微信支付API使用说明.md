# 简化版微信支付API使用说明

## 概述

这是一个简化版的微信支付接口，不依赖订单系统，直接传递价格和商品描述即可创建支付订单。

## API接口

### 1. 创建支付订单

**接口地址：** `POST /api/simple/pay/create`

**请求参数：**
```json
{
  "amount": 5000,                    // 支付金额（分），必填
  "description": "生鲜配送商品",      // 商品描述，选填
  "openid": "user_openid_here"       // 用户微信openid，必填
}
```

**响应结果：**
```json
{
  "success": true,
  "orderNo": "SP1704067200001234",   // 生成的订单号
  "data": {
    "prepayId": "wx123456789",       // 预支付ID
    "timeStamp": "1640995200",       // 时间戳
    "nonceStr": "abc123",            // 随机字符串
    "package": "prepay_id=wx123456789", // 订单详情扩展字符串
    "signType": "RSA",               // 签名方式
    "paySign": "signature_here"      // 支付签名
  },
  "message": "支付订单创建成功"
}
```

### 2. 查询支付结果

**接口地址：** `GET /api/simple/pay/query/{orderNo}`

**响应结果：**
```json
{
  "success": true,
  "data": {
    "out_trade_no": "SP1704067200001234",  // 商户订单号
    "transaction_id": "wx123456789",       // 微信支付订单号
    "trade_state": "SUCCESS",              // 交易状态
    "trade_state_desc": "支付成功",        // 交易状态描述
    "amount": {
      "total": 5000,                       // 订单总金额（分）
      "payer_total": 5000                  // 用户支付金额（分）
    },
    "success_time": "2024-01-01T12:00:00+08:00"  // 支付完成时间
  },
  "message": "查询成功"
}
```

**交易状态说明：**
- `SUCCESS`：支付成功
- `REFUND`：转入退款
- `NOTPAY`：未支付
- `CLOSED`：已关闭
- `REVOKED`：已撤销（付款码支付）
- `USERPAYING`：用户支付中（付款码支付）
- `PAYERROR`：支付失败

### 3. 关闭订单

**接口地址：** `POST /api/simple/pay/close/{orderNo}`

**响应结果：**
```json
{
  "success": true,
  "message": "订单关闭成功"
}
```

## 前端集成示例

### 小程序端调用示例

```javascript
// 1. 创建支付订单
function createPayment() {
  wx.request({
    url: 'https://your-domain.com/api/simple/pay/create',
    method: 'POST',
    data: {
      amount: 5000,                    // 50元，单位：分
      description: '新鲜苹果 2kg',
      openid: 'user_openid_here'       // 从登录接口获取
    },
    success: function(res) {
      if (res.data.success) {
        // 调用微信支付
        const payData = res.data.data;
        wx.requestPayment({
          timeStamp: payData.timeStamp,
          nonceStr: payData.nonceStr,
          package: payData.package,
          signType: payData.signType,
          paySign: payData.paySign,
          success: function(payRes) {
            console.log('支付成功', payRes);
            // 支付成功后可以查询支付结果确认
            queryPaymentResult(res.data.orderNo);
          },
          fail: function(payRes) {
            console.log('支付失败', payRes);
          }
        });
      } else {
        wx.showToast({
          title: res.data.message,
          icon: 'none'
        });
      }
    }
  });
}

// 2. 查询支付结果
function queryPaymentResult(orderNo) {
  wx.request({
    url: `https://your-domain.com/api/simple/pay/query/${orderNo}`,
    method: 'GET',
    success: function(res) {
      if (res.data.success) {
        const payResult = res.data.data;
        if (payResult.trade_state === 'SUCCESS') {
          wx.showToast({
            title: '支付成功',
            icon: 'success'
          });
          // 跳转到成功页面或执行其他逻辑
        }
      }
    }
  });
}
```

### Vue/React前端示例

```javascript
// 创建支付订单
async function createPayment(amount, description, openid) {
  try {
    const response = await fetch('/api/simple/pay/create', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        amount: amount,
        description: description,
        openid: openid
      })
    });
    
    const result = await response.json();
    
    if (result.success) {
      console.log('支付订单创建成功', result);
      return result;
    } else {
      throw new Error(result.message);
    }
  } catch (error) {
    console.error('创建支付订单失败', error);
    throw error;
  }
}

// 查询支付结果
async function queryPayment(orderNo) {
  try {
    const response = await fetch(`/api/simple/pay/query/${orderNo}`);
    const result = await response.json();
    
    if (result.success) {
      return result.data;
    } else {
      throw new Error(result.message);
    }
  } catch (error) {
    console.error('查询支付结果失败', error);
    throw error;
  }
}
```

## 测试用例

### 使用Postman测试

1. **创建支付订单**
   ```
   POST http://localhost:8081/api/simple/pay/create
   Content-Type: application/json
   
   {
     "amount": 100,
     "description": "测试商品",
     "openid": "test_openid_123"
   }
   ```

2. **查询支付结果**
   ```
   GET http://localhost:8081/api/simple/pay/query/SP1704067200001234
   ```

3. **关闭订单**
   ```
   POST http://localhost:8081/api/simple/pay/close/SP1704067200001234
   ```

## 注意事项

1. **金额单位**：所有金额均以"分"为单位，例如1元 = 100分
2. **订单号格式**：系统自动生成，格式为 `SP + 时间戳 + 4位随机数`
3. **openid获取**：需要通过微信登录接口获取用户的openid
4. **支付结果确认**：建议在支付成功后调用查询接口确认支付状态
5. **错误处理**：请妥善处理接口返回的错误信息

## 优势

- **简单易用**：不需要复杂的订单系统，直接传递价格即可
- **快速集成**：最少的参数，最快的集成速度
- **灵活性高**：适合各种简单的支付场景
- **独立性强**：不依赖其他业务模块

这个简化版接口特别适合：
- 快速原型开发
- 简单的商品销售
- 测试和演示
- 不需要复杂订单管理的场景