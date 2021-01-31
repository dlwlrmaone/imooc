package com.imooc.controller;

import org.springframework.stereotype.Controller;

@Controller
public class BaseController {

    //默认每页显示的评论数量
    public static final Integer COMMENT_PAGE_SIZE = 10;

    //默认显示第几页
    public static final Integer COMMENT_PAGE = 1;
}
