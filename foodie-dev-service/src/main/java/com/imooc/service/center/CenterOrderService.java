package com.imooc.service.center;

import com.imooc.pojo.Orders;
import com.imooc.utils.PagedGridResult;

/**
 * center用户中心-我的订单
 */
public interface CenterOrderService {

    /**
     * 用户订单信息展示
     * @param userId
     * @return
     */
    PagedGridResult getMyOrderInfo(String userId,Integer orderStatus,Integer page,Integer pageSize);

    /**
     * @Description: 订单状态 --> 商家发货
     */
    void updateDeliverOrderStatus(String orderId);

    /**
     * 查询我的订单 -> 订单确认收货前的验证
     * @param userId
     * @param orderId
     * @return
     */
    Orders getMyOrder(String userId,String orderId);

    /**
     * 更新订单状态 -> 确认收货
     * @param orderId
     * @return
     */
    Boolean UpdateReceiveOrderStatus(String orderId);

    /**
     * 删除订单 -> 逻辑删除
     * @param orderId
     * @return
     */
    Boolean deleteMyOrders(String orderId,String userId);

}
