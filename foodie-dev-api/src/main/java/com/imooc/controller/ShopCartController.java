package com.imooc.controller;

import com.imooc.pojo.bo.ShopCartBO;
import com.imooc.pojo.vo.CommentCountsVO;
import com.imooc.service.ItemService;
import com.imooc.utils.IMOOCJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 商品详情页Controller
 */
@Api(value = "购物车",tags = "购物车功能的相关接口")
@RestController
@RequestMapping("shopcart")
public class ShopCartController extends BaseController{

    private static final Logger logger = LoggerFactory.getLogger(PassportController.class);

    @Autowired
    private ItemService itemService;

    @ApiOperation(value = "同步购物车数据",notes = "同步存储前端cookie上的购物车数据到后端",httpMethod = "GET")
    @GetMapping("/add")
    public IMOOCJSONResult addShopCart(
            @RequestParam String userId, @RequestBody ShopCartBO shopCartBo,
            HttpServletRequest request, HttpServletResponse response){

        if (StringUtils.isBlank(userId)){
            return IMOOCJSONResult.errorMsg("该用户ID不存在！");
        }
        //TODO 用户登录后，前端把购物车数据传过来，后续通过Redis来存储对应购物车数据

        return IMOOCJSONResult.ok();
    }

    @ApiOperation(value = "删除购物车商品",notes = "同步删除用户登录后的购物车商品",httpMethod = "GET")
    @GetMapping("/del")
    public IMOOCJSONResult delShopCart(
            @RequestParam String userId, @RequestBody String itemSpecId,
            HttpServletRequest request, HttpServletResponse response){

        if (StringUtils.isBlank(userId)){
            return IMOOCJSONResult.errorMsg("该用户ID不存在！");
        }
        //TODO 用户登录后，删除购物车商品，通过Redis来同步删除对应购物车数据

        return IMOOCJSONResult.ok();
    }

}
