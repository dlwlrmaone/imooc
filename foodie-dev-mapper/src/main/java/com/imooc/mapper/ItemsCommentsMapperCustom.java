package com.imooc.mapper;

import com.imooc.my.mapper.MyMapper;
import com.imooc.pojo.ItemsComments;
import com.imooc.pojo.vo.center.CenterCommentVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ItemsCommentsMapperCustom extends MyMapper<ItemsComments> {

    /**
     * 保存评价信息
     * @param map
     */
    void saveComments(Map<String,Object> map);

    /**
     * 查询历史评价信息
     * @param map
     * @return
     */
    List<CenterCommentVO> getMyComments(@Param("paramsMap")Map<String,Object> map);
}