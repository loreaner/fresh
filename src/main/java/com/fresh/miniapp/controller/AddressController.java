package com.fresh.miniapp.controller;

import com.fresh.common.response.Result;
import com.fresh.core.entity.Address;
import com.fresh.core.service.AddressService;
import com.fresh.miniapp.dto.AddressDetailRequest;
import com.fresh.miniapp.dto.AddressListRequest;
import com.fresh.miniapp.dto.AddressRequest;

import com.fresh.miniapp.dto.DeleteAddressRequest;
import com.fresh.miniapp.dto.SetDefaultAddressRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 小程序地址管理控制器
 */
@RestController
@RequestMapping("/miniapp/address")
public class AddressController {

    @Resource
    private AddressService addressService;

    /**
     * 获取用户地址列表
     */
    @PostMapping("/list")
    public Result<List<Address>> getAddressList(@RequestBody AddressListRequest request) {
        List<Address> addresses = addressService.getUserAddresses(request.getUserId());

        return Result.success(addresses);
    }

    /**
     * 获取地址详情
     */
    @PostMapping("/detail")
    public Result getAddressDetail(@RequestBody AddressDetailRequest request) {
        Address address = addressService.getAddressDetail(request.getId());
        if (address == null) {
            return Result.error("地址不存在");
        }
        

        return Result.success(address);
    }

    /**
     * 添加地址
     */
    @PostMapping("/add")
    public Result<Void> addAddress(@RequestBody AddressRequest request) {
        Address address = new Address();
        BeanUtils.copyProperties(request, address);
        
        boolean success = addressService.addAddress(address);
        return success ? Result.success() : Result.error("添加失败");
    }

    /**
     * 更新地址
     */
    @PostMapping("/update")
    public Result<Void> updateAddress(@RequestBody AddressRequest request) {
        if (request.getUserId() == null) {
            return Result.error("地址ID不能为空");
        }
        
        Address address = new Address();
        BeanUtils.copyProperties(request, address);
        
        boolean success = addressService.updateAddress(address);
        return success ? Result.success() : Result.error("更新失败");
    }

    /**
     * 删除地址
     */
    @PostMapping("/delete")
    public Result<Void> deleteAddress(@RequestBody DeleteAddressRequest request) {
        boolean success = addressService.deleteAddress(request.getId());
        return success ? Result.success() : Result.error("删除失败");
    }

    /**
     * 设置默认地址
     */
    @PostMapping("/default")
    public Result<Void> setDefaultAddress(@RequestBody SetDefaultAddressRequest request) {
        boolean success = addressService.setDefaultAddress(request.getUserId(), request.getAddressId());
        return success ? Result.success() : Result.error("设置失败");
    }
}