package com.imooc.mapper;

import com.imooc.pojo.vo.CategoryVO;
import com.imooc.pojo.vo.NewItemsVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CategoryMapperCustom {

    /**
     * 获取子分类
     * @param rootCatId
     * @return
     */
    List<CategoryVO> getSubCatList(Integer rootCatId);

    /**
     * 获取子分类下最新6个商品
     * @param map
     * @return
     */
    List<NewItemsVO> getSixNewItems(@Param("paramsMap") Map<String,Object> map);
}