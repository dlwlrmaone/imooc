package com.imooc.controller;

import com.imooc.enumclass.PayMethodEnum;
import com.imooc.pojo.bo.SubmitOrderBO;
import com.imooc.service.AddressService;
import com.imooc.service.OrdersService;
import com.imooc.utils.CookieUtils;
import com.imooc.utils.IMOOCJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 订单Controller
 */
@Api(value = "订单相关功能",tags = "订单处理功能的相关API接口")
@RestController
@RequestMapping("orders")
public class OrdersController extends BaseController{

    private static final Logger logger = LoggerFactory.getLogger(PassportController.class);

    @Autowired
    private OrdersService ordersService;

    @ApiOperation(value = "订单创建",notes = "创建该用户下的相关订单信息",httpMethod = "POST")
    @PostMapping("/create")
    public IMOOCJSONResult createOrders(@RequestBody SubmitOrderBO submitOrderBO,HttpServletRequest request, HttpServletResponse response){

        System.out.println(submitOrderBO.toString());

        if (!PayMethodEnum.CodeBool(submitOrderBO.getPayMethod())){
            return IMOOCJSONResult.errorMsg("该支付方式不支持！");
        }

        //1.创建订单
        String orderId = ordersService.createOrders(submitOrderBO);
        //2.创建订单后，移除购物车中已提交（结算）的商品
        // TODO 整合redis之后，完善购物车上的库存商品清除，并同步到前端的cookie
//        CookieUtils.setCookie(request,response,FOODIE_SHOPCART,"",true);
        //3.向支付中心发生相关订单信息，用于保存支付中心的订单

        return IMOOCJSONResult.ok(orderId);
    }

}
