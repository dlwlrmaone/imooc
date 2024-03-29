package com.imooc.pojo.bo.center;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;
import java.util.Date;

/**
 * 前端传过来的json数据统一封装为BO对象
 * 用户中心-用户对象BO
 */
@ApiModel(value = "用户中心-用户对象BO",description = "从客户端，由用户传入的数据封装在此实体类中")
public class CenterUserBO {

    @ApiModelProperty(value = "用户名",name = "username",example = "imooc",required = true)
    private String username;
    @ApiModelProperty(value = "密码",name = "password",example = "123456",required = true)
    private String password;
    @ApiModelProperty(value = "确认密码",name = "confirmPassword",example = "123456",required = false)
    private String confirmPassword;
    @NotBlank(message = "用户昵称不能为空")
    @Length(max = 12,message = "用户昵称不能超过12位")
    @ApiModelProperty(value = "昵称",name = "nickname",example = "小猪",required = false)
    private String nickname;
    @Length(max = 12,message = "用户真实姓名不能超过12位")
    @ApiModelProperty(value = "真实姓名",name = "realname",example = "李宁",required = false)
    private String realname;
    @Pattern(regexp = "^(((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1}))+\\d{8})$",message = "手机号码格式不正确")
    @ApiModelProperty(value = "手机号码",name = "mobile",example = "18888888888",required = false)
    private String mobile;
    @Email(message = "邮箱格式不正确")
    @ApiModelProperty(value = "邮箱",name = "email",example = "123456@qq.com",required = false)
    private String email;
    @Min(value = 0,message = "性别选择不正确")
    @Max(value = 2,message = "性别选择不正确")
    @ApiModelProperty(value = "性别",name = "sex",example = "女",required = false)
    private Integer sex;
    @ApiModelProperty(value = "生日",name = "birthday",example = "1995-12-24",required = false)
    private Date birthday;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }
}
