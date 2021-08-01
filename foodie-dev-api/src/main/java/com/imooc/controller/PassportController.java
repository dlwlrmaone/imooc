package com.imooc.controller;

import com.imooc.pojo.Users;
import com.imooc.pojo.bo.ShopCartBO;
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
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户注册登录Controller
 */
@Api(value = "注册登录",tags = "用于注册登录的相关接口")
@RestController
@RequestMapping("passport")
public class PassportController extends BaseController{

    private static final Logger logger = LoggerFactory.getLogger(PassportController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private RedisOperator redisOperator;

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
        //同步购物车数据
        syncShopCartData(userResult.getId(),request,response);

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
        //同步购物车数据
        syncShopCartData(userResult.getId(),request,response);

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
        //用户退出登录需要清空cookie
        CookieUtils.deleteCookie(request,response,FOODIE_SHOPCART);
        //TODO 分布式会话中需要清除用户数据

        logger.info("用户已退出!");
        return IMOOCJSONResult.ok();
    }

    /**
     * 注册和登录成功后，同步cookie和redis购物车数据
     * 1.redis和cookie中均无数据，不做任何处理
     * 2.cookie有数据，redis无数据，同步数据到redis
     * 3.redis有数据，cookie无数据，同步数据到本地cookie
     * 4.cookie有数据，redis有数据，如果cookie中某个商品在redis中存在，
     * 则以cookie为主，删除redis中数据，同步cookie数据到redis（参考京东）
     * 5.同步到redis中后，覆盖本地cookie数据，保证本地cookie数据和redis一致，是最新的
     */
    private void syncShopCartData(String userId,HttpServletRequest request,HttpServletResponse response){

        //从redis中获取购物车数据
        String shopCartRedis = redisOperator.get(FOODIE_SHOPCART + ":" + userId);
        //从cookie中获取购物车数据
        String shopCartCookie = CookieUtils.getCookieValue(request,FOODIE_SHOPCART,true);
        if (StringUtils.isBlank(shopCartRedis)){
            //cookie有数据，redis无数据，同步数据到redis
            if (StringUtils.isNotBlank(shopCartCookie)){
                redisOperator.set(FOODIE_SHOPCART + ":" + userId,shopCartCookie);
            }
        }else {
            //cookie有数据，redis有数据，如果cookie中某个商品在redis中存在，则以cookie为主，删除redis中数据，同步cookie数据到redis（参考京东）
            if (StringUtils.isNotBlank(shopCartCookie)){
                /**
                 * 1.已经存在的，把cookie中对应的数量，覆盖redis（参考京东）
                 * 2.该项商品标记为待删除，统一放在待删除的list中
                 * 3.从cookie中清理所有待删除的商品
                 * 4.合并redis和cookie中的数据
                 * 5.更新到redis和cookie中
                 */
                List<ShopCartBO> shopCartRedisList = JsonUtils.jsonToList(shopCartRedis, ShopCartBO.class);
                List<ShopCartBO> shopCartCookieList = JsonUtils.jsonToList(shopCartCookie, ShopCartBO.class);
                //待删除list
                ArrayList<ShopCartBO> pendingDeleteList = new ArrayList<>();
                for (ShopCartBO redisShopCart : shopCartRedisList) {
                    String redisSpecId = redisShopCart.getSpecId();
                    for (ShopCartBO cookieShopCart : shopCartCookieList) {
                        String cookieSpecId = cookieShopCart.getSpecId();
                        //进行redis和cookie商品比对
                        if (redisSpecId.equals(cookieSpecId)){
                            //覆盖购买数量，不累加（参考京东）
                            redisShopCart.setBuyCounts(cookieShopCart.getBuyCounts());
                            //把cookieShopCart放入待删除列表，用于最后的删除和合并
                            pendingDeleteList.add(cookieShopCart);
                        }
                    }
                }
                //从现有cookie中删除对应覆盖过的商品数据
                shopCartCookieList.removeAll(pendingDeleteList);
                //合并两个list
                shopCartRedisList.addAll(shopCartCookieList);
                //更新到redis和cookie
                CookieUtils.setCookie(request,response,FOODIE_SHOPCART,JsonUtils.objectToJson(shopCartRedisList),true);
                redisOperator.set(FOODIE_SHOPCART + ":" + userId,JsonUtils.objectToJson(shopCartRedisList));
            }else {
                //redis有数据，cookie无数据，同步数据到本地cookie
                CookieUtils.setCookie(request,response,FOODIE_SHOPCART,shopCartRedis,true);
            }
        }
    }

}
