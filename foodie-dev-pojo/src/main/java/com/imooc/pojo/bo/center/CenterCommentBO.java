package com.imooc.pojo.bo.center;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 前端传过来的json数据统一封装为BO对象
 * 用户中心-评价BO
 */
@ApiModel(value = "用户中心-用户对象BO",description = "从客户端，由用户传入的数据封装在此实体类中")
public class CenterCommentBO {

    @ApiModelProperty(value = "评价主键ID",name = "commentId",example = "001",required = true)
    private String commentId;
    @ApiModelProperty(value = "商品ID",name = "itemId",example = "123456",required = true)
    private String itemId;
    @ApiModelProperty(value = "商品名称",name = "itemName",example = "芒果",required = true)
    private String itemName;
    @ApiModelProperty(value = "商品规格ID",name = "itemSpecId",example = "002",required = true)
    private String itemSpecId;
    @ApiModelProperty(value = "商品规格名称",name = "itemSpecName",example = "李宁",required = true)
    private String itemSpecName;
    @ApiModelProperty(value = "评价等级",name = "commentLevel",example = "1",required = true)
    private Integer commentLevel;
    @ApiModelProperty(value = "评价内容",name = "content",example = "芒果好好吃",required = true)
    private String content;

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemSpecId() {
        return itemSpecId;
    }

    public void setItemSpecId(String itemSpecId) {
        this.itemSpecId = itemSpecId;
    }

    public String getItemSpecName() {
        return itemSpecName;
    }

    public void setItemSpecName(String itemSpecName) {
        this.itemSpecName = itemSpecName;
    }

    public Integer getCommentLevel() {
        return commentLevel;
    }

    public void setCommentLevel(Integer commentLevel) {
        this.commentLevel = commentLevel;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
