package com.imooc.service;

import com.imooc.pojo.*;
import com.imooc.pojo.vo.CommentCountsVO;
import com.imooc.utils.PagedGridResult;

import java.util.List;

/**
 * 商品详情页相关接口
 */
public interface ItemService {

    /**
     * 根据商品id查询商品
     * @return
     */
    Items getItemsById(String itemId);

    /**
     * 根据商品id查询商品图片
     * @param itemId
     * @return
     */
    List<ItemsImg> getItemImgList(String itemId);

    /**
     * 根据商品id查询商品规格信息
     * @param itemId
     * @return
     */
    List<ItemsSpec> getItemSpecList(String itemId);

    /**
     * 根据商品id查询商品参数
     * @param itemId
     * @return
     */
    ItemsParam getItemParam(String itemId);

    /**
     * 根据商品id查询商品评价数
     * @param itemId
     */
    CommentCountsVO getCommentCounts(String itemId);

    /**
     * 根据商品id和评价等级查询商品评价(分页)
     * @param itemId
     * @param level
     * @return
     */
    PagedGridResult getItemComments(String itemId, Integer level, Integer page, Integer pageSize);

    /**
     * 搜索商品(分页)
     * @param keywords
     * @param sort
     * @param page
     * @param pageSize
     * @return
     */
    PagedGridResult searchItems(String keywords, String sort, Integer page, Integer pageSize);

    /**
     * 三级分类下搜索商品(分页)
     * @param catId
     * @param sort
     * @param page
     * @param pageSize
     * @return
     */
    PagedGridResult searchItemsByThirdCat(Integer catId, String sort, Integer page, Integer pageSize);

}
