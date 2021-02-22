package com.imooc.pojo.vo;

/**
 * 订单嵌套VO
 */
public class OrderVO {

    //商户订单VO
    private MerchantOrderVO merchantOrderVO;
    //订单ID
    private String orderId;

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
