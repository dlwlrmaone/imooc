package com.imooc.controller.center;

import com.imooc.pojo.Users;
import com.imooc.service.center.CenterUserService;
import com.imooc.utils.IMOOCJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 用户中心Controller
 */
@Api(value = "center-用户中心",tags = "用户中心展示功能的相关接口")
@RestController
@RequestMapping("center")
public class CenterController {

    @Autowired
    public CenterUserService centerUserService;

    @ApiOperation(value = "用户中心-查询用户信息",notes = "通过用户ID查询用户中心的用户信息",httpMethod = "GET")
    @GetMapping("/userInfo")
    public IMOOCJSONResult updateCenterUserInfo(@ApiParam(name = "userId",value = "用户ID",required = true) @RequestParam String userId){

        Users userInfo = centerUserService.getUserInfo(userId);
        return IMOOCJSONResult.ok(userInfo);
    }


}
