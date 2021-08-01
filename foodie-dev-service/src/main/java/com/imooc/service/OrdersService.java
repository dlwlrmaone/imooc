package com.imooc.service;

import com.imooc.pojo.OrderStatus;
import com.imooc.pojo.bo.ShopCartBO;
import com.imooc.pojo.bo.SubmitOrderBO;
import com.imooc.pojo.vo.OrderVO;

import java.util.List;

/**
 * 订单相关接口
 */
public interface OrdersService {

    /**
     * 订单创建
     * @param shopCartList
     * @param submitOrderBO
     * @return
     */
    OrderVO createOrders(List<ShopCartBO> shopCartList,SubmitOrderBO submitOrderBO);

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

    /**
     * 超时未支付订单关闭
     */
    void closeOrder();

}
