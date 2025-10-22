package com.fresh.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fresh.core.entity.Address;
import com.fresh.core.mapper.AddressMapper;
import com.fresh.core.service.AddressService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AddressServiceImpl extends ServiceImpl<AddressMapper, Address> implements AddressService {

    @Override
    public List<Address> getUserAddresses(String phone) {
        LambdaQueryWrapper<Address> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Address::getReceiverPhone, phone)
               .orderByDesc(Address::getIsDefault)
               .orderByDesc(Address::getUpdateTime);
        return list(wrapper);
    }

    @Override
    public boolean addAddress(Address address) {
        // 如果是默认地址，则将其他地址设为非默认
        if (Boolean.TRUE.equals(address.getIsDefault())) {
            clearOtherDefaultAddresses(address.getUserId());
        }
        return save(address);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateAddress(Address address) {
        // 如果是默认地址，则将其他地址设为非默认
        if (Boolean.TRUE.equals(address.getIsDefault())) {
            clearOtherDefaultAddresses(address.getUserId());
        }
        return updateById(address);
    }

    @Override
    public boolean deleteAddress(Long id) {
        return removeById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean setDefaultAddress(Long userId, Long addressId) {
        // 先将所有地址设为非默认
        clearOtherDefaultAddresses(userId);
        
        // 将指定地址设为默认
        Address address = new Address();
        address.setId(addressId);
        address.setIsDefault(true);
        return updateById(address);
    }

    private void clearOtherDefaultAddresses(Long userId) {
        LambdaQueryWrapper<Address> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Address::getUserId, userId)
               .eq(Address::getIsDefault, true);
        
        Address address = new Address();
        address.setIsDefault(false);
        
        update(address, wrapper);
    }
}