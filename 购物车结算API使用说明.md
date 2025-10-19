# 购物车结算API使用说明

## 概述

本文档详细说明如何使用购物车结算API来生成订单。购物车结算功能允许用户一次性购买多个商品，系统会自动计算总金额并创建订单。

## API接口信息

### 接口地址
```
POST /miniapp/order/checkout
```

### 请求头
```
Content-Type: application/json
```

### 请求体结构

#### CartCheckoutRequest 对象
| 字段名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| userId | Long | 是 | 用户ID |
| addressId | Long | 是 | 收货地址ID |
| items | List<OrderItemRequest> | 是 | 商品列表 |
| remark | String | 否 | 订单备注 |

#### OrderItemRequest 对象
| 字段名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| productId | Long | 是 | 商品ID |
| quantity | Integer | 是 | 购买数量 |

## 请求示例

### 1. 多商品购物车结算

```json
{
  "userId": 1,
  "addressId": 1,
  "items": [
    {
      "productId": 1,
      "quantity": 2
    },
    {
      "productId": 2,
      "quantity": 3
    },
    {
      "productId": 3,
      "quantity": 1
    }
  ],
  "remark": "请尽快配送，谢谢！"
}
```

### 2. 单商品购物车结算

```json
{
  "userId": 2,
  "addressId": 3,
  "items": [
    {
      "productId": 1,
      "quantity": 5
    }
  ],
  "remark": "单品订单"
}
```

## 响应格式

### 成功响应
```json
{
  "code": 200,
  "message": "success",
  "data": 12345
}
```

其中 `data` 字段返回创建的订单ID。

### 失败响应
```json
{
  "code": 500,
  "message": "订单创建失败",
  "data": null
}
```

## 业务逻辑说明

### 1. 数据验证
- 验证用户ID是否存在
- 验证收货地址ID是否存在且属于该用户
- 验证商品ID是否存在
- 验证商品库存是否充足

### 2. 金额计算
- 根据商品价格和数量计算每个商品的小计
- 累加所有商品小计得到订单总金额
- 设置配送费（当前为固定值）

### 3. 库存扣减
- 创建订单时会自动扣减商品库存
- 如果库存不足会抛出异常并回滚事务

### 4. 订单创建
- 生成唯一订单号
- 创建订单主记录
- 创建订单项记录
- 设置订单状态为"待支付"

## 使用cURL调用示例

```bash
curl -X POST http://localhost:8080/miniapp/order/checkout \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 1,
    "addressId": 1,
    "items": [
      {
        "productId": 1,
        "quantity": 2
      },
      {
        "productId": 2,
        "quantity": 3
      }
    ],
    "remark": "测试订单"
  }'
```

## 使用Postman调用

1. 设置请求方法为 `POST`
2. 设置URL为 `http://localhost:8080/miniapp/order/checkout`
3. 在Headers中添加 `Content-Type: application/json`
4. 在Body中选择raw格式，粘贴JSON请求体

## 错误处理

### 常见错误及解决方案

| 错误信息 | 原因 | 解决方案 |
|----------|------|----------|
| "用户不存在" | userId无效 | 检查用户ID是否正确 |
| "地址不存在" | addressId无效或不属于该用户 | 检查地址ID是否正确且属于该用户 |
| "商品库存不足" | 商品库存小于购买数量 | 减少购买数量或选择其他商品 |
| "商品信息不完整" | productId或quantity为空 | 确保所有商品信息完整 |
| "订单创建失败" | 系统内部错误 | 检查系统日志，联系技术支持 |

## 注意事项

1. **库存检查**：下单前建议先查询商品库存，避免库存不足导致下单失败
2. **地址验证**：确保收货地址属于当前用户，否则会创建失败
3. **事务处理**：订单创建过程中如果任何步骤失败，会自动回滚所有操作
4. **并发处理**：系统支持并发下单，但可能存在库存竞争，建议在前端做好库存提示

## 相关API

### 查询商品信息
```
GET /miniapp/product/detail/{productId}
```

### 查询用户地址
```
GET /miniapp/address/list?userId={userId}
```

### 查询订单详情
```
GET /miniapp/order/detail/{orderId}
```

### 订单支付
```
POST /miniapp/order/pay/{orderId}
```

## 测试数据

根据系统初始化数据，可以使用以下测试数据：

### 用户信息
- 用户ID: 1, 2, 3
- 地址ID: 1, 2, 3

### 商品信息
- 商品ID: 1 (红富士苹果, 价格: 12.80, 库存: 500)
- 商品ID: 2 (海南香蕉, 价格: 6.90, 库存: 300)
- 商品ID: 3 (有机生菜, 价格: 4.50, 库存: 200)

使用这些测试数据可以成功创建订单进行测试。