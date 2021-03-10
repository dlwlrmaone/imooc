package com.imooc.controller.center;

import com.imooc.controller.BaseController;
import com.imooc.enumclass.YesOrNo;
import com.imooc.pojo.OrderItems;
import com.imooc.pojo.Orders;
import com.imooc.service.center.CenterCommentsService;
import com.imooc.service.center.CenterOrderService;
import com.imooc.utils.IMOOCJSONResult;
import com.imooc.utils.PagedGridResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户中心-评价信息Controller
 */
@Api(value = "center-用户中心-评价信息",tags = "用户中心-评价信息页面的相关接口")
@RestController
@RequestMapping("myComments")
public class CenterCommentsController extends BaseController {

    @Autowired
    public CenterCommentsService centerCommentsService;

    @ApiOperation(value = "用户中心-查询待评价商品",notes = "根据用户id获取待评价商品",httpMethod = "POST")
    @PostMapping("/pending")
    public IMOOCJSONResult getComments(
            @ApiParam(name = "userId",value = "用户ID",required = true) @RequestParam String userId,
            @ApiParam(name = "orderId",value = "订单ID",required = true) @RequestParam String orderId){

        //判断订单与用户是否关联
        IMOOCJSONResult checkUserOrder = checkUserOrder(userId, orderId);
        if (checkUserOrder.getStatus() != HttpStatus.OK.value()){
            return checkUserOrder;
        }

        //判断该订单是否已经评价
        Orders orders = (Orders)checkUserOrder.getData();
        if (orders.getIsComment() == YesOrNo.YES.type){
            return IMOOCJSONResult.errorMsg("该订单已评价");
        }
        List<OrderItems> pendingComments = centerCommentsService.getPendingComments(orderId);

        return IMOOCJSONResult.ok(pendingComments);
    }

}
