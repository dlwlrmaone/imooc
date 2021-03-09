package com.imooc.controller.center;

import com.imooc.controller.BaseController;
import com.imooc.pojo.Users;
import com.imooc.pojo.bo.center.CenterUserBO;
import com.imooc.resource.FileUpload;
import com.imooc.service.center.CenterUserService;
import com.imooc.utils.CookieUtils;
import com.imooc.utils.DateUtil;
import com.imooc.utils.IMOOCJSONResult;
import com.imooc.utils.JsonUtils;
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
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户中心-用户信息Controller
 */
@Api(value = "center-用户中心-用户信息",tags = "用户中心-用户信息页面的相关接口")
@RestController
@RequestMapping("userInfo")
public class CenterUserController extends BaseController {

    @Autowired
    public CenterUserService centerUserService;

    @Autowired
    public FileUpload fileUpload;

    @ApiOperation(value = "用户头像修改上传",notes = "通过用户ID修改上传用户头像",httpMethod = "POST")
    @PostMapping("/uploadFace")
    public IMOOCJSONResult uploadFace(
            @ApiParam(name = "userId",value = "用户ID",required = true) @RequestParam String userId,
            @ApiParam(name = "face",value = "用户头像",required = true) MultipartFile face,
            HttpServletRequest request, HttpServletResponse response) {

        //定义头像保存的地址
//        String facePath = USER_FACE_LOCATION + File.separator + userId;
        String facePath = fileUpload.getUserFaceLocation();
        //在路径上为每一个用户增加一个userid，用于区分不同用户上传
        String uploadPathPrefix = File.separator + userId;

        if (face != null){
            //获取上传头像文件名称
            String filename = face.getOriginalFilename();
            if (StringUtils.isNotBlank(filename)){
                //文件重命名
                String[] strings = filename.split("\\.");
                //获取文件后缀名
                String suffix = strings[strings.length - 1];
                if (!suffix.equalsIgnoreCase("jpg") &&
                    !suffix.equalsIgnoreCase("jpeg") &&
                    !suffix.equalsIgnoreCase("png")){
                    return IMOOCJSONResult.errorMsg("待上传头像图片格式不符合要求，请检查！");
                }
                //文件名重组（覆盖式上传），如果需要增量式上传，则加上上传时间文件名
                String newFaceName = "face-" + userId + "." + suffix;
                //定义头像真实保存位置
                String finalFacePath = facePath + uploadPathPrefix + File.separator + newFaceName;
                // 用于提供给web服务访问的地址
                uploadPathPrefix += ("/" + newFaceName);

                File outFile = new File(finalFacePath);
                FileOutputStream fileOutputStream = null;
                if (outFile.getParentFile() != null){
                    //创建文件夹
                    outFile.getParentFile().mkdirs();
                }
                //文件输出到目录文件路径
                try {
                    fileOutputStream = new FileOutputStream(outFile);
                    InputStream inputStream = face.getInputStream();
                    IOUtils.copy(inputStream,fileOutputStream);
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    if (fileOutputStream != null) {
                        try {
                            fileOutputStream.flush();
                            fileOutputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }else {
            return IMOOCJSONResult.errorMsg("上传用户头像不能为空！");
        }
        // 获取图片服务地址
        String faceServerUrl = fileUpload.getUserFaceServerUrl();

        // 由于浏览器可能存在缓存的情况，所以在这里，我们需要加上时间戳来保证更新后的图片可以及时刷新
        String finalUserFaceUrl = faceServerUrl + uploadPathPrefix
                + "?t=" + DateUtil.getCurrentDateString(DateUtil.DATE_PATTERN);

        // 更新用户头像到数据库
        Users updateUserFace = centerUserService.updateUserFace(userId, finalUserFaceUrl);

        updateUserFace = setUserNullProp(updateUserFace);
        CookieUtils.setCookie(request, response, "user",
                JsonUtils.objectToJson(updateUserFace), true);

        // TODO 后续要改，增加令牌token，会整合进redis，分布式会话

        return IMOOCJSONResult.ok();
    }

    @ApiOperation(value = "修改用户信息",notes = "通过用户ID修改用户中心的用户信息",httpMethod = "POST")
    @PostMapping("/update")
    public IMOOCJSONResult updateCenterUserInfo(
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
