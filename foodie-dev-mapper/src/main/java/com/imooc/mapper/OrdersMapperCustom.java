package com.imooc.mapper;

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


}