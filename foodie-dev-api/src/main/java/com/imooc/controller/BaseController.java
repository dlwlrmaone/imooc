package com.imooc.controller;

import org.springframework.stereotype.Controller;

import java.io.File;

@Controller
public class BaseController {

    //cookie名称设置
    public static final String FOODIE_SHOPCART = "shopcart";

    //默认每页显示的评论数量
    public static final Integer COMMENT_PAGE_SIZE = 10;

    //默认显示第几页
    public static final Integer COMMENT_PAGE = 1;

    //支付中心调用地址
    String payUrl = "http://payment.t.mukewang.com/foodie-payment/payment/createMerchantOrder";

    //支付中心-》天天吃货后台，支付成功回调地址（natapp实现内网穿透，公网地址随时会变）
    String payReturnUrl = "http://7c35yq.natappfree.cc/orders/notifyMerchantOrderPaid";

    //用户头像上传的地址
    public static final String USER_FACE_LOCATION = File.separator + "Users" + File.separator + "dlwlrmaone"
            + File.separator + "Downloads" + File.separator + "foodie_docs" + File.separator + "foodie_face";
}
