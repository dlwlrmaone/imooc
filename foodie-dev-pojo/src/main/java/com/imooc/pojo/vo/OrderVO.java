package com.imooc.pojo.vo;

import com.imooc.pojo.bo.ShopCartBO;

import java.util.ArrayList;
import java.util.List;

/**
 * 订单嵌套VO
 */
public class OrderVO {

    //商户订单VO
    private MerchantOrderVO merchantOrderVO;
    //订单ID
    private String orderId;
    //待删除购物车列表
    private List<ShopCartBO> removedShopCartList;

    public List<ShopCartBO> getRemovedShopCartList() {
        return removedShopCartList;
    }

    public void setRemovedShopCartList(List<ShopCartBO> removedShopCartList) {
        this.removedShopCartList = removedShopCartList;
    }

    public MerchantOrderVO getMerchantOrderVO() {
        return merchantOrderVO;
    }

    public void setMerchantOrderVO(MerchantOrderVO merchantOrderVO) {
        this.merchantOrderVO = merchantOrderVO;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
