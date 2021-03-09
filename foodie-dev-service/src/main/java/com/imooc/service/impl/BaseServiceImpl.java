package com.imooc.service.impl;

import com.github.pagehelper.PageInfo;
import com.imooc.service.BaseService;
import com.imooc.utils.PagedGridResult;

import java.util.List;

/**
 * 通用Service实现类
 */
public class BaseServiceImpl implements BaseService {

    /**
     * 分页相关实现
     * @param list
     * @param page
     * @return
     */
    @Override
    public PagedGridResult setPagedGrid(List<?> list, Integer page) {
        PageInfo<?> pageList = new PageInfo<>(list);
        PagedGridResult gridResult = new PagedGridResult();
        gridResult.setPage(page);
        gridResult.setRows(list);
        gridResult.setTotal(pageList.getPages());
        gridResult.setRecords(pageList.getTotal());
        return gridResult;
    }
}
