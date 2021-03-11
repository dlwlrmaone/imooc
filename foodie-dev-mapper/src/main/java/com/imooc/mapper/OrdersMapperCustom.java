package com.imooc.mapper;

import com.imooc.pojo.OrderStatus;
import com.imooc.pojo.vo.MyOrdersVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface OrdersMapperCustom {

    /**
     * 获取用户中心我的订单信息
     * @param map
     * @return
     */
    List<MyOrdersVO> getMyOrders(@Param("paramsMap") Map<String, Object> map);

    /**
     * 获取用户中心我的订单状态数量
     * @param map
     * @return
     */
    int getMyOrderStatusCounts(@Param("paramsMap") Map<String,Object> map);

    /**
     * 获取用户中心我的订单动向
     * @param map
     * @return
     */
    List<OrderStatus> getMyOrderTrend(@Param("paramsMap") Map<String,Object> map);
}