package com.imooc.service;

import com.imooc.pojo.Carousel;
import com.imooc.pojo.Category;
import com.imooc.pojo.vo.CategoryVO;

import java.util.List;

/**
 * 分类展示相关接口
 */
public interface CategoryService {

    /**
     * 查询所有一级分类
     * @return
     */
    List<Category> queryRootCategory();

    /**
     * 根据一级分类id查询二级分类信息
     * @param rootCatId
     * @return
     */
    List<CategoryVO> getSubCatList(Integer rootCatId);

}
