package com.imooc.exception;

import com.imooc.utils.IMOOCJSONResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

/**
 * 自定义异常捕获助手类
 */
@RestControllerAdvice
public class CustomExceptionHandler {

    //MaxUploadSizeExceededException,上传文件大小超过最大限制异常
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public IMOOCJSONResult fileUploadMaxHandler(MaxUploadSizeExceededException exception){
        return IMOOCJSONResult.errorMsg("文件上传大小不能超过10MB，请处理后重新上传！");
    }
}
