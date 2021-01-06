package com.imooc.controller;

import com.imooc.service.StuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@RestController
public class StuFooController {

    @Autowired
    private StuService stuService;

    @GetMapping("/getStu")
    public Object getStu(int id){
        return stuService.getStuInfo(id);
    }

    @PostMapping("/saveStu")
    public Object saveStu(){
        stuService.saveStu();
        return "保存成功！";
    }

    @PostMapping("/updateStu")
    public Object updateStu(Integer id){
        stuService.updateStu(id);
        return "修改成功！";
    }

    @PostMapping("/deleteStu")
    public Object deleteStu(int id){
        stuService.deleteStu(id);
        return "删除成功！";
    }
}
