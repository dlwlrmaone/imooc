package com.imooc.controller;

import com.imooc.enumclass.YesOrNo;
import com.imooc.pojo.*;
import com.imooc.pojo.vo.CategoryVO;
import com.imooc.pojo.vo.CommentCountsVO;
import com.imooc.pojo.vo.ItemInfoVO;
import com.imooc.pojo.vo.NewItemsVO;
import com.imooc.service.CarouselService;
import com.imooc.service.CategoryService;
import com.imooc.service.ItemService;
import com.imooc.utils.IMOOCJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商品详情页Controller
 */
@Api(value = "商品详情页",tags = "商品详情页展示的相关接口")
@RestController
@RequestMapping("items")
public class ItemsController {

    private static final Logger logger = LoggerFactory.getLogger(PassportController.class);

    @Autowired
    private ItemService itemService;

    @ApiOperation(value = "查询商品详情",notes = "获取商品详情信息",httpMethod = "GET")
    @GetMapping("/info/{itemId}")
    public IMOOCJSONResult getItems(@ApiParam(name = "itemId",value = "商品ID",required = true) @PathVariable String itemId){

        if (itemId == null){
            return IMOOCJSONResult.errorMsg("该商品ID不存在！");
        }
        //查询出商品详情页要展示的所有数据，并且封装到一个VO类中
        Items item = itemService.getItemsById(itemId);
        List<ItemsImg> itemImgList = itemService.getItemImgList(itemId);
        List<ItemsSpec> itemSpecList = itemService.getItemSpecList(itemId);
        ItemsParam itemParam = itemService.getItemParam(itemId);

        ItemInfoVO itemInfoVO = new ItemInfoVO();
        itemInfoVO.setItem(item);
        itemInfoVO.setItemImgList(itemImgList);
        itemInfoVO.setItemSpecList(itemSpecList);
        itemInfoVO.setItemParams(itemParam);

        return IMOOCJSONResult.ok(itemInfoVO);
    }

    @ApiOperation(value = "查询商品评价数",notes = "根据商品id和评价等级获取商品评价数",httpMethod = "GET")
    @GetMapping("/commentLevel")
    public IMOOCJSONResult getCommentCounts(@ApiParam(name = "itemId",value = "商品ID",required = true) @RequestParam String itemId){

        if (itemId == null){
            return IMOOCJSONResult.errorMsg("该商品ID不存在！");
        }
        //查询出商品详情页要展示的所有数据，并且封装到一个VO类中
        CommentCountsVO commentCounts = itemService.getCommentCounts(itemId);

        return IMOOCJSONResult.ok(commentCounts);
    }

}
