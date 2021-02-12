package com.imooc.pojo.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 前端传过来的json数据统一封装为BO对象
 * 用户新增或修改收货地址的BO
 */
@ApiModel(value = "用户新增或修改收货地址BO",description = "从客户端，由用户传入的数据封装在此实体类中")
public class AddressBO {

    @ApiModelProperty(value = "收货地址ID",name = "addressId",example = "0001",required = true)
    private String addressId;
    @ApiModelProperty(value = "用户ID",name = "userId",example = "0001",required = true)
    private String userId;
    @ApiModelProperty(value = "收件人姓名",name = "receiver",example = "张三",required = true)
    private String receiver;
    @ApiModelProperty(value = "收件人手机号",name = "mobile",example = "13888888888",required = true)
    private String mobile;
    @ApiModelProperty(value = "收货省份",name = "province",example = "北京市",required = true)
    private String province;
    @ApiModelProperty(value = "收货城市",name = "city",example = "北京市",required = true)
    private String city;
    @ApiModelProperty(value = "收货区县",name = "district",example = "东城区",required = true)
    private String district;
    @ApiModelProperty(value = "收货详细地址",name = "detail",example = "朝外北大街18号楼202室",required = true)
    private String detail;

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

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
