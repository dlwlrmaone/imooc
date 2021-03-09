package com.imooc.controller.center;

import com.imooc.controller.BaseController;
import com.imooc.pojo.Users;
import com.imooc.pojo.bo.center.CenterUserBO;
import com.imooc.resource.FileUpload;
import com.imooc.service.center.CenterOrderService;
import com.imooc.service.center.CenterUserService;
import com.imooc.utils.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户中心-订单信息Controller
 */
@Api(value = "center-用户中心-订单信息",tags = "用户中心-订单信息页面的相关接口")
@RestController
@RequestMapping("myOrders")
public class CenterOrderController extends BaseController {

    @Autowired
    public CenterOrderService centerOrderService;

    @ApiOperation(value = "查询订单列表信息",notes = "根据用户id获取订单列表信息",httpMethod = "POST")
    @PostMapping("/query")
    public IMOOCJSONResult getComments(
            @ApiParam(name = "userId",value = "用户ID",required = true) @RequestParam String userId,
            @ApiParam(name = "orderStatus",value = "订单状态",required = false) @RequestParam Integer orderStatus,
            @ApiParam(name = "page",value = "查询下一页的第几页",required = false) @RequestParam Integer page,
            @ApiParam(name = "pageSize",value = "每一页显示的记录数",required = false) @RequestParam Integer pageSize){

        if (StringUtils.isBlank(userId)){
            return IMOOCJSONResult.errorMsg("该用户ID不存在！");
        }
        if (page == null){
            page = COMMON_PAGE;
        }
        if (pageSize == null){
            pageSize = COMMON_PAGE_SIZE;
        }
        PagedGridResult orderInfo = centerOrderService.getMyOrderInfo(userId,orderStatus,page,pageSize);

        return IMOOCJSONResult.ok(orderInfo);
    }

}
