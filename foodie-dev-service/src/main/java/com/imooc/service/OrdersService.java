package com.imooc.service;

import com.imooc.pojo.OrderStatus;
import com.imooc.pojo.bo.SubmitOrderBO;
import com.imooc.pojo.vo.OrderVO;

/**
 * 订单相关接口
 */
public interface OrdersService {

    /**
     * 订单创建
     * @param submitOrderBO
     * @return
     */
    OrderVO createOrders(SubmitOrderBO submitOrderBO);

    /**
     * 订单状态修改
     * @param merchantOrderId
     * @param orderStatus
     */
    void updateOrderStatus(String merchantOrderId,Integer orderStatus);

    /**
     * 查询订单状态
     * @param orderId
     * @return
     */
    OrderStatus getOrderStatusInfo(String orderId);

}
