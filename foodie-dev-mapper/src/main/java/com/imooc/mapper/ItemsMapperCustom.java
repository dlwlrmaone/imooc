package com.imooc.mapper;

import com.imooc.pojo.vo.ItemCommentVO;
import com.imooc.pojo.vo.SearchItemsVO;
import com.imooc.pojo.vo.ShopCartVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ItemsMapperCustom {

    /**
     * 获取商品评论详情
     * @param map
     * @return
     */
    List<ItemCommentVO> getItemComment(@Param("paramsMap") Map<String,Object> map);

    /**
     * 商品搜索
     * @param map
     * @return
     */
    List<SearchItemsVO> searchItems(@Param("paramsMap") Map<String,Object> map);

    /**
     * 三级分类下的商品搜索
     * @param map
     * @return
     */
    List<SearchItemsVO> searchItemsByThirdCat(@Param("paramsMap") Map<String,Object> map);

    /**
     * 购物车商品刷新
     * @param specIds
     * @return
     */
    List<ShopCartVO> getItemsBySpecIds(@Param("paramsList")List specIds);

}