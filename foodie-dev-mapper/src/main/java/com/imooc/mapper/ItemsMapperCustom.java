package com.imooc.mapper;

import com.imooc.pojo.vo.ItemCommentVO;
import com.imooc.pojo.vo.SearchItemsVO;
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

}