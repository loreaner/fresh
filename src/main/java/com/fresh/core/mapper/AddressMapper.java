package com.fresh.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fresh.core.entity.Address;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AddressMapper extends BaseMapper<Address> {
}