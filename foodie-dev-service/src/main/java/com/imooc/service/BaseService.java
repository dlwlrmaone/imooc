package com.imooc.service;

import com.imooc.utils.PagedGridResult;

import java.util.List;

/**
 * 通用Service
 */
public interface BaseService {

    /**
     * 设置分页
     * @param list
     * @param page
     * @return
     */
    PagedGridResult setPagedGrid(List<?> list, Integer page);
}
