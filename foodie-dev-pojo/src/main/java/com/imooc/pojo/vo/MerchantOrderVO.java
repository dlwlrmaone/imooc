package com.imooc.pojo.vo;

/**
 * 提交给支付中心的订单VO
 */
public class MerchantOrderVO {

    //商户订单号
    private String merchantOrderId;
    //商户系统订单用户ID
    private String merchantUserId;
    //实际支付总金额(包含运费)
    private Integer amount;
    //支付方式
    private Integer payMethod;
    //支付成功后回调商户的地址（由商户提供）
    private String returnUrl;

    public String getMerchantOrderId() {
        return merchantOrderId;
    }

    public void setMerchantOrderId(String merchantOrderId) {
        merchantOrderId = merchantOrderId;
    }

    public String getMerchantUserId() {
        return merchantUserId;
    }

    public void setMerchantUserId(String merchantUserId) {
        merchantUserId = merchantUserId;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(Integer payMethod) {
        this.payMethod = payMethod;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }
}
