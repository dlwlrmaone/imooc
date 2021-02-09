package com.imooc.controller;

import com.imooc.pojo.Users;
import com.imooc.pojo.bo.UserBO;
import com.imooc.service.UserService;
import com.imooc.utils.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用户注册登录Controller
 */
@Api(value = "注册登录",tags = "用于注册登录的相关接口")
@RestController
@RequestMapping("passport")
public class PassportController {

    private static final Logger logger = LoggerFactory.getLogger(PassportController.class);

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
    public IMOOCJSONResult register(@RequestBody UserBO userBO,HttpServletRequest request,HttpServletResponse response){

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
        Users userResult = userService.createUser(userBO);
        userResult = setUserNullProp(userResult);
        CookieUtils.setCookie(request,response,"user", JsonUtils.objectToJson(userResult),true);

        //TODO 生成用户token，存入redis会话
        //TODO 同步购物车数据

        logger.info("{}用户注册成功！",username);
        return IMOOCJSONResult.ok();

    }

    @ApiOperation(value = "用户登录",notes = "用户登录及校验",httpMethod = "POST")
    @PostMapping("/userLogin")
    //RequestBody常用于接收json数据
    public IMOOCJSONResult userLogin(@RequestBody UserBO userBO, HttpServletRequest request, HttpServletResponse response) throws Exception {

        String username = userBO.getUsername();
        String password = userBO.getPassword();

        //校验用户名和密码
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)){
            return IMOOCJSONResult.errorMsg("用户名或密码不能为空！");
        }

        //实现登录
        Users userResult = userService.userLogin(username, MD5Utils.getMD5Str(password));
        if (userResult == null){
            return IMOOCJSONResult.errorMsg("用户名或密码不正确！");
        }
        userResult = setUserNullProp(userResult);
        CookieUtils.setCookie(request,response,"user", JsonUtils.objectToJson(userResult),true);

        //TODO 生成用户token，存入redis会话
        //TODO 同步购物车数据

        logger.info("{}用户登录成功！",username);
        return IMOOCJSONResult.ok(userResult);
    }

    /**
     * 将用户cookie重要信息设置为null
     * @param users
     * @return
     */
    private Users setUserNullProp(Users users){

        users.setBirthday(null);
        users.setCreatedTime(null);
        users.setPassword(null);
        users.setUpdatedTime(null);
        users.setRealname(null);
        users.setMobile(null);
        return users;
    }

    @ApiOperation(value = "用户退出",notes = "用户退出登录状态",httpMethod = "POST")
    @PostMapping("/userLogout")
    public IMOOCJSONResult logout(@RequestParam String userId, HttpServletRequest request,HttpServletResponse response){

        //清除用户相关cookie信息
        CookieUtils.deleteCookie(request,response,"user");

        //TODO 用户退出登录需要清空cookie
        //TODO 分布式会话中需要清除用户数据

        logger.info("用户已退出!");
        return IMOOCJSONResult.ok();
    }
}
