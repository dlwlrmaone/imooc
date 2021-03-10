package com.imooc.service.center;

import com.imooc.pojo.OrderItems;

import java.util.List;

/**
 * center用户中心-我的评论
 */
public interface CenterCommentsService {

    /**
     * 用户中心-查询待评论商品
     * @param orderId
     * @return
     */
    List<OrderItems> getPendingComments(String orderId);
}
