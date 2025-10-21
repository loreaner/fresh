package com.fresh.core.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fresh.core.entity.DeliveryMan;

public interface DeliveryManService extends IService<DeliveryMan> {
    Page<DeliveryMan> getDeliveryManPage(Integer pageNum, Integer pageSize);
    DeliveryMan getDeliveryManDetail(Long id);
    boolean addDeliveryMan(DeliveryMan deliveryMan);
    boolean updateDeliveryMan(DeliveryMan deliveryMan);
    boolean deleteDeliveryMan(Long id);
}