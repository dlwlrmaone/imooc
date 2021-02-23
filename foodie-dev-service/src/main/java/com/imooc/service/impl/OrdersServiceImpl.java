package com.imooc.service.impl;

import com.imooc.enumclass.OrderStatusEnum;
import com.imooc.enumclass.YesOrNo;
import com.imooc.mapper.OrderItemsMapper;
import com.imooc.mapper.OrderStatusMapper;
import com.imooc.mapper.OrdersMapper;
import com.imooc.pojo.*;
import com.imooc.pojo.bo.SubmitOrderBO;
import com.imooc.pojo.vo.MerchantOrderVO;
import com.imooc.pojo.vo.OrderVO;
import com.imooc.service.AddressService;
import com.imooc.service.ItemService;
import com.imooc.service.OrdersService;
import com.imooc.utils.DateUtil;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 收货地址相关实现
 */
@Service
public class OrdersServiceImpl implements OrdersService {

    @Autowired
    private OrdersMapper ordersMapper;

    @Autowired
    private OrderItemsMapper orderItemsMapper;

    @Autowired
    private OrderStatusMapper orderStatusMapper;

    @Autowired
    private AddressService addressService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private Sid sid;

    /**
     * 订单创建
     * @param submitOrderBO
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public OrderVO createOrders(SubmitOrderBO submitOrderBO) {

        String userId = submitOrderBO.getUserId();
        String addressId = submitOrderBO.getAddressId();
        Integer payMethod = submitOrderBO.getPayMethod();
        String itemSpecIds = submitOrderBO.getItemSpecIds();
        String leftMsg = submitOrderBO.getLeftMsg();
        //包邮费用设置为0
        Integer postAmount = 0;
        //商品原价累计
        Integer totalAmount = 0;
        //商品实际价格累计
        Integer realPayAmount = 0;

        String orderId = sid.nextShort();
        UserAddress userAddress = addressService.getUserAddress(userId, addressId);

        //1.新订单数据保存
        Orders newOrders = new Orders();
        newOrders.setId(orderId);
        newOrders.setPayMethod(payMethod);
        newOrders.setLeftMsg(leftMsg);
        newOrders.setUserId(userId);
        newOrders.setReceiverAddress(userAddress.getProvince() + " " + userAddress.getCity()
                + " " + userAddress.getDistrict() + " " + userAddress.getDetail());
        newOrders.setReceiverMobile(userAddress.getMobile());
        newOrders.setReceiverName(userAddress.getReceiver());
        newOrders.setPostAmount(postAmount);
        newOrders.setIsComment(YesOrNo.NO.type);
        newOrders.setIsDelete(YesOrNo.NO.type);
        newOrders.setCreatedTime(new Date());
        newOrders.setUpdatedTime(new Date());

        //2.循环根据itemSpecIds保存订单商品信息表
        String[] specIds = itemSpecIds.split(",");
        for (String itemSpecId : specIds) {
            //TODO 整合redis后，商品购买后的数量重新从redis的购物车中获取
            Integer buyCounts = 1;

            //根据规格id，查询规格的具体信息，主要是价格
            ItemsSpec itemsSpec = itemService.getItemByItemSpecId(itemSpecId);
            totalAmount += itemsSpec.getPriceNormal() * buyCounts;
            realPayAmount += itemsSpec.getPriceDiscount() * buyCounts;

            //根据商品ID，获取商品图片以及商品信息
            String itemId = itemsSpec.getItemId();
            Items items = itemService.getItemsById(itemId);
            String mainImgUrl = itemService.getItemMainImgById(itemId);

            //循环保存子订单数据到数据库
            String subOrderId = sid.nextShort();
            OrderItems orderItems = new OrderItems();
            orderItems.setBuyCounts(buyCounts);
            orderItems.setId(subOrderId);
            orderItems.setItemId(itemId);
            orderItems.setItemImg(mainImgUrl);
            orderItems.setItemName(items.getItemName());
            orderItems.setItemSpecId(itemSpecId);
            orderItems.setPrice(itemsSpec.getPriceDiscount());
            orderItems.setOrderId(orderId);
            orderItems.setItemSpecName(itemsSpec.getName());
            orderItemsMapper.insert(orderItems);

            //用户提交订单后，商品规格表中扣除库存商品数量
            itemService.reduceItemStock(itemSpecId,buyCounts);
        }
        newOrders.setTotalAmount(totalAmount);
        newOrders.setRealPayAmount(realPayAmount);
        ordersMapper.insert(newOrders);

        //3.保存订单信息状态表
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setOrderId(orderId);
        orderStatus.setOrderStatus(OrderStatusEnum.WAIT_PAY.type);
        orderStatus.setCreatedTime(new Date());
        orderStatusMapper.insert(orderStatus);

        //4.构建商户订单，将订单信息传给支付中心
        MerchantOrderVO merchantOrderVO = new MerchantOrderVO();
        merchantOrderVO.setMerchantOrderId(orderId);
        merchantOrderVO.setPayMethod(payMethod);
        merchantOrderVO.setAmount(realPayAmount + postAmount);
        merchantOrderVO.setMerchantUserId(userId);

        //5.构建自定义订单VO
        OrderVO orderVO = new OrderVO();
        orderVO.setMerchantOrderVO(merchantOrderVO);
        orderVO.setOrderId(orderId);

        return orderVO;
    }

    /**
     * 订单状态修改
     * @param merchantOrderId
     * @param orderStatus
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateOrderStatus(String merchantOrderId, Integer orderStatus) {

        OrderStatus paidStatus = new OrderStatus();
        paidStatus.setOrderId(merchantOrderId);
        paidStatus.setOrderStatus(orderStatus);
        paidStatus.setPayTime(new Date());

        orderStatusMapper.updateByPrimaryKeySelective(paidStatus);

    }

    /**
     * 查询订单状态
     * @param orderId
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public OrderStatus getOrderStatusInfo(String orderId) {
        return orderStatusMapper.selectByPrimaryKey(orderId);
    }

    /**
     * 超时未支付订单关闭
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void closeOrder() {

        //查询所有未支付订单，判断是否超时（1天）
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setOrderStatus(OrderStatusEnum.WAIT_PAY.type);
        List<OrderStatus> statusList = orderStatusMapper.select(orderStatus);
        for (OrderStatus os : statusList) {
            //获取订单创建时间
            Date createdTime = os.getCreatedTime();
            //和当前时间对比，使用DataUtil中的daysBetween方法
            int days = DateUtil.daysBetween(createdTime, new Date());
            if (days >= 1){
                doCloseOrder(os.getOrderId());
            }
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void doCloseOrder(String orderId){

        OrderStatus status = new OrderStatus();
        status.setOrderId(orderId);
        status.setOrderStatus(OrderStatusEnum.CLOSE.type);
        status.setCloseTime(new Date());
        orderStatusMapper.updateByPrimaryKeySelective(status);
    }
}
