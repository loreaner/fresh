package com.fresh.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fresh.core.entity.DeliveryMan;
import com.fresh.core.mapper.DeliveryManMapper;
import com.fresh.core.service.DeliveryManService;
import org.springframework.stereotype.Service;

@Service
public class DeliveryManServiceImpl extends ServiceImpl<DeliveryManMapper, DeliveryMan> implements DeliveryManService {

    @Override
    public Page<DeliveryMan> getDeliveryManPage(Integer pageNum, Integer pageSize) {
        Page<DeliveryMan> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<DeliveryMan> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(DeliveryMan::getCreateTime);
        return page(page, wrapper);
    }

    @Override
    public DeliveryMan getDeliveryManDetail(Long id) {
        return getById(id);
    }

    @Override
    public boolean addDeliveryMan(DeliveryMan deliveryMan) {
        return save(deliveryMan);
    }

    @Override
    public boolean updateDeliveryMan(DeliveryMan deliveryMan) {
        return updateById(deliveryMan);
    }

    @Override
    public boolean deleteDeliveryMan(Long id) {
        return removeById(id);
    }
}