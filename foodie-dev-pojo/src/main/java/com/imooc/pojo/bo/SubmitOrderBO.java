package com.imooc.pojo.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 前端传过来的json数据统一封装为BO对象
 * 创建订单信息的BO
 */
@ApiModel(value = "用户创建订单信息BO",description = "从客户端，由用户传入的数据封装在此实体类中")
public class SubmitOrderBO {

    @ApiModelProperty(value = "收货地址ID", name = "addressId", example = "0001", required = true)
    private String addressId;
    @ApiModelProperty(value = "用户ID", name = "userId", example = "0001", required = true)
    private String userId;
    @ApiModelProperty(value = "商品规格ID", name = "itemSpecIds", example = "0002", required = true)
    private String itemSpecIds;
    @ApiModelProperty(value = "支付方式", name = "payMethod", example = "支付宝", required = true)
    private Integer payMethod;
    @ApiModelProperty(value = "买家留言", name = "leftMsg", example = "不要辣椒", required = true)
    private String leftMsg;

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getItemSpecIds() {
        return itemSpecIds;
    }

    public void setItemSpecIds(String itemSpecIds) {
        this.itemSpecIds = itemSpecIds;
    }

    public Integer getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(Integer payMethod) {
        this.payMethod = payMethod;
    }

    public String getLeftMsg() {
        return leftMsg;
    }

    public void setLeftMsg(String leftMsg) {
        this.leftMsg = leftMsg;
    }

    @Override
    public String toString() {
        return "SubmitOrderBO{" +
                "addressId='" + addressId + '\'' +
                ", userId='" + userId + '\'' +
                ", itemSpecIds='" + itemSpecIds + '\'' +
                ", payMethod=" + payMethod +
                ", leftMsg='" + leftMsg + '\'' +
                '}';
    }
}
