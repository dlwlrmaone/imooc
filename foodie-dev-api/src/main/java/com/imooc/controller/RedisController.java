package com.imooc.controller;

import com.imooc.utils.RedisOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("redis")
public class RedisController {

    @Autowired
    private RedisOperator redisOperator;

    @GetMapping("/set")
    public Object set(String key,String value){

        redisOperator.set(key,value);
        return "Redis Set OK!";
    }

    @GetMapping("/get")
    public String get(String key){

        return redisOperator.get(key);
    }

    @GetMapping("/delete")
    public Object delete(String key){

        redisOperator.del(key);
        return "Redis Delete OK!";
    }
}
