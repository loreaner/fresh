# 生鲜配送系统 - Java后端项目结构

## 完整项目结构

```
fresh-delivery-system/
├── pom.xml                           # Maven配置文件
├── README.md                         # 项目说明文档
└── src/
    ├── main/
    │   ├── java/com/fresh/
    │   │   ├── FreshDeliveryApplication.java    # Spring Boot启动类
    │   │   ├── common/                          # 公共模块
    │   │   │   ├── config/                      # 公共配置
    │   │   │   │   ├── WebConfig.java          # Web配置
    │   │   │   │   ├── MyBatisConfig.java      # MyBatis配置
    │   │   │   │   ├── RedisConfig.java        # Redis配置
    │   │   │   │   ├── WeChatConfig.java       # 微信配置
    │   │   │   │   └── SwaggerConfig.java      # Swagger配置
    │   │   │   ├── exception/                   # 全局异常处理
    │   │   │   │   ├── GlobalExceptionHandler.java
    │   │   │   │   ├── BusinessException.java
    │   │   │   │   └── ErrorCode.java
    │   │   │   ├── utils/                       # 工具类
    │   │   │   │   ├── JwtUtil.java            # JWT工具
    │   │   │   │   ├── WeChatUtil.java         # 微信工具
    │   │   │   │   ├── QRCodeUtil.java         # 二维码工具
    │   │   │   │   ├── DateUtil.java           # 日期工具
    │   │   │   │   └── StringUtil.java         # 字符串工具
    │   │   │   ├── response/                    # 通用响应
    │   │   │   │   ├── Result.java             # 统一响应结果
    │   │   │   │   ├── PageResult.java         # 分页响应结果
    │   │   │   │   └── ResultCode.java         # 响应状态码
    │   │   │   ├── interceptor/                 # 拦截器
    │   │   │   │   ├── AuthInterceptor.java    # 认证拦截器
    │   │   │   │   └── LogInterceptor.java     # 日志拦截器
    │   │   │   └── annotation/                  # 自定义注解
    │   │   │       ├── RequireAuth.java        # 需要认证注解
    │   │   │       └── Log.java                # 日志注解
    │   │   ├── core/                            # 核心模块
    │   │   │   ├── entity/                      # 实体类
    │   │   │   │   ├── User.java               # 用户实体
    │   │   │   │   ├── Product.java            # 商品实体
    │   │   │   │   ├── Category.java           # 商品分类实体
    │   │   │   │   ├── Order.java              # 订单实体
    │   │   │   │   ├── OrderItem.java          # 订单项实体
    │   │   │   │   ├── Cart.java               # 购物车实体
    │   │   │   │   ├── Address.java            # 收货地址实体
    │   │   │   │   ├── Points.java             # 积分实体
    │   │   │   │   ├── PointsRecord.java       # 积分记录实体
    │   │   │   │   ├── PointsExchange.java     # 积分兑换实体
    │   │   │   │   ├── QRCode.java             # 二维码实体
    │   │   │   │   ├── DeliveryMan.java        # 配送员实体
    │   │   │   │   ├── Banner.java             # 轮播图实体
    │   │   │   │   └── CompanyInfo.java        # 公司信息实体
    │   │   │   ├── mapper/                      # 数据访问层
    │   │   │   │   ├── UserMapper.java
    │   │   │   │   ├── ProductMapper.java
    │   │   │   │   ├── CategoryMapper.java
    │   │   │   │   ├── OrderMapper.java
    │   │   │   │   ├── OrderItemMapper.java
    │   │   │   │   ├── CartMapper.java
    │   │   │   │   ├── AddressMapper.java
    │   │   │   │   ├── PointsMapper.java
    │   │   │   │   ├── PointsRecordMapper.java
    │   │   │   │   ├── PointsExchangeMapper.java
    │   │   │   │   ├── QRCodeMapper.java
    │   │   │   │   ├── DeliveryManMapper.java
    │   │   │   │   ├── BannerMapper.java
    │   │   │   │   └── CompanyInfoMapper.java
    │   │   │   ├── service/                     # 服务接口
    │   │   │   │   ├── UserService.java
    │   │   │   │   ├── ProductService.java
    │   │   │   │   ├── CategoryService.java
    │   │   │   │   ├── OrderService.java
    │   │   │   │   ├── CartService.java
    │   │   │   │   ├── AddressService.java
    │   │   │   │   ├── PointsService.java
    │   │   │   │   ├── QRCodeService.java
    │   │   │   │   ├── DeliveryService.java
    │   │   │   │   ├── BannerService.java
    │   │   │   │   └── CompanyInfoService.java
    │   │   │   └── service/impl/                # 服务实现
    │   │   │       ├── UserServiceImpl.java
    │   │   │       ├── ProductServiceImpl.java
    │   │   │       ├── CategoryServiceImpl.java
    │   │   │       ├── OrderServiceImpl.java
    │   │   │       ├── CartServiceImpl.java
    │   │   │       ├── AddressServiceImpl.java
    │   │   │       ├── PointsServiceImpl.java
    │   │   │       ├── QRCodeServiceImpl.java
    │   │   │       ├── DeliveryServiceImpl.java
    │   │   │       ├── BannerServiceImpl.java
    │   │   │       └── CompanyInfoServiceImpl.java
    │   │   ├── miniapp/                         # 小程序端接口
    │   │   │   ├── controller/                  # 控制器
    │   │   │   │   ├── AuthController.java     # 用户授权控制器
    │   │   │   │   ├── HomeController.java     # 首页控制器
    │   │   │   │   ├── ProductController.java  # 商品控制器
    │   │   │   │   ├── CartController.java     # 购物车控制器
    │   │   │   │   ├── OrderController.java    # 订单控制器
    │   │   │   │   ├── UserController.java     # 用户中心控制器
    │   │   │   │   ├── AddressController.java  # 地址管理控制器
    │   │   │   │   ├── PointsController.java   # 积分管理控制器
    │   │   │   │   └── PaymentController.java  # 支付控制器
    │   │   │   ├── dto/                         # 数据传输对象
    │   │   │   │   ├── LoginRequest.java
    │   │   │   │   ├── OrderRequest.java
    │   │   │   │   ├── CartRequest.java
    │   │   │   │   ├── AddressRequest.java
    │   │   │   │   ├── PointsExchangeRequest.java
    │   │   │   │   └── PaymentRequest.java
    │   │   │   └── vo/                          # 视图对象
    │   │   │       ├── UserVO.java
    │   │   │       ├── ProductVO.java
    │   │   │       ├── OrderVO.java
    │   │   │       ├── CartVO.java
    │   │   │       ├── AddressVO.java
    │   │   │       └── PointsVO.java
    │   │   ├── admin/                           # 管理后台接口
    │   │   │   ├── controller/                  # 控制器
    │   │   │   │   ├── AdminAuthController.java    # 管理员认证
    │   │   │   │   ├── AdminProductController.java # 商品管理
    │   │   │   │   ├── AdminCategoryController.java # 分类管理
    │   │   │   │   ├── AdminOrderController.java   # 订单管理
    │   │   │   │   ├── AdminUserController.java    # 用户管理
    │   │   │   │   ├── AdminPointsController.java  # 积分管理
    │   │   │   │   ├── AdminQRCodeController.java  # 二维码管理
    │   │   │   │   ├── AdminDeliveryController.java # 配送管理
    │   │   │   │   ├── AdminStatisticsController.java # 数据统计
    │   │   │   │   └── AdminSettingsController.java # 系统设置
    │   │   │   ├── dto/                         # 数据传输对象
    │   │   │   │   ├── AdminLoginRequest.java
    │   │   │   │   ├── ProductRequest.java
    │   │   │   │   ├── CategoryRequest.java
    │   │   │   │   ├── QRCodeRequest.java
    │   │   │   │   ├── DeliveryManRequest.java
    │   │   │   │   └── SettingsRequest.java
    │   │   │   └── vo/                          # 视图对象
    │   │   │       ├── AdminUserVO.java
    │   │   │       ├── ProductManageVO.java
    │   │   │       ├── OrderManageVO.java
    │   │   │       ├── StatisticsVO.java
    │   │   │       └── PointsStatisticsVO.java
    │   │   └── delivery/                        # 配送端接口
    │   │       ├── controller/                  # 控制器
    │   │       │   ├── DeliveryAuthController.java # 配送员认证
    │   │       │   ├── DeliveryOrderController.java # 配送订单管理
    │   │       │   └── DeliveryStatisticsController.java # 配送统计
    │   │       ├── dto/                         # 数据传输对象
    │   │       │   ├── DeliveryLoginRequest.java
    │   │       │   └── OrderStatusRequest.java
    │   │       └── vo/                          # 视图对象
    │   │           ├── DeliveryOrderVO.java
    │   │           └── DeliveryStatisticsVO.java
    │   └── resources/
    │       ├── mapper/                          # MyBatis映射文件
    │       │   ├── UserMapper.xml
    │       │   ├── ProductMapper.xml
    │       │   ├── CategoryMapper.xml
    │       │   ├── OrderMapper.xml
    │       │   ├── OrderItemMapper.xml
    │       │   ├── CartMapper.xml
    │       │   ├── AddressMapper.xml
    │       │   ├── PointsMapper.xml
    │       │   ├── PointsRecordMapper.xml
    │       │   ├── PointsExchangeMapper.xml
    │       │   ├── QRCodeMapper.xml
    │       │   ├── DeliveryManMapper.xml
    │       │   ├── BannerMapper.xml
    │       │   └── CompanyInfoMapper.xml
    │       ├── static/                          # 静态资源
    │       │   ├── images/                      # 图片资源
    │       │   ├── css/                         # 样式文件
    │       │   └── js/                          # JavaScript文件
    │       ├── templates/                       # 模板文件
    │       ├── application.yml                  # 主配置文件
    │       ├── application-dev.yml              # 开发环境配置
    │       ├── application-prod.yml             # 生产环境配置
    │       └── sql/                             # 数据库脚本
    │           ├── schema.sql                   # 建表脚本
    │           └── data.sql                     # 初始数据脚本
    └── test/
        └── java/com/fresh/
            ├── service/                         # 服务层测试
            ├── controller/                      # 控制器测试
            └── mapper/                          # 数据访问层测试
```

## 详细模块说明

### 1. common模块 - 公共组件
- **config/**: 系统配置类
  - WebConfig: Web相关配置，跨域、拦截器等
  - MyBatisConfig: MyBatis配置，分页插件等
  - RedisConfig: Redis缓存配置
  - WeChatConfig: 微信小程序配置
  - SwaggerConfig: API文档配置

- **exception/**: 全局异常处理
  - GlobalExceptionHandler: 全局异常处理器
  - BusinessException: 业务异常类
  - ErrorCode: 错误码枚举

- **utils/**: 工具类
  - JwtUtil: JWT令牌工具
  - WeChatUtil: 微信API工具
  - QRCodeUtil: 二维码生成工具
  - DateUtil: 日期处理工具
  - StringUtil: 字符串工具

- **response/**: 统一响应格式
  - Result: 统一响应结果封装
  - PageResult: 分页响应结果
  - ResultCode: 响应状态码

- **interceptor/**: 拦截器
  - AuthInterceptor: 用户认证拦截器
  - LogInterceptor: 操作日志拦截器

- **annotation/**: 自定义注解
  - RequireAuth: 需要认证注解
  - Log: 操作日志注解

### 2. core模块 - 核心业务模块
- **entity/**: 实体类定义
  - User: 用户信息
  - Product: 商品信息
  - Category: 商品分类
  - Order: 订单信息
  - OrderItem: 订单项
  - Cart: 购物车
  - Address: 收货地址
  - Points: 用户积分
  - PointsRecord: 积分记录
  - PointsExchange: 积分兑换
  - QRCode: 二维码信息
  - DeliveryMan: 配送员信息
  - Banner: 轮播图
  - CompanyInfo: 公司信息

- **mapper/**: 数据访问层接口
- **service/**: 业务服务接口
- **service/impl/**: 业务服务实现

### 3. miniapp模块 - 小程序端接口
- **controller/**: REST API控制器
  - AuthController: 用户授权（微信登录、获取用户信息）
  - HomeController: 首页数据（轮播图、分类、推荐商品）
  - ProductController: 商品相关（列表、详情、搜索）
  - CartController: 购物车管理
  - OrderController: 订单管理（下单、查询、状态更新）
  - UserController: 用户中心（个人信息、订单历史）
  - AddressController: 收货地址管理
  - PointsController: 积分管理（扫码获取、兑换、记录查询）
  - PaymentController: 支付相关（微信支付）

- **dto/**: 请求数据传输对象
- **vo/**: 响应视图对象

### 4. admin模块 - 管理后台接口
- **controller/**: 管理后台控制器
  - AdminAuthController: 管理员认证
  - AdminProductController: 商品管理（增删改查、上下架）
  - AdminCategoryController: 分类管理
  - AdminOrderController: 订单管理（状态更新、配送分配）
  - AdminUserController: 用户管理
  - AdminPointsController: 积分系统管理
  - AdminQRCodeController: 二维码管理（生成、启用/禁用）
  - AdminDeliveryController: 配送管理（配送员管理、配送区域）
  - AdminStatisticsController: 数据统计（销售统计、用户统计）
  - AdminSettingsController: 系统设置（公司信息、基础配置）

### 5. delivery模块 - 配送端接口
- **controller/**: 配送端控制器
  - DeliveryAuthController: 配送员登录认证
  - DeliveryOrderController: 配送订单管理（接单、状态更新）
  - DeliveryStatisticsController: 配送统计（今日配送、历史记录）

## 数据库表设计

### 用户相关表
- **users**: 用户基本信息表
- **user_addresses**: 用户收货地址表

### 商品相关表
- **categories**: 商品分类表
- **products**: 商品信息表
- **product_images**: 商品图片表

### 订单相关表
- **orders**: 订单主表
- **order_items**: 订单项表
- **carts**: 购物车表

### 积分相关表
- **user_points**: 用户积分表
- **points_records**: 积分记录表
- **points_exchanges**: 积分兑换申请表
- **qr_codes**: 二维码表

### 配送相关表
- **delivery_men**: 配送员信息表
- **delivery_orders**: 配送订单表

### 系统相关表
- **banners**: 轮播图表
- **company_info**: 公司信息表
- **admin_users**: 管理员用户表

## 技术栈

### 后端技术
- **Spring Boot 2.7+**: 主框架
- **Spring Security**: 安全框架
- **MyBatis Plus**: ORM框架
- **MySQL 8.0+**: 数据库
- **Redis**: 缓存
- **JWT**: 认证方案
- **Swagger**: API文档

### 第三方集成
- **微信小程序API**: 用户授权、支付
- **阿里云OSS**: 文件存储
- **短信服务**: 验证码发送

## 开发规范

### 代码规范
1. 使用阿里巴巴Java开发手册
2. 统一的代码格式化配置
3. 完善的注释和文档

### 接口规范
1. RESTful API设计
2. 统一的响应格式
3. 完善的错误处理

### 数据库规范
1. 统一的命名规范
2. 合理的索引设计
3. 数据库版本控制