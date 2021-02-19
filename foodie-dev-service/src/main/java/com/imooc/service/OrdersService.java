package com.imooc.service;

import com.imooc.pojo.bo.SubmitOrderBO;

/**
 * 订单相关接口
 */
public interface OrdersService {

    /**
     * 订单创建
     * @param submitOrderBO
     * @return
     */
    String createOrders(SubmitOrderBO submitOrderBO);

}
