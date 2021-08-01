package com.imooc.controller;

import com.imooc.enumclass.OrderStatusEnum;
import com.imooc.enumclass.PayMethodEnum;
import com.imooc.pojo.OrderStatus;
import com.imooc.pojo.bo.ShopCartBO;
import com.imooc.pojo.bo.SubmitOrderBO;
import com.imooc.pojo.vo.MerchantOrderVO;
import com.imooc.pojo.vo.OrderVO;
import com.imooc.service.OrdersService;
import com.imooc.utils.IMOOCJSONResult;
import com.imooc.utils.JsonUtils;
import com.imooc.utils.RedisOperator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

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

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RedisOperator redisOperator;

    @ApiOperation(value = "订单创建",notes = "创建该用户下的相关订单信息",httpMethod = "POST")
    @PostMapping("/create")
    public IMOOCJSONResult createOrders(
            @ApiParam(name = "submitOrderBO",value = "订单提交BO",required = true)
            @RequestBody SubmitOrderBO submitOrderBO,HttpServletRequest request, HttpServletResponse response){

        System.out.println(submitOrderBO.toString());

        if (!PayMethodEnum.CodeBool(submitOrderBO.getPayMethod())){
            return IMOOCJSONResult.errorMsg("该支付方式不支持！");
        }

        String shopCartJson = redisOperator.get(FOODIE_SHOPCART + ":" + submitOrderBO.getUserId());
        if (StringUtils.isBlank(shopCartJson)){
            return IMOOCJSONResult.errorMsg("购物车数据不正确！");
        }
        List<ShopCartBO> shopCartList = JsonUtils.jsonToList(shopCartJson, ShopCartBO.class);

        //1.创建订单
        OrderVO orderVO = ordersService.createOrders(shopCartList, submitOrderBO);

        //2.创建订单后，移除购物车中已提交（结算）的商品
        // TODO 整合redis之后，完善购物车上的库存商品清除，并同步到前端的cookie
//        CookieUtils.setCookie(request,response,FOODIE_SHOPCART,"",true);

        //3.向支付中心发生相关订单信息，用于保存支付中心的订单
        MerchantOrderVO merchantOrderVO = orderVO.getMerchantOrderVO();
        merchantOrderVO.setReturnUrl(payReturnUrl);

        HttpHeaders httpHeaders = new HttpHeaders();
        //设置http请求头为application/json
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.add("imoocUserId","imooc");
        httpHeaders.add("password","imooc");
        HttpEntity<MerchantOrderVO> httpEntity = new HttpEntity<>(merchantOrderVO,httpHeaders);
        ResponseEntity<IMOOCJSONResult> responseEntity = restTemplate.postForEntity(payUrl, httpEntity, IMOOCJSONResult.class);
        IMOOCJSONResult payResult = responseEntity.getBody();
        if (payResult.getStatus() != 200){
            logger.error("发送错误{}",payResult.getMsg());
            return IMOOCJSONResult.errorMsg("支付中心订单创建失败，请联系订单中心管理员！");
        }

        return IMOOCJSONResult.ok(orderVO);
    }

    @ApiOperation(value = "支付后，订单状态修改",notes = "支付成功后，修改订单状态为待发货",httpMethod = "POST")
    @PostMapping("notifyMerchantOrderPaid")
    public Integer notifyMerchantOrderPaid(
            @ApiParam(name = "merchantOrderId",value = "商家订单ID",required = true)@RequestParam String merchantOrderId){

        ordersService.updateOrderStatus(merchantOrderId, OrderStatusEnum.WAIT_DELIVER.type);

        return HttpStatus.OK.value();
    }

    @ApiOperation(value = "支付状态轮询查询",notes = "支付后，订单支付状态轮询查询",httpMethod = "POST")
    @PostMapping("getPaidOrderInfo")
    public IMOOCJSONResult getPaidOrderInfo(
            @ApiParam(name = "orderId",value = "订单ID",required = true)@RequestParam String orderId){

        OrderStatus orderStatusInfo = ordersService.getOrderStatusInfo(orderId);
        return IMOOCJSONResult.ok(orderStatusInfo);
    }

}
