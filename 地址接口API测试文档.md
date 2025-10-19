# 地址管理接口API测试文档

## 基本信息
- **服务器地址**: http://localhost:8081
- **基础路径**: /api/miniapp/address
- **Content-Type**: application/json

## 接口列表

### 1. 获取用户地址列表 ✅
- **接口地址**: `GET /api/miniapp/address/list`
- **请求方法**: GET
- **接口描述**: 获取指定用户的所有地址列表，按默认地址和更新时间排序

#### 请求参数
查询参数: `userId` (Long)

#### curl测试命令
```bash
# PowerShell命令
Invoke-WebRequest -Uri "http://localhost:8081/api/miniapp/address/list?userId=1" -Method GET

# curl命令
curl -X GET "http://localhost:8081/api/miniapp/address/list?userId=1"
```

#### 成功响应示例
```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "id": 4,
      "createTime": "2025-10-12T18:43:42",
      "updateTime": "2025-10-12T18:43:42",
      "userId": 1,
      "receiverName": "张三",
      "receiverPhone": "13800138000",
      "province": "北京市",
      "city": "北京市",
      "district": "朝阳区",
      "detailAddress": "朝阳路123号",
      "isDefault": true
    }
  ]
}
```

#### 错误响应示例
```json
{
  "code": 400,
  "message": "参数错误",
  "data": null
}
```

---

### 2. 获取地址详情 ✅
- **接口地址**: `GET /api/miniapp/address/detail/{id}`
- **请求方法**: GET
- **接口描述**: 根据地址ID获取地址详细信息

#### 请求参数
路径参数: `id` (Long)

#### curl测试命令
```bash
# PowerShell命令
Invoke-WebRequest -Uri "http://localhost:8081/api/miniapp/address/detail/4" -Method GET

# curl命令
curl -X GET "http://localhost:8081/api/miniapp/address/detail/4"
```

#### 成功响应示例
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 4,
    "createTime": "2025-10-12T18:43:42",
    "updateTime": "2025-10-12T18:43:42",
    "userId": 1,
    "receiverName": "张三",
    "receiverPhone": "13800138000",
    "province": "北京市",
    "city": "北京市",
    "district": "朝阳区",
    "detailAddress": "朝阳路123号",
    "isDefault": true
  }
}
```

#### 错误响应示例
```json
{
  "code": 400,
  "message": "地址不存在",
  "data": null
}
```

---

### 3. 添加地址 ✅
- **接口地址**: `POST /api/miniapp/address/add`
- **请求方法**: POST
- **接口描述**: 添加新的收货地址

#### 请求参数
```json
{
  "userId": 1,
  "receiverName": "张三",
  "receiverPhone": "13800138000",
  "province": "北京市",
  "city": "北京市",
  "district": "朝阳区",
  "detailAddress": "朝阳路123号",
  "isDefault": true
}
```

#### 参数说明
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| userId | Long | 是 | 用户ID |
| receiverName | String | 是 | 收货人姓名 |
| receiverPhone | String | 是 | 收货人电话 |
| province | String | 是 | 省份 |
| city | String | 是 | 城市 |
| district | String | 是 | 区县 |
| detailAddress | String | 是 | 详细地址 |
| isDefault | Boolean | 否 | 是否为默认地址 |

#### curl测试命令
```bash
# PowerShell命令
Invoke-WebRequest -Uri "http://localhost:8081/api/miniapp/address/add" -Method POST -ContentType "application/json" -Body '{"userId": 1, "receiverName": "张三", "receiverPhone": "13800138000", "province": "北京市", "city": "北京市", "district": "朝阳区", "detailAddress": "朝阳路123号", "isDefault": true}'

# curl命令
curl -X POST "http://localhost:8081/api/miniapp/address/add" -H "Content-Type: application/json" -d '{"userId": 1, "receiverName": "张三", "receiverPhone": "13800138000", "province": "北京市", "city": "北京市", "district": "朝阳区", "detailAddress": "朝阳路123号", "isDefault": true}'
```

#### 成功响应示例
```json
{
  "code": 200,
  "message": "success",
  "data": null
}
```

#### 错误响应示例
```json
{
  "code": 500,
  "message": "添加失败",
  "data": null
}
```

---

### 4. 更新地址
- **接口地址**: `PUT /api/miniapp/address/update`
- **请求方法**: PUT
- **接口描述**: 更新现有地址信息

#### 请求参数
```json
{
  "id": 1,
  "userId": 1,
  "receiverName": "李四",
  "receiverPhone": "13800138001",
  "province": "上海市",
  "city": "上海市",
  "district": "浦东新区",
  "detailAddress": "浦东大道456号",
  "isDefault": false
}
```

#### 参数说明
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| id | Long | 是 | 地址ID |
| userId | Long | 是 | 用户ID |
| receiverName | String | 是 | 收货人姓名 |
| receiverPhone | String | 是 | 收货人电话 |
| province | String | 是 | 省份 |
| city | String | 是 | 城市 |
| district | String | 是 | 区县 |
| detailAddress | String | 是 | 详细地址 |
| isDefault | Boolean | 否 | 是否为默认地址 |

#### curl测试命令
```bash
# PowerShell命令
Invoke-WebRequest -Uri "http://localhost:8081/api/miniapp/address/update" -Method PUT -Headers @{"Content-Type"="application/json"} -Body '{"id": 1, "userId": 1, "receiverName": "李四", "receiverPhone": "13800138001", "province": "上海市", "city": "上海市", "district": "浦东新区", "detailAddress": "浦东大道456号", "isDefault": false}'
```

#### 成功响应示例
```json
{
  "code": 200,
  "message": "success",
  "data": null
}
```

#### 错误响应示例
```json
{
  "code": 400,
  "message": "地址ID不能为空",
  "data": null
}
```

```json
{
  "code": 500,
  "message": "更新失败",
  "data": null
}
```

---

### 5. 删除地址 ✅
- **接口地址**: `DELETE /api/miniapp/address/delete/{id}`
- **请求方法**: DELETE
- **接口描述**: 删除指定的地址

#### 请求参数
路径参数: `id` (Long)

#### 参数说明
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| id | Long | 是 | 要删除的地址ID |

#### curl测试命令
```bash
# PowerShell命令
Invoke-WebRequest -Uri "http://localhost:8081/api/miniapp/address/delete/4" -Method DELETE

# curl命令
curl -X DELETE "http://localhost:8081/api/miniapp/address/delete/4"
```

#### 成功响应示例
```json
{
  "code": 200,
  "message": "success",
  "data": null
}
```

#### 错误响应示例
```json
{
  "code": 500,
  "message": "删除失败",
  "data": null
}
```

---

### 6. 设置默认地址
- **接口地址**: `PUT /api/miniapp/address/default`
- **请求方法**: PUT
- **接口描述**: 将指定地址设置为用户的默认地址

#### 请求参数
```json
{
  "userId": 1,
  "addressId": 2
}
```

#### 参数说明
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| userId | Long | 是 | 用户ID |
| addressId | Long | 是 | 要设置为默认的地址ID |

#### curl测试命令
```bash
# PowerShell命令
Invoke-WebRequest -Uri "http://localhost:8081/api/miniapp/address/default" -Method PUT -Headers @{"Content-Type"="application/json"} -Body '{"userId": 1, "addressId": 2}'
```

#### 成功响应示例
```json
{
  "code": 200,
  "message": "success",
  "data": null
}
```

#### 错误响应示例
```json
{
  "code": 500,
  "message": "设置失败",
  "data": null
}
```

---

## 修复总结

### 测试环境
- 服务器: http://localhost:8081
- 测试时间: 2025-10-12
- 测试工具: PowerShell Invoke-WebRequest

### 已修复的问题 ✅

1. **AddressRequest DTO 缺少 userId 字段**
   - **问题**: 添加地址时 `user_id` 字段为空，导致数据库插入失败
   - **修复**: 在 `AddressRequest.java` 中添加了 `userId` 字段
   - **结果**: 添加地址接口现在正常工作

2. **GET 请求使用 @RequestBody 的设计问题**
   - **问题**: GET 请求不应该使用请求体传参
   - **修复**: 
     - 获取地址列表改为使用查询参数: `GET /list?userId=1`
     - 获取地址详情改为使用路径参数: `GET /detail/{id}`
   - **结果**: GET 接口现在符合 RESTful 设计规范

3. **DELETE 请求使用 @RequestBody 的设计问题**
   - **问题**: DELETE 请求建议使用路径参数
   - **修复**: 删除地址改为使用路径参数: `DELETE /delete/{id}`
   - **结果**: DELETE 接口现在符合 RESTful 设计规范

### 测试结果汇总 ✅

| 接口 | 方法 | 状态 | 响应码 | 说明 |
|------|------|------|--------|------|
| /list | GET | ✅ | 200 | 正常返回地址列表 |
| /detail/{id} | GET | ✅ | 200 | 正常返回地址详情 |
| /add | POST | ✅ | 200 | 成功添加地址 |
| /update | PUT | ⚠️ | - | 需要进一步测试 |
| /delete/{id} | DELETE | ✅ | 200 | 成功删除地址 |
| /default | PUT | ⚠️ | - | 需要进一步测试 |

### 核心修改文件

1. **AddressRequest.java** - 添加了 `userId` 字段
2. **AddressController.java** - 修复了参数传递方式:
   - `getAddressList()` 使用 `@RequestParam Long userId`
   - `getAddressDetail()` 使用 `@PathVariable Long id`
   - `deleteAddress()` 使用 `@PathVariable Long id`

### 数据库状态
- 数据库连接正常
- addresses 表结构完整，包含所有必要字段
- 成功插入和删除地址记录

---

## 总结

本文档详细记录了地址管理接口的测试过程和结果。虽然由于接口设计和数据库配置问题导致部分接口无法正常测试，但已经提供了完整的接口规范、参数说明、curl命令和预期响应格式，为后续的接口调试和前端对接提供了完整的参考。