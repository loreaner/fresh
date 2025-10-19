-- 产品数据插入脚本
-- 注意：价格使用分为单位（例如：12.80元 = 1280分）

-- 插入商品分类数据
INSERT INTO categories (id, name, sort, create_time, update_time) VALUES
(1, '新鲜水果', 1, NOW(), NOW()),
(2, '新鲜蔬菜', 2, NOW(), NOW()),
(3, '肉类海鲜', 3, NOW(), NOW()),
(4, '乳制品', 4, NOW(), NOW()),
(5, '粮油调料', 5, NOW(), NOW())
ON DUPLICATE KEY UPDATE 
name = VALUES(name), 
sort = VALUES(sort), 
update_time = NOW();

-- 插入商品数据（价格以分为单位）
INSERT INTO products (id, category_id, name, description, price, stock, image, status, create_time, update_time) VALUES
-- 水果类
(1, 1, '新鲜苹果', '红富士苹果，香甜可口，产地山东烟台', 1280, 100, 'https://example.com/images/apple.jpg', 1, NOW(), NOW()),
(2, 1, '有机香蕉', '进口有机香蕉，营养丰富，无农药残留', 850, 80, 'https://example.com/images/banana.jpg', 1, NOW(), NOW()),
(3, 1, '新鲜橙子', '赣南脐橙，汁多味甜，维C丰富', 1500, 60, 'https://example.com/images/orange.jpg', 1, NOW(), NOW()),
(4, 1, '进口猕猴桃', '新西兰进口猕猴桃，酸甜可口', 2200, 40, 'https://example.com/images/kiwi.jpg', 1, NOW(), NOW()),
(5, 1, '新鲜草莓', '奶油草莓，香甜多汁', 3500, 30, 'https://example.com/images/strawberry.jpg', 1, NOW(), NOW()),

-- 蔬菜类
(6, 2, '有机白菜', '有机种植大白菜，绿色健康', 680, 120, 'https://example.com/images/cabbage.jpg', 1, NOW(), NOW()),
(7, 2, '新鲜西红柿', '沙瓤西红柿，酸甜适中', 980, 90, 'https://example.com/images/tomato.jpg', 1, NOW(), NOW()),
(8, 2, '有机胡萝卜', '有机胡萝卜，营养丰富', 750, 100, 'https://example.com/images/carrot.jpg', 1, NOW(), NOW()),
(9, 2, '新鲜黄瓜', '水嫩黄瓜，清脆爽口', 580, 110, 'https://example.com/images/cucumber.jpg', 1, NOW(), NOW()),
(10, 2, '有机菠菜', '有机菠菜，铁质丰富', 880, 70, 'https://example.com/images/spinach.jpg', 1, NOW(), NOW()),

-- 肉类海鲜
(11, 3, '新鲜猪肉', '优质猪肉，肉质鲜美', 2800, 50, 'https://example.com/images/pork.jpg', 1, NOW(), NOW()),
(12, 3, '新鲜牛肉', '优质牛肉，营养丰富', 4500, 30, 'https://example.com/images/beef.jpg', 1, NOW(), NOW()),
(13, 3, '新鲜鸡肉', '土鸡肉，肉质紧实', 2200, 40, 'https://example.com/images/chicken.jpg', 1, NOW(), NOW()),
(14, 3, '新鲜带鱼', '深海带鱼，肉质鲜美', 3200, 25, 'https://example.com/images/fish.jpg', 1, NOW(), NOW()),
(15, 3, '新鲜虾仁', '优质虾仁，Q弹爽滑', 5800, 20, 'https://example.com/images/shrimp.jpg', 1, NOW(), NOW()),

-- 乳制品
(16, 4, '纯牛奶', '优质纯牛奶，营养丰富', 1200, 80, 'https://example.com/images/milk.jpg', 1, NOW(), NOW()),
(17, 4, '酸奶', '益生菌酸奶，助消化', 800, 60, 'https://example.com/images/yogurt.jpg', 1, NOW(), NOW()),
(18, 4, '奶酪', '进口奶酪，口感醇厚', 2500, 35, 'https://example.com/images/cheese.jpg', 1, NOW(), NOW()),

-- 粮油调料
(19, 5, '优质大米', '东北优质大米，粒粒饱满', 1500, 100, 'https://example.com/images/rice.jpg', 1, NOW(), NOW()),
(20, 5, '花生油', '纯正花生油，香味浓郁', 3800, 50, 'https://example.com/images/oil.jpg', 1, NOW(), NOW())

ON DUPLICATE KEY UPDATE 
category_id = VALUES(category_id),
name = VALUES(name),
description = VALUES(description),
price = VALUES(price),
stock = VALUES(stock),
image = VALUES(image),
status = VALUES(status),
update_time = NOW();

-- 查询验证数据
SELECT 
    p.id,
    p.name,
    p.description,
    CONCAT(p.price / 100, '元') as price_yuan,
    p.price as price_cents,
    p.stock,
    c.name as category_name
FROM products p
LEFT JOIN categories c ON p.category_id = c.id
ORDER BY p.category_id, p.id;