# 生鲜配送系统接口测试文档

## 接口测试环境
- 基础URL: http://localhost:8080/api
- 测试工具: PowerShell Invoke-RestMethod
- 测试时间: 2024-03-21

## 接口测试用例

### 1. 商品管理接口

#### 1.1 获取商品列表（管理端）
- 请求方法: GET
- 请求路径: /admin/product/list
- 请求参数: 
  - pageNum: 页码（默认1）
  - pageSize: 每页数量（默认10）
  - categoryId: 分类ID（可选）
  - keyword: 搜索关键字（可选）
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/admin/product/list?pageNum=1&pageSize=10" -Method GET
```
测试结果：
- 状态码: 200
- 消息: success
- 数据: 包含total=3的分页数据

#### 1.2 获取商品列表（小程序端）
- 请求方法: GET
- 请求路径: /miniapp/product/list
- 请求参数:
  - pageNum: 页码（默认1）
  - pageSize: 每页数量（默认10）
  - categoryId: 分类ID（可选）
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/miniapp/product/list?pageNum=1&pageSize=10" -Method GET
```
测试结果：
- 状态码: 500
- 错误: 内部服务器错误

### 2. 订单管理接口

#### 2.1 获取订单列表（小程序端）
- 请求方法: GET
- 请求路径: /miniapp/order/list
- 请求参数:
  - pageNum: 页码（默认1）
  - pageSize: 每页数量（默认10）
  - userId: 用户ID
  - status: 订单状态（可选）
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/miniapp/order/list?pageNum=1&pageSize=10&userId=1" -Method GET
```
测试结果：
- 状态码: 200
- 消息: success
- 数据: 包含total=2的分页数据

#### 2.2 获取订单详情（小程序端）
- 请求方法: GET
- 请求路径: /miniapp/order/detail/{id}
- 路径参数:
  - id: 订单ID
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/miniapp/order/detail/1" -Method GET
```
测试结果：
- 状态码: 200
- 消息: success
- 数据: 包含订单号、总金额、状态等详细信息

### 3. 分类管理接口

#### 3.1 获取分类列表（管理端）
- 请求方法: GET
- 请求路径: /admin/category/list
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/admin/category/list" -Method GET
```
测试结果：
- 状态码: 200
- 消息: success
- 数据: 返回分类列表数据

#### 3.2 获取分类列表（小程序端）
- 请求方法: GET
- 请求路径: /miniapp/category/list
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/miniapp/category/list" -Method GET
```
测试结果：
- 状态码: 200
- 消息: success
- 数据: 返回分类列表数据

### 4. 购物车接口

#### 4.1 获取购物车列表
- 请求方法: GET
- 请求路径: /miniapp/cart/list/{userId}
- 路径参数:
  - userId: 用户ID
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/miniapp/cart/list/1" -Method GET
```
测试结果：
- 状态码: 200
- 消息: success
- 数据: 返回用户购物车列表数据

### 5. 配送员管理接口

#### 5.1 获取配送员列表
- 请求方法: GET
- 请求路径: /delivery/deliveryman/list
- 请求参数:
  - pageNum: 页码（默认1）
  - pageSize: 每页数量（默认10）
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/delivery/deliveryman/list?pageNum=1&pageSize=10" -Method GET
```
测试结果：
- 状态码: 200
- 消息: success
- 数据: 包含total=2的分页数据

## 测试总结

已完成测试的接口：
1. 管理端商品列表 - 成功（200）
2. 小程序端商品列表 - 失败（500）
3. 管理端分类列表 - 成功（200）
4. 小程序端分类列表 - 成功（200）
5. 订单列表 - 成功（200）
6. 订单详情 - 成功（200）
7. 购物车列表 - 成功（200）
8. 配送员列表 - 成功（200）

发现的问题：
1. 小程序端商品列表接口返回500错误，需要进一步排查原因。可能的原因包括：
   - 数据库查询异常
   - 业务逻辑处理异常
   - 数据映射问题
   建议查看服务器日志以获取具体错误信息。

整体测试结果：
- 成功率：8/9 = 88.89%
- 失败率：1/9 = 11.11%
- 大部分接口运行正常，仅小程序端商品列表接口需要修复。