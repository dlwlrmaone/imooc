package com.imooc.controller;

import com.imooc.pojo.UserAddress;
import com.imooc.service.AddressService;
import com.imooc.utils.IMOOCJSONResult;
import com.imooc.utils.PagedGridResult;
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
@RequestMapping("shopcart")
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

    @ApiOperation(value = "收货地址查询",notes = "查询当前用户下所有收货地址列表",httpMethod = "GET")
    @GetMapping("/list")
    public IMOOCJSONResult getAllAddress(
            @ApiParam(name = "userId",value = "用户ID",required = true) @RequestParam String userId){

        if (StringUtils.isBlank(userId)){
            return IMOOCJSONResult.errorMsg("该商品ID不存在！");
        }
        List<UserAddress> addressList = addressService.getAllAddress(userId);

        return IMOOCJSONResult.ok(addressList);
    }

}
