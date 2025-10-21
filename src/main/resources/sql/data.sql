-- 清空所有表的数据，确保幂等性
DELETE FROM `point_records`;
DELETE FROM `user_points`;
DELETE FROM `admins`;
DELETE FROM `delivery_men`;
DELETE FROM `order_items`;
DELETE FROM `orders`;
DELETE FROM `carts`;
DELETE FROM `products`;
DELETE FROM `categories`;
DELETE FROM `addresses`;
DELETE FROM `users`;

-- 初始化数据，严格匹配当前实体与 schema.sql

-- 用户数据（与 users 表字段一致）
INSERT INTO users (id, username, phone, openid, avatar, create_time, update_time) VALUES
(1, 'zhangsan', '13800138001', 'openid1', 'https://example.com/avatars/1.jpg', '2024-01-01 10:00:00', '2024-01-01 10:00:00'),
(2, 'lisi', '13800138002', 'openid2', 'https://example.com/avatars/2.jpg', '2024-01-01 10:00:00', '2024-01-01 10:00:00'),
(3, 'wangwu', '13800138003', 'openid3', 'https://example.com/avatars/3.jpg', '2024-01-01 10:00:00', '2024-01-01 10:00:00');

-- 收货地址数据（表名为 addresses，含 detail_address 与 is_default）
INSERT INTO addresses (id, user_id, receiver_name, receiver_phone, province, city, district, detail_address, is_default, create_time, update_time) VALUES
(1, 1, '张三', '13800138001', '北京市', '北京市', '朝阳区', '三里屯SOHO 1号楼1单元101室', 1, '2024-01-01 10:00:00', '2024-01-01 10:00:00'),
(2, 1, '张三公司', '13800138001', '北京市', '北京市', '海淀区', '中关村软件园二期3号楼5层', 0, '2024-01-01 10:00:00', '2024-01-01 10:00:00'),
(3, 2, '李四', '13800138002', '上海市', '上海市', '浦东新区', '陆家嘴金融中心25楼', 1, '2024-01-01 10:00:00', '2024-01-01 10:00:00');

-- 商品分类数据（去掉 description/image/status，仅保留 schema 中字段）
INSERT INTO categories (id, name, sort, create_time, update_time) VALUES
(1, '新鲜水果', 1, '2024-01-01 10:00:00', '2024-01-01 10:00:00'),
(2, '时令蔬菜', 2, '2024-01-01 10:00:00', '2024-01-01 10:00:00'),
(3, '肉禽蛋品', 3, '2024-01-01 10:00:00', '2024-01-01 10:00:00'),
(4, '海鲜水产', 4, '2024-01-01 10:00:00', '2024-01-01 10:00:00');

-- 商品数据（字段顺序与 schema.sql 一致）
INSERT INTO products (id, category_id, name, image, price, description, stock, status, create_time, update_time) VALUES
(1, 1, '红富士苹果', 'https://example.com/products/apple.jpg', 12.80, '山东烟台红富士苹果，脆甜多汁', 500, 1, '2024-01-01 10:00:00', '2024-01-01 10:00:00'),
(2, 1, '海南香蕉', 'https://example.com/products/banana.jpg', 6.90, '海南特产香蕉，香甜软糯', 300, 1, '2024-01-01 10:00:00', '2024-01-01 10:00:00'),
(3, 2, '有机生菜', 'https://example.com/products/lettuce.jpg', 4.50, '绿色有机生菜', 200, 1, '2024-01-01 10:00:00', '2024-01-01 10:00:00');

-- 购物车数据（仅引用已存在的用户与商品）
INSERT INTO carts (id, user_id, product_id, quantity, create_time, update_time) VALUES
(1, 1, 1, 2, '2024-01-01 10:00:00', '2024-01-01 10:00:00'),
(2, 1, 3, 1, '2024-01-01 10:00:00', '2024-01-01 10:00:00'),
(3, 2, 2, 3, '2024-01-01 10:00:00', '2024-01-01 10:00:00');

-- 订单数据（使用新的订单表结构）
INSERT INTO orders (id, order_no, user_id, address_id, total_price, actual_amount, discount_amount, delivery_fee, status, payment_status, receiver_name, receiver_phone, receiver_address, points_earned, points_used, remark, create_time, update_time) VALUES
(1, 'ORD202401010001', 1, 1, 51, 51.50, 0.00, 5.00, 5, 1, '张三', '13800138001', '北京市朝阳区三里屯SOHO 1号楼1单元101室', 5, 0, '请尽快配送', '2024-01-01 10:00:00', '2024-01-01 10:00:00'),
(2, 'ORD202401010002', 2, 3, 20, 20.70, 0.00, 5.00, 4, 1, '李四', '13800138002', '上海市浦东新区陆家嘴金融中心25楼', 2, 0, '请小心轻放', '2024-01-01 11:00:00', '2024-01-01 11:00:00'),
(3, 'ORD202401010003', 1, 1, 12, 12.80, 0.00, 5.00, 2, 1, '张三', '13800138001', '北京市朝阳区三里屯SOHO 1号楼1单元101室', 1, 0, '周末送货', '2024-01-01 12:00:00', '2024-01-01 12:00:00'),
(4, 'ORD202401010004', 1, 2, 18, 18.30, 0.00, 5.00, 1, 0, '张三公司', '13800138001', '北京市海淀区中关村软件园二期3号楼5层', 0, 0, '下午送货', '2024-01-01 13:00:00', '2024-01-01 13:00:00');

-- 订单项数据（补充 update_time 字段）
INSERT INTO order_items (id, order_id, product_id, product_name, product_image, price, quantity, create_time, update_time) VALUES
(1, 1, 1, '红富士苹果', 'https://example.com/products/apple.jpg', 12.80, 2, '2024-01-01 10:00:00', '2024-01-01 10:00:00'),
(2, 1, 3, '有机生菜', 'https://example.com/products/lettuce.jpg', 4.50, 1, '2024-01-01 10:00:00', '2024-01-01 10:00:00'),
(3, 2, 2, '海南香蕉', 'https://example.com/products/banana.jpg', 6.90, 3, '2024-01-01 11:00:00', '2024-01-01 11:00:00'),
(4, 3, 1, '红富士苹果', 'https://example.com/products/apple.jpg', 12.80, 1, '2024-01-01 12:00:00', '2024-01-01 12:00:00'),
(5, 4, 2, '海南香蕉', 'https://example.com/products/banana.jpg', 6.90, 2, '2024-01-01 13:00:00', '2024-01-01 13:00:00');

-- 配送员数据（匹配 delivery_men 表字段：username/phone/status）
INSERT INTO delivery_men (id, username, phone, status, create_time, update_time) VALUES
(1, '王师傅', '13900139001', 1, '2024-01-01 10:00:00', '2024-01-01 10:00:00'),
(2, '李师傅', '13900139002', 1, '2024-01-01 10:00:00', '2024-01-01 10:00:00');

-- 管理员数据（密码为123456的MD5加密值）
INSERT INTO admins (id, username, password, real_name, phone, email, status, create_time, update_time) VALUES
(1, 'admin', 'e10adc3949ba59abbe56e057f20f883e', '系统管理员', '13800138000', 'admin@fresh.com', 1, '2024-01-01 10:00:00', '2024-01-01 10:00:00'),
(2, 'manager', 'e10adc3949ba59abbe56e057f20f883e', '运营经理', '13800138888', 'manager@fresh.com', 1, '2024-01-01 10:00:00', '2024-01-01 10:00:00');

-- 用户积分数据
INSERT INTO user_points (id, user_id, phone, total_points, available_points, used_points, expired_points, create_time, update_time) VALUES
(1, 1, '13800138001', 100, 94, 6, 0, '2024-01-01 10:00:00', '2024-01-01 10:00:00'),
(2, 2, '13800138002', 50, 48, 2, 0, '2024-01-01 10:00:00', '2024-01-01 10:00:00'),
(3, 3, '13800138003', 20, 20, 0, 0, '2024-01-01 10:00:00', '2024-01-01 10:00:00');

-- 积分记录数据
INSERT INTO point_records (id, user_id, order_id, type, points, source, description, create_time) VALUES
(1, 1, 1, 1, 5, 'order', '订单完成获得积分', '2024-01-01 10:00:00'),
(2, 2, 2, 1, 2, 'order', '订单完成获得积分', '2024-01-01 11:00:00'),
(3, 1, 3, 1, 1, 'order', '订单完成获得积分', '2024-01-01 12:00:00'),
(4, 1, NULL, 1, 88, 'sign', '每日签到获得积分', '2024-01-01 08:00:00'),
(5, 2, NULL, 1, 46, 'sign', '每日签到获得积分', '2024-01-01 08:00:00'),
(6, 3, NULL, 1, 20, 'sign', '每日签到获得积分', '2024-01-01 08:00:00');