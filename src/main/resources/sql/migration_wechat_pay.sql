-- 微信支付功能数据库迁移脚本
-- 执行时间：2024年

-- 1. 修改订单表金额字段类型为整数（分为单位）
ALTER TABLE `orders` 
MODIFY COLUMN `total_amount` int(11) NOT NULL COMMENT '订单总金额（分）',
MODIFY COLUMN `actual_amount` int(11) NOT NULL COMMENT '实际支付金额（分）',
MODIFY COLUMN `discount_amount` int(11) DEFAULT 0 COMMENT '优惠金额（分）',
MODIFY COLUMN `delivery_fee` int(11) DEFAULT 0 COMMENT '配送费（分）';

-- 2. 添加微信支付相关字段
ALTER TABLE `orders` 
ADD COLUMN `wechat_order_no` varchar(64) DEFAULT NULL COMMENT '微信订单号' AFTER `payment_time`,
ADD COLUMN `wechat_prepay_id` varchar(64) DEFAULT NULL COMMENT '微信预支付ID' AFTER `wechat_order_no`;

-- 3. 修改订单项表价格字段类型为整数（分为单位）
ALTER TABLE `order_items` 
MODIFY COLUMN `price` int(11) NOT NULL COMMENT '商品价格（分）';

-- 4. 添加索引
ALTER TABLE `orders` 
ADD INDEX `idx_wechat_order_no` (`wechat_order_no`),
ADD INDEX `idx_wechat_prepay_id` (`wechat_prepay_id`);

-- 5. 更新现有数据（如果有的话）
-- 将现有的decimal价格转换为分（乘以100）
-- 注意：这个脚本假设现有数据的价格是以元为单位的
UPDATE `orders` SET 
    `total_amount` = `total_amount` * 100,
    `actual_amount` = `actual_amount` * 100,
    `discount_amount` = `discount_amount` * 100,
    `delivery_fee` = `delivery_fee` * 100
WHERE `total_amount` < 10000; -- 只更新小于100元的订单，避免重复执行

UPDATE `order_items` SET 
    `price` = `price` * 100
WHERE `price` < 10000; -- 只更新小于100元的商品，避免重复执行

-- 6. 验证数据
SELECT 
    id, order_no, total_amount, actual_amount, discount_amount, delivery_fee,
    wechat_order_no, wechat_prepay_id
FROM `orders` 
LIMIT 5;

SELECT 
    id, order_id, product_id, product_name, price, quantity
FROM `order_items` 
LIMIT 5;