package com.imooc.controller;

import com.imooc.pojo.*;
import com.imooc.pojo.vo.CommentCountsVO;
import com.imooc.pojo.vo.ItemInfoVO;
import com.imooc.service.ItemService;
import com.imooc.utils.IMOOCJSONResult;
import com.imooc.utils.PagedGridResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
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
public class ItemsController extends BaseController{

    private static final Logger logger = LoggerFactory.getLogger(PassportController.class);

    @Autowired
    private ItemService itemService;

    @ApiOperation(value = "查询商品详情",notes = "获取商品详情信息",httpMethod = "GET")
    @GetMapping("/info/{itemId}")
    public IMOOCJSONResult getItems(@ApiParam(name = "itemId",value = "商品ID",required = true) @PathVariable String itemId){

        if (StringUtils.isBlank(itemId)){
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

        if (StringUtils.isBlank(itemId)){
            return IMOOCJSONResult.errorMsg("该商品ID不存在！");
        }
        //查询出商品详情页要展示的所有数据，并且封装到一个VO类中
        CommentCountsVO commentCounts = itemService.getCommentCounts(itemId);

        return IMOOCJSONResult.ok(commentCounts);
    }

    @ApiOperation(value = "查询商品评价信息",notes = "根据商品id获取商品评价详细信息",httpMethod = "GET")
    @GetMapping("/comments")
    public IMOOCJSONResult getComments(
            @ApiParam(name = "itemId",value = "商品ID",required = true) @RequestParam String itemId,
            @ApiParam(name = "level",value = "评论等级",required = false) @RequestParam Integer level,
            @ApiParam(name = "page",value = "查询下一页的第几页",required = false) @RequestParam Integer page,
            @ApiParam(name = "pageSize",value = "每一页显示的记录数",required = false) @RequestParam Integer pageSize){

        if (StringUtils.isBlank(itemId)){
            return IMOOCJSONResult.errorMsg("该商品ID不存在！");
        }
        if (page == null){
            page = COMMENT_PAGE;
        }
        if (pageSize == null){
            pageSize = COMMENT_PAGE_SIZE;
        }
        //查询出商品详情页要展示的所有数据，并且封装到一个VO类中
        PagedGridResult commentCounts = itemService.getItemComments(itemId,level,page,pageSize);

        return IMOOCJSONResult.ok(commentCounts);
    }

    @ApiOperation(value = "商品搜索",notes = "搜索商品列表",httpMethod = "GET")
    @GetMapping("/search")
    public IMOOCJSONResult searchItems(
            @ApiParam(name = "keywords",value = "关键字",required = true) @RequestParam String keywords,
            @ApiParam(name = "sort",value = "排序",required = false) @RequestParam String sort,
            @ApiParam(name = "page",value = "查询下一页的第几页",required = false) @RequestParam Integer page,
            @ApiParam(name = "pageSize",value = "每一页显示的记录数",required = false) @RequestParam Integer pageSize){

        if (StringUtils.isBlank(keywords)){
            return IMOOCJSONResult.errorMsg("该商品ID不存在！");
        }
        if (page == null){
            page = COMMENT_PAGE;
        }
        if (pageSize == null){
            pageSize = COMMENT_PAGE_SIZE;
        }
        //查询出商品详情页要展示的所有数据，并且封装到一个VO类中
        PagedGridResult searchItems = itemService.searchItems(keywords,sort,page,pageSize);

        return IMOOCJSONResult.ok(searchItems);
    }

    @ApiOperation(value = "三级分类下商品搜索",notes = "根据三级分类ID搜索商品列表",httpMethod = "GET")
    @GetMapping("/catItems")
    public IMOOCJSONResult searchItemsByThirdCat(
            @ApiParam(name = "catId",value = "三级分类ID",required = true) @RequestParam Integer catId,
            @ApiParam(name = "sort",value = "排序",required = false) @RequestParam String sort,
            @ApiParam(name = "page",value = "查询下一页的第几页",required = false) @RequestParam Integer page,
            @ApiParam(name = "pageSize",value = "每一页显示的记录数",required = false) @RequestParam Integer pageSize){

        if (catId == null){
            return IMOOCJSONResult.errorMsg("该商品ID不存在！");
        }
        if (page == null){
            page = COMMENT_PAGE;
        }
        if (pageSize == null){
            pageSize = COMMENT_PAGE_SIZE;
        }
        //查询出商品详情页要展示的所有数据，并且封装到一个VO类中
        PagedGridResult searchItems = itemService.searchItemsByThirdCat(catId,sort,page,pageSize);

        return IMOOCJSONResult.ok(searchItems);
    }

}
