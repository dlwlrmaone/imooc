package com.imooc.service.impl.center;

import com.github.pagehelper.PageHelper;
import com.imooc.enumclass.YesOrNo;
import com.imooc.mapper.*;
import com.imooc.pojo.OrderItems;
import com.imooc.pojo.OrderStatus;
import com.imooc.pojo.Orders;
import com.imooc.pojo.bo.center.CenterCommentBO;
import com.imooc.pojo.vo.center.CenterCommentVO;
import com.imooc.service.BaseService;
import com.imooc.service.center.CenterCommentsService;
import com.imooc.service.impl.BaseServiceImpl;
import com.imooc.utils.PagedGridResult;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * 用户中心-我的订单相关实现
 */
@Service
public class CenterCommentsServiceImpl extends BaseServiceImpl implements CenterCommentsService {

    @Autowired
    public OrderItemsMapper orderItemsMapper;

    @Autowired
    public Sid sid;

    @Autowired
    public ItemsCommentsMapperCustom itemsCommentsMapperCustom;

    @Autowired
    public OrdersMapper ordersMapper;

    @Autowired
    public OrderStatusMapper orderStatusMapper;

    /**
     * 用户中心-查询待评论商品
     * @param orderId
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<OrderItems> getPendingComments(String orderId) {

        OrderItems orderItems = new OrderItems();
        orderItems.setOrderId(orderId);
        return orderItemsMapper.select(orderItems);
    }

    /**
     * 保存评价信息
     * @param orderId
     * @param userId
     * @param commentList
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void saveComments(String orderId, String userId, List<CenterCommentBO> commentList) {

        //1.保存评价到items_comments评价表中
        for (CenterCommentBO commentBO: commentList) {
            commentBO.setCommentId(sid.nextShort());
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("userId",userId);
        map.put("commentList",commentList);
        itemsCommentsMapperCustom.saveComments(map);
        //2.修改orders订单表，改为已评价
        Orders orders = new Orders();
        orders.setId(orderId);
        orders.setIsComment(YesOrNo.YES.type);
        ordersMapper.updateByPrimaryKeySelective(orders);
        //3.更新order_status订单状态表，更新评价时间
        OrderStatus status = new OrderStatus();
        status.setOrderId(orderId);
        status.setCommentTime(new Date());
        orderStatusMapper.updateByPrimaryKeySelective(status);
    }

    /**
     * 查询历史评价（分页）
     * @param userId
     * @param page
     * @param pageSize
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedGridResult getMyComments(String userId, Integer page, Integer pageSize) {

        HashMap<String, Object> map = new HashMap<>();
        map.put("userId",userId);
        PageHelper.startPage(page,pageSize);
        List<CenterCommentVO> myComments = itemsCommentsMapperCustom.getMyComments(map);
        return setPagedGrid(myComments,page);
    }
}
