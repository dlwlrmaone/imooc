package com.imooc.service.impl.center;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.imooc.mapper.OrdersMapper;
import com.imooc.mapper.OrdersMapperCustom;
import com.imooc.mapper.UsersMapper;
import com.imooc.pojo.Users;
import com.imooc.pojo.bo.center.CenterUserBO;
import com.imooc.pojo.vo.MyOrdersVO;
import com.imooc.service.BaseService;
import com.imooc.service.center.CenterOrderService;
import com.imooc.service.center.CenterUserService;
import com.imooc.service.impl.BaseServiceImpl;
import com.imooc.utils.PagedGridResult;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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

}
