package com.imooc.service;

import com.imooc.pojo.Carousel;
import com.imooc.pojo.UserAddress;

import java.util.List;

/**
 * 收货地址相关接口
 */
public interface AddressService {

    /**
     * 获取当前用户下所有收货地址列表
     * @param userId
     * @return
     */
    List<UserAddress> getAllAddress(String userId);

}
