package com.fresh.core.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.fresh.core.entity.Address;

import java.util.List;

public interface AddressService extends IService<Address> {
    List<Address> getUserAddresses(Long userId);
    Address getAddressDetail(Long id);
    boolean addAddress(Address address);
    boolean updateAddress(Address address);
    boolean deleteAddress(Long id);
    boolean setDefaultAddress(Long userId, Long addressId);
}