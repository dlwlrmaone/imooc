package com.imooc.service;

import com.imooc.pojo.*;
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

}
