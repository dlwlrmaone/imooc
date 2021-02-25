package com.imooc.controller.center;

import com.imooc.pojo.Users;
import com.imooc.pojo.bo.center.CenterUserBO;
import com.imooc.service.center.CenterUserService;
import com.imooc.utils.CookieUtils;
import com.imooc.utils.IMOOCJSONResult;
import com.imooc.utils.JsonUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用户中心-用户信息Controller
 */
@Api(value = "center-用户中心-用户信息",tags = "用户中心-用户信息页面的相关接口")
@RestController
@RequestMapping("userInfo")
public class CenterUserController {

    @Autowired
    public CenterUserService centerUserService;

    @ApiOperation(value = "修改用户中心用户信息",notes = "通过用户ID修改用户中心的用户信息",httpMethod = "POST")
    @PostMapping("/update")
    public IMOOCJSONResult getCenterUserInfo(
            @ApiParam(name = "userId",value = "用户ID",required = true) @RequestParam String userId,
            @ApiParam(name = "centerUserBO",value = "用户中心-用户BO",required = true) @RequestBody CenterUserBO centerUserBO,
            HttpServletRequest request, HttpServletResponse response){

        Users updateUserInfo = centerUserService.updateUserInfo(userId, centerUserBO);
        updateUserInfo = setUserNullProp(updateUserInfo);
        //将用户更新信息设置到cookie中
        CookieUtils.setCookie(request,response,"user", JsonUtils.objectToJson(updateUserInfo),true);

        //TODO 后续会使用redis进行优化，增加token校验
        return IMOOCJSONResult.ok();
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
}