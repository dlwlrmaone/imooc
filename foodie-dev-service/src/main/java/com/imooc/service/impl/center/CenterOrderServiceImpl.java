package com.imooc.service.impl.center;

import com.github.pagehelper.PageHelper;
import com.imooc.enumclass.OrderStatusEnum;
import com.imooc.enumclass.YesOrNo;
import com.imooc.mapper.OrderStatusMapper;
import com.imooc.mapper.OrdersMapper;
import com.imooc.mapper.OrdersMapperCustom;
import com.imooc.pojo.OrderStatus;
import com.imooc.pojo.Orders;
import com.imooc.pojo.vo.MyOrdersVO;
import com.imooc.pojo.vo.center.OrderStatusCountsVO;
import com.imooc.service.center.CenterOrderService;
import com.imooc.service.impl.BaseServiceImpl;
import com.imooc.utils.PagedGridResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * 用户中心展示相关实现
 */
@Service
public class CenterOrderServiceImpl extends BaseServiceImpl implements CenterOrderService{

    @Autowired
    public OrdersMapperCustom ordersMapperCustom;

    @Autowired
    public OrderStatusMapper orderStatusMapper;

    @Autowired
    public OrdersMapper ordersMapper;

    /**
     * 用户中心-订单信息展示
     * @param userId
     * @param orderStatus
     * @param page
     * @param pageSize
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedGridResult getMyOrderInfo(String userId, Integer orderStatus, Integer page, Integer pageSize) {

        HashMap<String, Object> map = new HashMap<>();
        map.put("userId",userId);
        if (orderStatus != null) {
            map.put("orderStatus",orderStatus);
        }
        PageHelper.startPage(page,pageSize);
        List<MyOrdersVO> myOrders = ordersMapperCustom.getMyOrders(map);
        return setPagedGrid(myOrders,page);
    }

    /**
     * 用户中心-修改订单状态 -> 商家发货
     * @param orderId
     */
    @Transactional(propagation=Propagation.REQUIRED)
    @Override
    public void updateDeliverOrderStatus(String orderId) {

        OrderStatus updateOrder = new OrderStatus();
        updateOrder.setOrderStatus(OrderStatusEnum.WAIT_RECEIVE.type);
        updateOrder.setDeliverTime(new Date());

        Example example = new Example(OrderStatus.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orderId", orderId);
        criteria.andEqualTo("orderStatus", OrderStatusEnum.WAIT_DELIVER.type);

        orderStatusMapper.updateByExampleSelective(updateOrder, example);
    }

    /**
     * 订单收货前验证，查询订单
     * @param userId
     * @param orderId
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Orders getMyOrder(String userId, String orderId) {

        Orders orders = new Orders();
        orders.setId(orderId);
        orders.setUserId(userId);
        orders.setIsDelete(YesOrNo.NO.type);

        return ordersMapper.selectOne(orders);
    }

    /**
     * 更新订单状态 -> 确认收货
     * @param orderId
     * @return
     */
    @Transactional(propagation=Propagation.REQUIRED)
    @Override
    public Boolean UpdateReceiveOrderStatus(String orderId) {

        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setOrderStatus(OrderStatusEnum.SUCCESS.type);
        orderStatus.setSuccessTime(new Date());

        Example example = new Example(OrderStatus.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orderId",orderId);
        criteria.andEqualTo("orderStatus",OrderStatusEnum.WAIT_RECEIVE.type);
        int result = orderStatusMapper.updateByExampleSelective(orderStatus, example);
        return result == 1 ? true : false;
    }

    /**
     * 删除订单 -> 逻辑删除
     * @param orderId
     * @param userId
     * @return
     */
    @Transactional(propagation=Propagation.REQUIRED)
    @Override
    public Boolean deleteMyOrders(String orderId, String userId) {

        Orders orders = new Orders();
        orders.setIsDelete(YesOrNo.YES.type);
        orders.setUpdatedTime(new Date());

        Example example = new Example(Orders.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("id",orderId);
        criteria.andEqualTo("userId",userId);
        int result = ordersMapper.updateByExampleSelective(orders, example);
        return result == 1 ? true : false;
    }

    /**
     * 查询用户中心订单状态数
     * @param userId
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public OrderStatusCountsVO getOrderStatusCounts(String userId) {

        HashMap<String, Object> map = new HashMap<>();
        map.put("userId",userId);
        //查询待付款订单数量
        map.put("orderStatus",OrderStatusEnum.WAIT_PAY.type);
        int waitPayCounts = ordersMapperCustom.getMyOrderStatusCounts(map);
        //查询已付款(待发货)订单数量
        map.put("orderStatus",OrderStatusEnum.WAIT_DELIVER.type);
        int waitDeliverCounts = ordersMapperCustom.getMyOrderStatusCounts(map);
        //查询已发货（待收货）订单数量
        map.put("orderStatus",OrderStatusEnum.WAIT_RECEIVE.type);
        int waitReceiveCounts = ordersMapperCustom.getMyOrderStatusCounts(map);
        //查询交易成功（有可能是待评价）订单数量
        map.put("orderStatus",OrderStatusEnum.SUCCESS.type);
        map.put("isComment",YesOrNo.NO.type);
        int waitCommentCounts = ordersMapperCustom.getMyOrderStatusCounts(map);
        OrderStatusCountsVO statusCountsVO = new OrderStatusCountsVO(
                waitPayCounts,waitDeliverCounts,waitReceiveCounts,waitCommentCounts);

        return statusCountsVO;

    }

}
