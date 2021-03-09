package com.imooc.service.center;

import com.imooc.utils.PagedGridResult;

/**
 * center用户中心-我的订单
 */
public interface CenterOrderService {

    /**
     * 查询用户订单信息
     * @param userId
     * @return
     */
    PagedGridResult getMyOrderInfo(String userId,Integer orderStatus,Integer page,Integer pageSize);

}
