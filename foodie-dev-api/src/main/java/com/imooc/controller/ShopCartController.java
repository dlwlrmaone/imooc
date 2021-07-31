package com.imooc.controller;

import com.imooc.pojo.bo.ShopCartBO;
import com.imooc.utils.IMOOCJSONResult;
import com.imooc.utils.JsonUtils;
import com.imooc.utils.RedisOperator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 购物车Controller
 */
@Api(value = "购物车",tags = "购物车功能的相关接口")
@RestController
@RequestMapping("shopcart")
public class ShopCartController extends BaseController{

    private static final Logger logger = LoggerFactory.getLogger(PassportController.class);

    @Autowired
    private RedisOperator redisOperator;

    @ApiOperation(value = "同步购物车数据",notes = "同步存储前端cookie上的购物车数据到后端",httpMethod = "GET")
    @PostMapping("/add")
    public IMOOCJSONResult addShopCart(
            @RequestParam String userId, @RequestBody ShopCartBO shopCartBo,
            HttpServletRequest request, HttpServletResponse response){

        if (StringUtils.isBlank(userId)){
            return IMOOCJSONResult.errorMsg("该用户ID不存在！");
        }
        //用户登录后，前端把购物车数据传过来，后续通过Redis来存储对应购物车数据
        String shopCartJson = redisOperator.get(FOODIE_SHOPCART + ":" + userId);
        List<ShopCartBO> shopCartBOList;
        //redis中已经有购物车
        if (StringUtils.isNotBlank(shopCartJson)){
            shopCartBOList = JsonUtils.jsonToList(shopCartJson,ShopCartBO.class);
            //判断购物车中是否存在已有商品，如果有就累加
            boolean isHaving = false;
            for (ShopCartBO sc : shopCartBOList) {
                String tmpSpecId = sc.getSpecId();
                //存在已有商品，进行数量累加
                if (tmpSpecId.equals(shopCartBo.getSpecId())){
                    sc.setBuyCounts(sc.getBuyCounts() + shopCartBo.getBuyCounts());
                    isHaving = true;
                }
            }
            //购物车不包含该商品
            if (!isHaving){
                shopCartBOList.add(shopCartBo);
            }
        }else {
            //redis没有购物车
            shopCartBOList = new ArrayList<>();
            //直接添加到购物车
            shopCartBOList.add(shopCartBo);
        }

        //覆盖现有redis中的购物车
        redisOperator.set(FOODIE_SHOPCART + ":" + userId,JsonUtils.objectToJson(shopCartBOList));

        return IMOOCJSONResult.ok();
    }

    @ApiOperation(value = "删除购物车商品",notes = "同步删除用户登录后的购物车商品",httpMethod = "GET")
    @PostMapping("/del")
    public IMOOCJSONResult delShopCart(
            @RequestParam String userId, @RequestParam String itemSpecId,
            HttpServletRequest request, HttpServletResponse response){

        if (StringUtils.isBlank(userId)){
            return IMOOCJSONResult.errorMsg("该用户ID不存在！");
        }
        //用户登录后，删除购物车商品，通过Redis来同步删除对应购物车数据
        String shopCartJson = redisOperator.get(FOODIE_SHOPCART + ":" + userId);
        //redis中已经有购物车
        if (StringUtils.isNotBlank(shopCartJson)){
            List<ShopCartBO> shopCartBOList = JsonUtils.jsonToList(shopCartJson,ShopCartBO.class);
            for (ShopCartBO sc : shopCartBOList) {
                String tmpSpecId = sc.getSpecId();
                //存在已有商品，进行删除
                if (tmpSpecId.equals(itemSpecId)){
                    shopCartBOList.remove(sc);
                    break;
                }
            }
            //覆盖现有redis中的购物车
            redisOperator.set(FOODIE_SHOPCART + ":" + userId,JsonUtils.objectToJson(shopCartBOList));
        }

        return IMOOCJSONResult.ok();
    }

}
