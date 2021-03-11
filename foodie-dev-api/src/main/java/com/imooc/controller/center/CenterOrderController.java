package com.imooc.controller.center;

import com.imooc.controller.BaseController;
import com.imooc.pojo.vo.center.OrderStatusCountsVO;
import com.imooc.service.center.CenterOrderService;
import com.imooc.utils.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * 用户中心-订单信息Controller
 */
@Api(value = "center-用户中心-订单信息",tags = "用户中心-订单信息页面的相关接口")
@RestController
@RequestMapping("myOrders")
public class CenterOrderController extends BaseController {

    @Autowired
    public CenterOrderService centerOrderService;

    @ApiOperation(value = "用户中心-查询订单",notes = "根据用户id获取订单列表信息",httpMethod = "POST")
    @PostMapping("/query")
    public IMOOCJSONResult getOrders(
            @ApiParam(name = "userId",value = "用户ID",required = true) @RequestParam String userId,
            @ApiParam(name = "orderStatus",value = "订单状态",required = false) @RequestParam Integer orderStatus,
            @ApiParam(name = "page",value = "查询下一页的第几页",required = false) @RequestParam Integer page,
            @ApiParam(name = "pageSize",value = "每一页显示的记录数",required = false) @RequestParam Integer pageSize){

        if (StringUtils.isBlank(userId)){
            return IMOOCJSONResult.errorMsg("该用户ID不存在！");
        }
        if (page == null){
            page = COMMON_PAGE;
        }
        if (pageSize == null){
            pageSize = COMMON_PAGE_SIZE;
        }
        PagedGridResult orderInfo = centerOrderService.getMyOrderInfo(userId,orderStatus,page,pageSize);

        return IMOOCJSONResult.ok(orderInfo);
    }

    //商家发货没有后端对接，该接口仅为模拟
    @ApiOperation(value = "用户中心-商家发货",notes = "模拟商家发货功能",httpMethod = "GET")
    @GetMapping("/deliver")
    public IMOOCJSONResult deliver(@ApiParam(name = "orderId", value = "订单id", required = true) @RequestParam String orderId) {

        if (StringUtils.isBlank(orderId)) {
            return IMOOCJSONResult.errorMsg("订单ID不能为空");
        }
        centerOrderService.updateDeliverOrderStatus(orderId);
        return IMOOCJSONResult.ok();
    }

    @ApiOperation(value = "用户中心-确认收货",notes = "用户确认收货功能",httpMethod = "POST")
    @PostMapping("/confirmReceive")
    public IMOOCJSONResult confirmReceive(
            @ApiParam(name = "orderId", value = "订单id", required = true) @RequestParam String orderId,
            @ApiParam(name = "userId", value = "用户id", required = true) @RequestParam String userId) {

        IMOOCJSONResult checkUserOrder = checkUserOrder(userId, orderId);
        if (checkUserOrder.getStatus() != HttpStatus.OK.value()){
            return checkUserOrder;
        }

        Boolean aBoolean = centerOrderService.UpdateReceiveOrderStatus(orderId);
        if (!aBoolean){
            return IMOOCJSONResult.errorMsg("订单确认收货失败，请联系商户管理员核实处理！");
        }

        return IMOOCJSONResult.ok();
    }

    @ApiOperation(value = "用户中心-订单删除",notes = "用户删除订单功能",httpMethod = "POST")
    @PostMapping("/delete")
    public IMOOCJSONResult deleteOrders(
            @ApiParam(name = "orderId", value = "订单id", required = true) @RequestParam String orderId,
            @ApiParam(name = "userId", value = "用户id", required = true) @RequestParam String userId) {

        IMOOCJSONResult checkUserOrder = checkUserOrder(userId, orderId);
        if (checkUserOrder.getStatus() != HttpStatus.OK.value()){
            return checkUserOrder;
        }

        Boolean aBoolean = centerOrderService.deleteMyOrders(orderId, userId);
        if (!aBoolean){
            return IMOOCJSONResult.errorMsg("订单删除失败，请联系商户管理员核实处理！");
        }

        return IMOOCJSONResult.ok();
    }

    @ApiOperation(value = "用户中心-查询订单状态数",notes = "获取订单状态数，并且展示",httpMethod = "POST")
    @PostMapping("/statusCounts")
    public IMOOCJSONResult getOrderStatusCounts(
            @ApiParam(name = "userId", value = "用户id", required = true) @RequestParam String userId) {

        if (StringUtils.isBlank(userId)) {
            return IMOOCJSONResult.errorMsg("用户ID不能为空");
        }

        OrderStatusCountsVO statusCounts = centerOrderService.getOrderStatusCounts(userId);
        return IMOOCJSONResult.ok(statusCounts);
    }

}
