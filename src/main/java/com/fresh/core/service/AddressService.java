package com.fresh.core.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.fresh.core.entity.Address;

import java.util.List;

public interface AddressService extends IService<Address> {
    List<Address> getUserAddresses(String phone);
    boolean addAddress(Address address);
    boolean updateAddress(Address address);
    boolean deleteAddress(String phone, String detailAddress);
    boolean setDefaultAddress(Long userId, Long addressId);
}