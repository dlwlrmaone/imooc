package com.imooc.service;

import com.imooc.pojo.UserAddress;
import com.imooc.pojo.bo.AddressBO;

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

    /**
     * 新增收货地址
     * @param addressBO
     */
    void addNewAddress(AddressBO addressBO);

    /**
     * 修改收货地址
     * @param addressBO
     */
    void updateAddress(AddressBO addressBO);

    /**
     * 删除收货地址
     * @param userId
     * @param addressId
     */
    void deleteAddress(String userId,String addressId);

    /**
     * 修改默认收货地址
     * @param userId
     * @param addressId
     */
    void updateDefaultAddress(String userId,String addressId);

}
