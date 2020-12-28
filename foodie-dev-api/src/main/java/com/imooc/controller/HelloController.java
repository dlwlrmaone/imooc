package com.imooc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

//@Controller
@RestController//相比于Controller注解，返回的都是json数据
public class HelloController {

    @GetMapping("/hello")
    public Object hello(){
        return "hello imooc";
    }
}
