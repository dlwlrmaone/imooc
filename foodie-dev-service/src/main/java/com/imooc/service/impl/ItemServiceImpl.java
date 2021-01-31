package com.imooc.service.impl;

import com.imooc.enumclass.CommentLevelEnum;
import com.imooc.mapper.*;
import com.imooc.pojo.*;
import com.imooc.pojo.vo.CommentCountsVO;
import com.imooc.pojo.vo.ItemCommentVO;
import com.imooc.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Autowired
    private ItemsCommentsMapper itemsCommentsMapper;

    @Autowired
    private ItemsMapperCustom itemsMapperCustom;

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

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public CommentCountsVO getCommentCounts(String itemId) {

        Integer goodCounts = getCommentCounts(itemId, CommentLevelEnum.GOOD.type);
        Integer normalCounts = getCommentCounts(itemId, CommentLevelEnum.NORMAL.type);
        Integer badCounts = getCommentCounts(itemId, CommentLevelEnum.BAD.type);
        Integer totalCounts = goodCounts+normalCounts+badCounts;

        CommentCountsVO commentCountsVO = new CommentCountsVO();
        commentCountsVO.setGoodCounts(goodCounts);
        commentCountsVO.setNormalCounts(normalCounts);
        commentCountsVO.setBadCounts(badCounts);
        commentCountsVO.setTotalCounts(totalCounts);

        return commentCountsVO;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<ItemCommentVO> getItemComments(String itemId, Integer level) {
        //sql中有两个判断条件，使用map来接收
        HashMap<String,Object> map = new HashMap<>();
        map.put("itemId",itemId);
        map.put("level",level);

        List<ItemCommentVO> itemComment = itemsMapperCustom.getItemComment(map);
        return itemComment;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    Integer getCommentCounts(String itemId,Integer level){

        ItemsComments counts = new ItemsComments();
        counts.setItemId(itemId);
        if (level != null){
            counts.setCommentLevel(level);
        }
        return itemsCommentsMapper.selectCount(counts);
    }
}
