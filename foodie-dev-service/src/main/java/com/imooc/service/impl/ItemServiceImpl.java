package com.imooc.service.impl;

import com.imooc.mapper.ItemsImgMapper;
import com.imooc.mapper.ItemsMapper;
import com.imooc.mapper.ItemsParamMapper;
import com.imooc.mapper.ItemsSpecMapper;
import com.imooc.pojo.*;
import com.imooc.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * 商品详情页相关实现
 */
@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemsMapper itemsMapper;

    @Autowired
    private ItemsImgMapper itemsImgMapper;

    @Autowired
    private ItemsParamMapper itemsParamMapper;

    @Autowired
    private ItemsSpecMapper itemsSpecMapper;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Items getItemsById(String itemId) {
        return itemsMapper.selectByPrimaryKey(itemId);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<ItemsImg> getItemImgList(String itemId) {

        Example itemImgExample = new Example(ItemsImg.class);
        Example.Criteria criteria = itemImgExample.createCriteria();
        criteria.andEqualTo("itemId",itemId);
        return itemsImgMapper.selectByExample(itemImgExample);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<ItemsSpec> getItemSpecList(String itemId) {
        Example itemSpecExample = new Example(ItemsSpec.class);
        Example.Criteria criteria = itemSpecExample.createCriteria();
        criteria.andEqualTo("itemId",itemId);
        return itemsSpecMapper.selectByExample(itemSpecExample);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public ItemsParam getItemParam(String itemId) {
        Example itemParamExample = new Example(ItemsParam.class);
        Example.Criteria criteria = itemParamExample.createCriteria();
        criteria.andEqualTo("itemId",itemId);
        return itemsParamMapper.selectOneByExample(itemParamExample);
    }
}
