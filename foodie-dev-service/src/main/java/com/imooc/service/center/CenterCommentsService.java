package com.imooc.service.center;

import com.imooc.pojo.OrderItems;
import com.imooc.pojo.bo.center.CenterCommentBO;
import com.imooc.utils.PagedGridResult;

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

    /**
     * 保存评价信息
     * @param orderId
     * @param userId
     * @param commentList
     */
    void saveComments(String orderId, String userId, List<CenterCommentBO> commentList);

    /**
     * 查询历史评价（分页）
     * @param userId
     * @param page
     * @param pageSize
     * @return
     */
    PagedGridResult getMyComments(String userId,Integer page,Integer pageSize);
}
