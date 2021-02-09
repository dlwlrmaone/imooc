package com.imooc.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.imooc.enumclass.CommentLevelEnum;
import com.imooc.mapper.*;
import com.imooc.pojo.*;
import com.imooc.pojo.vo.CommentCountsVO;
import com.imooc.pojo.vo.ItemCommentVO;
import com.imooc.pojo.vo.SearchItemsVO;
import com.imooc.pojo.vo.ShopCartVO;
import com.imooc.service.ItemService;
import com.imooc.utils.DesensitizationUtil;
import com.imooc.utils.PagedGridResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
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

    @Autowired
    private ItemsCommentsMapper itemsCommentsMapper;

    @Autowired
    private ItemsMapperCustom itemsMapperCustom;

    /**
     * 商品查询
     * @param itemId
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Items getItemsById(String itemId) {
        return itemsMapper.selectByPrimaryKey(itemId);
    }

    /**
     * 商品图片展示
     * @param itemId
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<ItemsImg> getItemImgList(String itemId) {

        Example itemImgExample = new Example(ItemsImg.class);
        Example.Criteria criteria = itemImgExample.createCriteria();
        criteria.andEqualTo("itemId",itemId);
        return itemsImgMapper.selectByExample(itemImgExample);
    }

    /**
     * 商品详情展示
     * @param itemId
     * @return
     */
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

    /**
     * 评论数展示
     * @param itemId
     * @return
     */
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

    /**
     * 商品评论详情展示
     * @param itemId
     * @param level
     * @param page
     * @param pageSize
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedGridResult getItemComments(String itemId, Integer level, Integer page,Integer pageSize) {
        //sql中有两个判断条件，使用map来接收
        HashMap<String,Object> map = new HashMap<>();
        map.put("itemId",itemId);
        map.put("level",level);

        //对数据进行分页，在sql执行之前
        PageHelper.startPage(page,pageSize);

        List<ItemCommentVO> itemComment = itemsMapperCustom.getItemComment(map);
        //昵称信息脱敏
        for (ItemCommentVO vo: itemComment) {
            vo.setNickName(DesensitizationUtil.commonDisplay(vo.getNickName()));
        }
        return setPagedGrid(itemComment,page);
    }

    /**
     * 商品搜索
     * @param keywords
     * @param sort
     * @param page
     * @param pageSize
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedGridResult searchItems(String keywords, String sort, Integer page, Integer pageSize) {

        HashMap<String,Object> map = new HashMap<>();
        map.put("keywords",keywords);
        map.put("sort",sort);

        PageHelper.startPage(page,pageSize);
        List<SearchItemsVO> searchItems = itemsMapperCustom.searchItems(map);
        return setPagedGrid(searchItems,page);
    }

    /**
     * 三级分类下商品搜索
     * @param catId
     * @param sort
     * @param page
     * @param pageSize
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedGridResult searchItemsByThirdCat(Integer catId, String sort, Integer page, Integer pageSize) {

        HashMap<String,Object> map = new HashMap<>();
        map.put("catId",catId);
        map.put("sort",sort);

        PageHelper.startPage(page,pageSize);
        List<SearchItemsVO> searchItems = itemsMapperCustom.searchItemsByThirdCat(map);
        return setPagedGrid(searchItems,page);
    }

    /**
     * 购物车商品刷新
     * @param specIds
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<ShopCartVO> getItemsBySpecIds(String specIds) {

        String[] ids = specIds.split(",");
        ArrayList<String> specIdList = new ArrayList<>();
        //使用Collections工具类将数组数据放在list里面
        Collections.addAll(specIdList,ids);
        return itemsMapperCustom.getItemsBySpecIds(specIdList);

    }

    /**
     * 分页相关实现
     * @param list
     * @param page
     * @return
     */
    private PagedGridResult setPagedGrid(List<?> list,Integer page){
        PageInfo<?> pageList = new PageInfo<>(list);
        PagedGridResult gridResult = new PagedGridResult();
        gridResult.setPage(page);
        gridResult.setRows(list);
        gridResult.setTotal(pageList.getPages());
        gridResult.setRecords(pageList.getTotal());
        return gridResult;
    }

    /**
     * 获取评论数量
     * @param itemId
     * @param level
     * @return
     */
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
