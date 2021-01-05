package com.imooc.controller;

import com.imooc.pojo.bo.UserBO;
import com.imooc.service.UserService;
import com.imooc.utils.IMOOCJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(value = "注册登录",tags = "用于注册登录的相关接口")
@RestController
@RequestMapping("passport")
public class PassportController {

    @Autowired
    private UserService userService;

    @ApiOperation(value = "用户名校验",notes = "校验用户名是否存在",httpMethod = "GET")
    @GetMapping("/usernameIsExist")
    //RequestParam一般接受简单类型的属性，也可以接受对象类型
    public IMOOCJSONResult usernameIsExist(@RequestParam String username){

        //此处使用apache.commons.lang3包的StringUtils工具类进行非空校验
        if (StringUtils.isBlank(username)){
            return IMOOCJSONResult.errorMsg("用户名不能为空！");
        }
        boolean isExist = userService.queryUsernameIsExist(username);
        return isExist == true ? IMOOCJSONResult.errorMsg("用户名已存在！") : IMOOCJSONResult.ok();
    }

    @ApiOperation(value = "用户注册",notes = "用户注册及校验",httpMethod = "POST")
    @PostMapping("/register")
    //RequestBody常用于接收json数据
    public IMOOCJSONResult register(@RequestBody UserBO userBO){

        String username = userBO.getUsername();
        String password = userBO.getPassword();
        String confirmPassword = userBO.getConfirmPassword();

        //校验用户名和密码
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password) || StringUtils.isBlank(confirmPassword)){
            return IMOOCJSONResult.errorMsg("用户名或密码不能为空！");
        }
        boolean isExist = userService.queryUsernameIsExist(username);
        if (isExist){
            return IMOOCJSONResult.errorMsg("用户名已存在！");
        }
        if(password.length() < 6 || confirmPassword.length() < 6){
            return IMOOCJSONResult.errorMsg("密码长度不能小于6位！");
        }
        if(!password.equals(confirmPassword)){
            return IMOOCJSONResult.errorMsg("两次密码输入不一致！");
        }

        //实现注册
        userService.createUser(userBO);

        return IMOOCJSONResult.ok();

    }
}
