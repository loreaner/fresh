# 管理员登录接口文档

## 基础信息

- **基础URL**: `http://localhost:8080/api`
- **接口地址**: `POST /admin/login`

## 请求参数

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| username | string | 是 | 用户名 |
| password | string | 是 | 密码 |

## 请求示例

```json
{
  "username": "admin",
  "password": "123456"
}
```

## 响应格式

### 成功响应

```json
{
  "code": 200,
  "message": "success",
  "data": true
}
```

### 错误响应

```json
{
  "code": 200,
  "message": "success",
  "data": false
}
```

## Axios 调用示例

```javascript
import axios from 'axios';

// 管理员登录
const adminLogin = async (username, password) => {
  try {
    const response = await axios.post('http://localhost:8080/api/admin/login', {
      username,
      password
    });
    
    if (response.data.code === 200 && response.data.data === true) {
      // 登录成功
      console.log('登录成功');
      return true;
    } else {
      // 登录失败
      throw new Error('用户名或密码错误');
    }
  } catch (error) {
    throw new Error(error.response?.data?.message || '登录失败');
  }
};

// 使用示例
adminLogin('admin', '123456')
  .then(result => {
    console.log('登录成功:', result);
  })
  .catch(error => {
    console.error('登录失败:', error.message);
  });
```

## 测试账号

| 用户名 | 密码 |
|--------|------|
| admin | 123456 |
| manager | 123456 |