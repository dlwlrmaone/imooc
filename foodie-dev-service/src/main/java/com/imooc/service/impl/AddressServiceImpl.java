package com.imooc.service.impl;

import com.github.pagehelper.util.StringUtil;
import com.imooc.enumclass.YesOrNo;
import com.imooc.mapper.UserAddressMapper;
import com.imooc.pojo.UserAddress;
import com.imooc.pojo.bo.AddressBO;
import com.imooc.service.AddressService;
import org.apache.commons.lang3.StringUtils;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 收货地址相关实现
 */
@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private UserAddressMapper userAddressMapper;

    @Autowired
    private Sid sid;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<UserAddress> getAllAddress(String userId) {

        UserAddress userAddress = new UserAddress();
        userAddress.setUserId(userId);

        return userAddressMapper.select(userAddress);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void addNewAddress(AddressBO addressBO) {

        //1.判断当前用户是否存在收货地址，如果没有，则新增为默认地址
        Integer isDefault = 0;
        List<UserAddress> allAddress = this.getAllAddress(addressBO.getUserId());
        if (allAddress == null || allAddress.isEmpty() || allAddress.size() == 0){
            isDefault = 1;
        }
        //随机生成UUID
        String addressId = sid.nextShort();
        //2.保存地址到数据库
        UserAddress userAddress = new UserAddress();
        //使用BeanUtils工具类将一个类中的属性复制到另一个实体类中
        BeanUtils.copyProperties(addressBO,userAddress);
        userAddress.setId(addressId);
        userAddress.setIsDefault(isDefault);
        userAddress.setCreatedTime(new Date());
        userAddress.setUpdatedTime(new Date());
        userAddressMapper.insert(userAddress);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateAddress(AddressBO addressBO) {

        String addressId = addressBO.getAddressId();
        UserAddress userAddress = new UserAddress();
        //使用BeanUtils工具类将一个类中的属性复制到另一个实体类中
        BeanUtils.copyProperties(addressBO,userAddress);
        userAddress.setId(addressId);
        userAddress.setUpdatedTime(new Date());
        //使用updateByPrimaryKeySelective方法来避免空属性覆盖掉数据
        userAddressMapper.updateByPrimaryKeySelective(userAddress);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void deleteAddress(String userId,String addressId) {

        UserAddress userAddress = new UserAddress();
        userAddress.setId(addressId);
        userAddress.setUserId(userId);
        userAddressMapper.delete(userAddress);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateDefaultAddress(String userId,String addressId) {

        //1.查找默认地址，设置为不默认
        UserAddress userAddress = new UserAddress();
        userAddress.setIsDefault(YesOrNo.YES.type);
        userAddress.setUserId(userId);
        //正常默认地址均为1个，可以使用selectOne方法，考虑数据库可能有错误数据，使用select方法
        List<UserAddress> addressList = userAddressMapper.select(userAddress);
        for (UserAddress address : addressList) {
            address.setIsDefault(YesOrNo.NO.type);
            userAddressMapper.updateByPrimaryKeySelective(address);
        }
        //2.根据地址ID，修改为默认地址
        UserAddress defaultAddress = new UserAddress();
        defaultAddress.setUserId(userId);
        defaultAddress.setId(addressId);
        defaultAddress.setIsDefault(YesOrNo.YES.type);
        defaultAddress.setUpdatedTime(new Date());

        userAddressMapper.updateByPrimaryKeySelective(defaultAddress);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public UserAddress getUserAddress(String userId, String addressId) {

        UserAddress userAddress = new UserAddress();
        userAddress.setUserId(userId);
        userAddress.setId(addressId);

        return userAddressMapper.selectOne(userAddress);
    }
}
