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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            @ApiParam(name = "centerUserBO",value = "用户中心-用户BO",required = true) @RequestBody @Valid CenterUserBO centerUserBO,
            BindingResult result, HttpServletRequest request, HttpServletResponse response){

        //如果BindingResult有错误信息，就直接return错误信息
        if (result.hasErrors()){
            Map<String, String> errorMap = getErrors(result);
            return IMOOCJSONResult.errorMap(errorMap);
        }

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

    /**
     * 数据验证错误信息收集
     * @param result
     * @return
     */
    private Map<String,String> getErrors(BindingResult result){

        HashMap<String, String> map = new HashMap<>();
        List<FieldError> fieldErrors = result.getFieldErrors();
        for (FieldError error : fieldErrors) {
            //发生验证错误所对应的一个属性
            String errorField = error.getField();
            //发生验证错误所对应的错误信息
            String errorMessage = error.getDefaultMessage();
            map.put(errorField,errorMessage);
        }
        return map;
    }
}
