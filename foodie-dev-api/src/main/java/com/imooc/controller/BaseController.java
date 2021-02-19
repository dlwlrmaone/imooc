package com.imooc.controller;

import org.springframework.stereotype.Controller;

@Controller
public class BaseController {

    public static final String FOODIE_SHOPCART = "shopcart";

    //默认每页显示的评论数量
    public static final Integer COMMENT_PAGE_SIZE = 10;

    //默认显示第几页
    public static final Integer COMMENT_PAGE = 1;
}
