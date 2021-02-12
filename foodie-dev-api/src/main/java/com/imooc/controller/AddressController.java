package com.imooc.controller;

import com.imooc.pojo.UserAddress;
import com.imooc.pojo.bo.AddressBO;
import com.imooc.service.AddressService;
import com.imooc.utils.IMOOCJSONResult;
import com.imooc.utils.MobileEmailUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 收货地址Controller
 */
@Api(value = "收货地址功能",tags = "收货地址功能的相关接口")
@RestController
@RequestMapping("address")
public class AddressController extends BaseController{

    private static final Logger logger = LoggerFactory.getLogger(PassportController.class);

    @Autowired
    private AddressService addressService;

    /**
     * 用户在确认订单页面，可以针对收获地址做如下操作
     * 1.获取到该用户下所有的收货地址列表
     * 2.新增收货地址
     * 3.修改收货地址
     * 4.删除收货地址
     * 5.设置默认收货地址
     */

    @ApiOperation(value = "收货地址查询",notes = "查询当前用户下所有收货地址列表",httpMethod = "POST")
    @PostMapping("/list")
    public IMOOCJSONResult getAllAddress(
            @ApiParam(name = "userId",value = "用户ID",required = true) @RequestParam String userId){

        if (StringUtils.isBlank(userId)){
            return IMOOCJSONResult.errorMsg("该商品ID不存在！");
        }
        List<UserAddress> addressList = addressService.getAllAddress(userId);

        return IMOOCJSONResult.ok(addressList);
    }

    @ApiOperation(value = "收货地址新增",notes = "当前用户下收货地址新增，如果没有，设置为默认地址",httpMethod = "POST")
    @PostMapping("/add")
    public IMOOCJSONResult addAddress(
            @ApiParam(name = "addressBO",value = "用户地址信息BO",required = true) @RequestBody AddressBO addressBO){

        IMOOCJSONResult result = checkAddress(addressBO);
        if (result.getStatus() != 200){
            return result;
        }
        addressService.addNewAddress(addressBO);

        return IMOOCJSONResult.ok();
    }

    @ApiOperation(value = "收货地址修改",notes = "当前用户下收货地址修改",httpMethod = "POST")
    @PostMapping("/update")
    public IMOOCJSONResult updateAddress(
            @ApiParam(name = "addressBO",value = "用户地址信息BO",required = true) @RequestBody AddressBO addressBO){

        if (StringUtils.isBlank(addressBO.getAddressId())){
            return IMOOCJSONResult.errorMsg("待修改地址不存在！");
        }
        IMOOCJSONResult result = checkAddress(addressBO);
        if (result.getStatus() != 200){
            return result;
        }
        addressService.updateAddress(addressBO);

        return IMOOCJSONResult.ok();
    }

    @ApiOperation(value = "收货地址删除",notes = "当前用户下收货地址删除",httpMethod = "POST")
    @PostMapping("/delete")
    public IMOOCJSONResult updateAddress(
            @ApiParam(name = "userId",value = "用户ID",required = true) @RequestParam String userId,
            @ApiParam(name = "addressId",value = "用户地址ID",required = true) @RequestParam String addressId){

        //加判断已防止空数据到达数据库，对数据库造成压力
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(addressId)){
            return IMOOCJSONResult.errorMsg("该用户或待删除地址不存在！");
        }

        addressService.deleteAddress(userId,addressId);

        return IMOOCJSONResult.ok();
    }

    @ApiOperation(value = "修改默认收货地址",notes = "当前用户下修改默认收货地址",httpMethod = "POST")
    @PostMapping("/setDefault")
    public IMOOCJSONResult updateDefaultAddress(
            @ApiParam(name = "userId",value = "用户ID",required = true) @RequestParam String userId,
            @ApiParam(name = "addressId",value = "用户地址ID",required = true) @RequestParam String addressId){

        //加判断已防止空数据到达数据库，对数据库造成压力
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(addressId)){
            return IMOOCJSONResult.errorMsg("该用户或待修改默认地址不存在！");
        }

        addressService.updateDefaultAddress(userId,addressId);

        return IMOOCJSONResult.ok();
    }

    private IMOOCJSONResult checkAddress(AddressBO addressBO){

        String receiver = addressBO.getReceiver();
        if (StringUtils.isBlank(receiver)){
            return IMOOCJSONResult.errorMsg("收货人不能为空");
        }
        if (receiver.length() > 18){
            return IMOOCJSONResult.errorMsg("收货人姓名不能超过18位！！");
        }

        String mobile = addressBO.getMobile();
        if (StringUtils.isBlank(mobile)){
            return IMOOCJSONResult.errorMsg("收货人手机号码不能为空！");
        }
        if (mobile.length() != 11){
            return IMOOCJSONResult.errorMsg("收货人手机号码长度不正确！");
        }
        if (!MobileEmailUtils.checkMobileIsOk(mobile)){
            return IMOOCJSONResult.errorMsg("收货人手机号码格式不正确！");
        }
        if (StringUtils.isBlank(addressBO.getProvince())
                || StringUtils.isBlank(addressBO.getCity())
                || StringUtils.isBlank(addressBO.getDistrict())
                || StringUtils.isBlank(addressBO.getDetail())){
            return IMOOCJSONResult.errorMsg("收货人收货地址信息不能为空！");
        }

        return IMOOCJSONResult.ok();
    }

}
