package com.imooc.pojo.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 前端传过来的json数据统一封装为BO对象
 * 购物车BO
 */
@ApiModel(value = "购物车BO",description = "从客户端，由用户传入的数据封装在此实体类中")
public class ShopCartBO {

    @ApiModelProperty(value = "商品ID",name = "itemId",example = "0001",required = true)
    private String itemId;
    @ApiModelProperty(value = "商品图片路径",name = "itemImgUrl",example = "http://127.0.0.1:8080/iu/iu_0001.jepg",required = true)
    private String itemImgUrl;
    @ApiModelProperty(value = "商品名称",name = "itemName",example = "麻辣味牛肉干",required = true)
    private String itemName;
    @ApiModelProperty(value = "商品规格ID",name = "specId",example = "0005",required = true)
    private String specId;
    @ApiModelProperty(value = "商品规格名称",name = "specName",example = "麻辣味",required = true)
    private String specName;
    @ApiModelProperty(value = "购物车数量",name = "buyCounts",example = "8",required = true)
    private Integer buyCounts;
    @ApiModelProperty(value = "商品优惠价",name = "priceDiscount",example = "188元",required = true)
    private String priceDiscount;
    @ApiModelProperty(value = "商品原价",name = "priceNormal",example = "200元",required = true)
    private String priceNormal;

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemImgUrl() {
        return itemImgUrl;
    }

    public void setItemImgUrl(String itemImgUrl) {
        this.itemImgUrl = itemImgUrl;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getSpecId() {
        return specId;
    }

    public void setSpecId(String specId) {
        this.specId = specId;
    }

    public String getSpecName() {
        return specName;
    }

    public void setSpecName(String specName) {
        this.specName = specName;
    }

    public Integer getBuyCounts() {
        return buyCounts;
    }

    public void setBuyCounts(Integer buyCounts) {
        this.buyCounts = buyCounts;
    }

    public String getPriceDiscount() {
        return priceDiscount;
    }

    public void setPriceDiscount(String priceDiscount) {
        this.priceDiscount = priceDiscount;
    }

    public String getPriceNormal() {
        return priceNormal;
    }

    public void setPriceNormal(String priceNormal) {
        this.priceNormal = priceNormal;
    }
}
